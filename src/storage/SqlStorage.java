package storage;

import com.sun.deploy.util.StringUtils;
import exception.ResumeExistsStorageException;
import exception.ResumeNotExistsStorageException;
import exception.StorageException;
import model.*;
import sql.ConnectionFactory;
import util.JsonParser;
import util.SqlHelper;

import java.sql.*;
import java.util.*;

/**
 * Created by Marisha on 17/03/2018.
 */
public class SqlStorage implements Storage {
    private final ConnectionFactory connectionFactory;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        try{
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e){
            throw new IllegalStateException(e);
        }
        this.connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    @Override
    public void clear() {
        executeQuery("DELETE FROM resume", ps -> {
            ps.execute();
            return null;
        });
    }

    @Override
    public void save(Resume r) {
        executeTransaction(conn -> {
            String uuid = r.getUuid();
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO resume(uuid, full_name) VALUES (?, ?)")){
                ps.setString(1, uuid);
                ps.setString(2, r.getFullName());
                ps.execute();
            } catch (SQLException e) {
                throw new ResumeExistsStorageException(uuid);
            }
            insertContacts(conn, r);
            insertSections(conn, r);
            return null;
        });
    }

    @Override
    public void update(Resume r) {
        executeTransaction(conn -> {
            String uuid = r.getUuid();
            try(PreparedStatement ps = conn.prepareStatement("UPDATE resume set FULL_NAME = ? where UUID = ?")){
                ps.setString(1, r.getFullName());
                ps.setString(2, uuid);
                if (ps.executeUpdate() == 0) {
                    throw new ResumeNotExistsStorageException(uuid);
                }
            }
            try(PreparedStatement ps = conn.prepareStatement("DELETE FROM contact WHERE resume_uuid = ?")){
                ps.setString(1, uuid);
                ps.execute();
            }
            insertContacts(conn, r);
            try(PreparedStatement ps = conn.prepareStatement("DELETE FROM section WHERE resume_uuid = ?")){
                ps.setString(1, uuid);
                ps.execute();
            }
            insertSections(conn, r);
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return executeTransaction(conn -> {
            Resume r = null;
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM resume r " +
                            "LEFT JOIN contact c " +
                            "ON r.uuid = c.resume_uuid " +
                            "WHERE r.uuid = ?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    throw new ResumeNotExistsStorageException(uuid);
                }
                r = new Resume(uuid, rs.getString("full_name"));
                do {
                    String type = rs.getString("type");
                    if (type != null) {
                        r.addContact(ContactType.valueOf(type), rs.getString("value"));
                    }
                } while (rs.next());
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM section s WHERE resume_uuid = ?")) {
                ps.setString(1, uuid);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    addSectionToResume(rs, r);
                }
            }
            return r;
        });
    }

    @Override
    public void delete(String uuid) {
        executeQuery("DELETE FROM resume WHERE UUID = ?", ps -> {
            ps.setString(1, uuid);
            if (!ps.execute()) {
                throw new ResumeNotExistsStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return executeTransaction(conn -> {
            Map<String,Resume> resumes = new LinkedHashMap<>();
            try(PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume ORDER BY full_name, uuid")){
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    resumes.put(rs.getString("uuid"), new Resume(rs.getString("uuid").trim(), rs.getString("full_name")));
                }
            }
            try(PreparedStatement ps = conn.prepareStatement("SELECT * FROM contact")){
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    resumes.get(rs.getString("resume_uuid")).addContact(ContactType.valueOf(rs.getString("type")), rs.getString("value"));
                }
            }
            try(PreparedStatement ps = conn.prepareStatement("SELECT * FROM section")){
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    addSectionToResume(rs, resumes.get(rs.getString("resume_uuid")));
                }
            }
            return new ArrayList<>(resumes.values());
        });
    }

    @Override
    public int getSize() {
        return executeQuery("SELECT count(*) as size FROM resume", ps -> {
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new StorageException("Can't get size of resume table");
            }
            return rs.getInt("size");
        });
    }

    private <T> T executeQuery(String query, SqlHelper.SqlExecutor<T> executor) {
        return SqlHelper.execute(connectionFactory, query, executor);
    }

    private <T> T executeTransaction(SqlHelper.SqlTransaction<T> executor) {
        return SqlHelper.executeTransaction(connectionFactory, executor);
    }

    private void insertContacts(Connection conn, Resume r) throws SQLException {
        Map<ContactType, String> contacts = r.getContacts();
        if (!contacts.isEmpty()){
            try(PreparedStatement pst = conn.prepareStatement("INSERT INTO contact(resume_uuid, type, value) VALUES (?, ?, ?)")){
                for (Map.Entry<ContactType, String> e: contacts.entrySet()) {
                    pst.setString(1, r.getUuid());
                    pst.setString(2, e.getKey().name());
                    pst.setString(3, e.getValue());
                    pst.addBatch();
                }
                pst.executeBatch();
            }
        }
    }

    private void insertSections(Connection conn, Resume r) throws SQLException {
        Map<SectionType, Section> sections = r.getSections();
        Map<SectionType, Section> textSections = new HashMap<>();
        Map<SectionType, Section> organizations = new HashMap<>();
        for (Map.Entry<SectionType, Section> e : sections.entrySet()) {
            SectionType key = e.getKey();
            Section value = e.getValue();
            switch (key) {
                case EXPERIENCE:
                case EDUCATION:
                    organizations.put(key, value);
                    break;
                default:
                    textSections.put(key, value);
            }
        }
        if (!textSections.isEmpty()) {
            insertTextSections(conn, textSections, r);
        }
        if (!organizations.isEmpty()) {
            insertOrganizations(conn, organizations, r);
        }
    }

    private void insertTextSections(Connection conn, Map<SectionType, Section> sections, Resume r) throws SQLException {
        try(PreparedStatement pst = conn.prepareStatement("INSERT INTO section(resume_uuid, type, value) VALUES (?, ?, ?)")){
            for (Map.Entry<SectionType, Section> e: sections.entrySet()) {
                pst.setString(1, r.getUuid());
                pst.setString(2, e.getKey().name());
                Section s = e.getValue();
                pst.setString(3, JsonParser.write(s, Section.class));
//                if (s instanceof TextSection){
//                    pst.setString(3, ((TextSection) s).getContent());
//                } else{
//                    pst.setString(3, StringUtils.join(((ListSection) e.getValue()).getItems(), "\n"));
//                }
                pst.addBatch();
            }
            pst.executeBatch();
        }
    }
    private void insertOrganizations(Connection conn, Map<SectionType, Section> sections, Resume r) throws SQLException{}

    private void addSectionToResume(ResultSet rs, Resume r) throws SQLException{
            String type = rs.getString("type");
            String value = rs.getString("value");
//            String[] values = value.split("\\n");
//            Section s = values.length == 1 ? new TextSection(value) : new ListSection<>(values);
//            r.addSection(SectionType.valueOf(type), s);
            r.addSection(SectionType.valueOf(type), JsonParser.read(value, Section.class));
    }
}

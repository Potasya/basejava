package storage;

import com.sun.org.apache.regexp.internal.RE;
import exception.ResumeExistsStorageException;
import exception.ResumeNotExistsStorageException;
import exception.StorageException;
import model.ContactType;
import model.Resume;
import sql.ConnectionFactory;
import util.SqlHelper;

import java.sql.*;
import java.util.*;

/**
 * Created by Marisha on 17/03/2018.
 */
public class SqlStorage implements Storage {
    public final ConnectionFactory connectionFactory;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
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
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return executeQuery(
                "SELECT * FROM resume r " +
                        "LEFT JOIN contact c " +
                        "ON r.uuid = c.resume_uuid " +
                        "WHERE r.uuid = ?", ps -> {
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new ResumeNotExistsStorageException(uuid);
                    }
                    Resume r = new Resume(uuid, rs.getString("full_name"));
                    do {
                        String type = rs.getString("type");
                        if (type != null){
                            r.addContact(ContactType.valueOf(rs.getString("type")), rs.getString("value"));
                        }
                    } while (rs.next());
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
            Map<String,Resume> resumes = new HashMap<>();
            try(PreparedStatement ps = conn.prepareStatement("SELECT * FROM resume")){
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    resumes.put(rs.getString("uuid"), new Resume(rs.getString("uuid").trim(), rs.getString("full_name")));
                }
            }
            try(PreparedStatement ps = conn.prepareStatement("SELECT * FROM contact")){
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    String resume_uuid = rs.getString("resume_uuid");
                    resumes.get(resume_uuid).addContact(ContactType.valueOf(rs.getString("type")), rs.getString("value"));
                }
            }
            List<Resume> res = new ArrayList<>(resumes.values());
            Collections.sort(res);
            return res;
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
}

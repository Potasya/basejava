package storage;

import exception.ResumeExistsStorageException;
import exception.ResumeNotExistsStorageException;
import exception.StorageException;
import model.Resume;
import sql.ConnectionFactory;
import util.SqlHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        execute("DELETE FROM resume", ps -> {
            ps.execute();
            return null;
        });
    }

    @Override
    public void save(Resume r) {
        execute("INSERT INTO resume(uuid, full_name) VALUES (?, ?)", ps -> {
            ps.setString(1, r.getUuid());
            ps.setString(2, r.getFullName());
            try {
                ps.execute();
            } catch (SQLException e) {
                throw new ResumeExistsStorageException(r.getUuid());
            }
            return null;
        });
    }

    @Override
    public void update(Resume r) {
        execute("UPDATE resume set FULL_NAME = ? where UUID = ?", ps -> {
            ps.setString(1, r.getFullName());
            ps.setString(2, r.getUuid());
            if (ps.executeUpdate() == 0) {
                throw new ResumeNotExistsStorageException(r.getUuid());
            }
            return null;
        });
    }

    @Override
    public Resume get(String uuid) {
        return execute("SELECT * FROM resume r WHERE r.uuid = ?", ps -> {
            ps.setString(1, uuid);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new ResumeNotExistsStorageException(uuid);
            }
            Resume r = new Resume(uuid, rs.getString("full_name"));
            return r;
        });
    }

    @Override
    public void delete(String uuid) {
        execute("DELETE FROM resume WHERE UUID = ?", ps -> {
            ps.setString(1, uuid);
            if (!ps.execute()) {
                throw new ResumeNotExistsStorageException(uuid);
            }
            return null;
        });
    }

    @Override
    public List<Resume> getAllSorted() {
        return execute("SELECT * FROM resume ORDER BY full_name, UUID", ps -> {
            List<Resume> list = new ArrayList<>();
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Resume(rs.getString("uuid").trim(), rs.getString("full_name")));
            }
            return list;
        });
    }

    @Override
    public int getSize() {
        return execute("SELECT count(*) as size FROM resume", ps -> {
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                throw new StorageException("Can't get size of resume table");
            }
            return rs.getInt("size");
        });
    }

    private <R> R execute(String query, SqlHelper.ThrowingFunction<PreparedStatement, R> function) {
        return SqlHelper.execute(connectionFactory, query, function);
    }
}

package util;

import exception.StorageException;
import sql.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Marisha on 18/03/2018.
 */
public class SqlHelper {

    public static <T> T execute(ConnectionFactory connectionFactory, String query, SqlExecutor<T> executor){
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            return executor.execute(ps);
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    public static <T> T executeTransaction(ConnectionFactory connectionFactory, SqlTransaction<T> executor){
        try (Connection conn = connectionFactory.getConnection()) {
            try {
                conn.setAutoCommit(false);
                T res = executor.execute(conn);
                conn.commit();
                return res;
            } catch (SQLException e){
                conn.rollback();
                throw new StorageException(e);
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    public interface SqlExecutor<T>{
        T execute(PreparedStatement t) throws SQLException;
    }

    public interface SqlTransaction<T> {
        T execute(Connection connection) throws SQLException;
    }
}

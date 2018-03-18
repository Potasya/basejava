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

    public static <R> R execute(ConnectionFactory connectionFactory, String query, ThrowingFunction<PreparedStatement, R> function){
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            return function.apply(ps);
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    public interface ThrowingFunction<T,R>{
        R apply(T t) throws SQLException;
    }
}

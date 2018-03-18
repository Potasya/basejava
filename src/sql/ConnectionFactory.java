package sql;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Marisha on 17/03/2018.
 */
public interface ConnectionFactory {
    Connection getConnection() throws SQLException;
}

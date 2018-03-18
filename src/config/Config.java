package config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Marisha on 16/03/2018.
 */
public class Config {
    private static final File PROPS = new File("config/resumes.properties");
    private static final Properties props = new Properties();
    private String storageDir;
    private String dbUser;
    private String dbPassword;
    private String dbUrl;
    private static final Config INSTANCE = new Config();

    public static Config get() {
        return INSTANCE;
    }

    private Config() {
        try(InputStream is = new FileInputStream(PROPS)){
            props.load(is);
            storageDir = props.getProperty("storage.dir");
            dbUser = props.getProperty("db.user");
            dbPassword = props.getProperty("db.password");
            dbUrl = props.getProperty("db.url");
        } catch (IOException e) {
            throw new IllegalStateException("Invalid config file " + PROPS.getAbsolutePath());
        }
    }

    public String getStorageDir() {
        return storageDir;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public String getDbUrl() {
        return dbUrl;
    }
}

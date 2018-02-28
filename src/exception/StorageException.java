package exception;

import java.io.IOException;

/**
 * Created by Marisha on 19/02/2018.
 */
public class StorageException extends RuntimeException {

    private final String uuid;

    public StorageException(String message, String uuid) {
        super(message);
        this.uuid = uuid;
    }

    public StorageException(String message) {
        super(message);
        this.uuid = null;
    }

    public StorageException(String message, String uuid, Exception e) {
        super(message, e);
        this.uuid = uuid;
    }

    public StorageException(String message, Exception e) {
        this(message, null, e);
    }

    public String getUuid() {
        return uuid;
    }
}

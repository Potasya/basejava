package exception;

/**
 * Created by Marisha on 19/02/2018.
 */
public class ResumeNotExistsStorageException extends StorageException {
    public ResumeNotExistsStorageException(String uuid) {
        super("Resume " + uuid + " doesn't exist", uuid);
    }
}

package exception;

/**
 * Created by Marisha on 19/02/2018.
 */
public class ResumeExistsStorageException extends StorageException {
    public ResumeExistsStorageException(String uuid) {
        super("Resume " + uuid + " already exists", uuid);
    }
}

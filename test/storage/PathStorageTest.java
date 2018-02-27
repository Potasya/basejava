package storage;

/**
 * Created by Marisha on 27/02/2018.
 */
public class PathStorageTest extends AbstractStorageTest{
    protected static final String FILE_STORAGE_PATH = "/Users/Marisha/basejava/storage";

    public PathStorageTest() {
        super(new PathStorage(FILE_STORAGE_PATH, new ObjectStreamStrategy()));
    }
}
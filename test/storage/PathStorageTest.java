package storage;

import storage.serializer.ObjectStreamSerializer;

/**
 * Created by Marisha on 27/02/2018.
 */
public class PathStorageTest extends AbstractStorageTest{

    public PathStorageTest() {
        super(new PathStorage(STORAGE_PATH, new ObjectStreamSerializer()));
    }
}
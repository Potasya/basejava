package storage;

import storage.serializer.DataStreamSerializer;

/**
 * Created by Marisha on 03/03/2018.
 */
public class DataPathStorageTest extends AbstractStorageTest {
    public DataPathStorageTest() {
        super(new PathStorage(STORAGE_PATH, new DataStreamSerializer()));
    }
}

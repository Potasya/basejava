package storage;

import storage.serializer.ObjectStreamSerializer;

/**
 * Created by Marisha on 27/02/2018.
 */
public class ObjectPathStorageTest extends AbstractStorageTest{

    public ObjectPathStorageTest() {
        super(new PathStorage(STORAGE_PATH, new ObjectStreamSerializer()));
    }
}
package storage;

import storage.serializer.ObjectStreamSerializer;

import java.io.File;

/**
 * Created by Marisha on 27/02/2018.
 */
public class ObjectFileStorageTest extends AbstractStorageTest{
    protected static final File FILE_STORAGE = new File(STORAGE_PATH);

    public ObjectFileStorageTest() {
        super(new FileStorage(FILE_STORAGE, new ObjectStreamSerializer()));
    }
}
package storage;

import storage.serializer.JsonSerializer;

/**
 * Created by Marisha on 03/03/2018.
 */
public class JsonPathStorageTest extends AbstractStorageTest {
    public JsonPathStorageTest() {
        super(new PathStorage<>(STORAGE_PATH, new JsonSerializer()));
    }
}

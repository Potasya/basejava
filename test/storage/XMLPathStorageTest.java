package storage;

import storage.serializer.XMLStreamSerializer;

import static org.junit.Assert.*;

/**
 * Created by Marisha on 01/03/2018.
 */
public class XMLPathStorageTest extends AbstractStorageTest{

    public XMLPathStorageTest() {
        super(new PathStorage(STORAGE_PATH, new XMLStreamSerializer()));
    }
}
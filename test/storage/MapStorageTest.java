package storage;

import model.Resume;

import static org.junit.Assert.*;

/**
 * Created by Marisha on 23/02/2018.
 */
public class MapStorageTest extends AbstractStorageTest{

    public MapStorageTest() {
        super(new MapStorage());
    }

    @Override
    protected void testOrder(Resume[] resumes) {
    }
}
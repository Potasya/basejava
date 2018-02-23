package storage;

import exception.ResumeExistsStorageException;
import exception.ResumeNotExistsStorageException;
import exception.StorageException;
import model.Resume;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Marisha on 23/02/2018.
 */
public abstract class AbstractArrayStorageTest {
    private static final Resume RESUME_1 = new Resume("1");
    private static final Resume RESUME_3 = new Resume("3");
    private static final Resume RESUME_2 = new Resume("2");
    private static final Resume RESUME_4 = new Resume("4");
    private static final Resume TEST = new Resume("TEST");
    private Storage storage;

    protected AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() throws Exception {
        storage.clear();
        storage.save(RESUME_1);
        storage.save(RESUME_3);
        storage.save(RESUME_2);
        storage.save(RESUME_4);
    }

    @Test
    public void clear() throws Exception {
        storage.clear();
        assertStorageSize(0);
    }

    @Test
    public void save() throws Exception {
        storage.save(TEST);
        assertStorageSize(5);
        assertStorageGet(TEST);
    }

    @Test(expected = ResumeExistsStorageException.class)
    public void saveExist() throws Exception {
        storage.save(RESUME_1);
    }

    @Test(expected = StorageException.class)
    public void saveOverflow() throws Exception {
        storage.clear();
        try {
            for (int i = 0; i < AbstractArrayStorage.STORAGE_LIMIT; i++) {
                storage.save(new Resume());
            }
        } catch (StorageException e){
            Assert.fail();
        }
        storage.save(new Resume());
    }

    @Test
    public void update() throws Exception {
        Resume r = new Resume("1");
        storage.update(r);
        assertTrue(r == storage.get("1"));
        assertFalse(RESUME_1 == storage.get("1"));
    }

    @Test(expected = ResumeNotExistsStorageException.class)
    public void updateNotFound() throws Exception {
        storage.update(TEST);
    }

    @Test
    public void get() throws Exception {
        assertStorageGet(RESUME_1);
        assertStorageGet(RESUME_2);
        assertStorageGet(RESUME_3);
        assertStorageGet(RESUME_4);
    }

    @Test(expected = ResumeNotExistsStorageException.class)
    public void getNotFound() throws Exception {
        storage.get(TEST.getUuid());
    }

    @Test(expected = ResumeNotExistsStorageException.class)
    public void delete() throws Exception {
        storage.delete(RESUME_1.getUuid());
        assertStorageSize(3);
        storage.get(RESUME_1.getUuid());
    }

    @Test(expected = ResumeNotExistsStorageException.class)
    public void deleteNotFound() throws Exception {
        storage.delete(TEST.getUuid());
    }

    @Test
    public void getAll() throws Exception {
        Resume[] resumes = storage.getAll();
        assertEquals(4, resumes.length);
    }

    @Test
    public void getSize() throws Exception {
        assertStorageSize(4);
    }

    private void assertStorageGet(Resume r){
        Assert.assertEquals(r, storage.get(r.getUuid()));
    }

    private void assertStorageSize(int size){
        Assert.assertEquals(size, storage.getSize());
    }
}
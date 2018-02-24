package storage;

import exception.ResumeExistsStorageException;
import exception.ResumeNotExistsStorageException;
import model.Resume;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Marisha on 23/02/2018.
 */
public abstract class AbstractStorageTest {
    protected static final Resume RESUME_1 = new Resume("1", "A");
    protected static final Resume RESUME_3 = new Resume("3", "C");
    protected static final Resume RESUME_2 = new Resume("2", "B");
    protected static final Resume RESUME_4 = new Resume("4", "D");
    private static final Resume TEST = new Resume("TEST", "TEST");
    protected Storage storage;

    protected AbstractStorageTest(Storage storage) {
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

    @Test
    public void update() throws Exception {
        Resume r = new Resume("1", "Aa");
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
    public void getAllSorted() throws Exception {
        List<Resume> resumes = storage.getAllSorted();
        assertEquals(4, resumes.size());
        Assert.assertEquals(resumes.get(0), RESUME_1);
        Assert.assertEquals(resumes.get(1), RESUME_2);
        Assert.assertEquals(resumes.get(2), RESUME_3);
        Assert.assertEquals(resumes.get(3), RESUME_4);
    }

    @Test
    public void getSize() throws Exception {
        assertStorageSize(4);
    }

    private void assertStorageGet(Resume r) {
        Assert.assertEquals(r, storage.get(r.getUuid()));
    }

    private void assertStorageSize(int size) {
        Assert.assertEquals(size, storage.getSize());
    }
}
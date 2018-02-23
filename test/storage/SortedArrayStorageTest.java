package storage;

import model.Resume;
import org.junit.Assert;

/**
 * Created by Marisha on 23/02/2018.
 */
public class SortedArrayStorageTest extends AbstractArrayStorageTest {

    public SortedArrayStorageTest() {
        super(new SortedArrayStorage());
    }

    @Override
    protected void testOrder(Resume[] resumes) {
        Assert.assertEquals(resumes[0], RESUME_1);
        Assert.assertEquals(resumes[1], RESUME_2);
        Assert.assertEquals(resumes[2], RESUME_3);
        Assert.assertEquals(resumes[3], RESUME_4);
    }
}
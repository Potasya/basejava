package storage;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by Marisha on 24/02/2018.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses(
        {
                ArrayStorageTest.class,
                SortedArrayStorageTest.class,
                ListStorageTest.class,
                MapUuidStorageTest.class,
                MapResumeStorageTest.class
        }
)
public class AllStorageTest {
}

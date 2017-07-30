import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];

    void clear() {
        Arrays.fill(storage,null);
    }

    void save(Resume r) {
        storage[size()] = r;
    }

    Resume get(String uuid) {

        for (int i = 0; i < size(); i++) {
            if (uuid.equals(storage[i].uuid)) {
                return storage[i];
            }
        }
        return null;
    }

    void delete(String uuid) {
        for (int i = 0; i < size(); i++) {
            if (uuid.equals(storage[i].uuid)){
                for (int j = i; j < size()-1; j++) {
                    storage[j] = storage[j+1];
                }
                storage[size()-1] = null;
                break;
            }
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        Resume[] resumes = new Resume[10000];
        int i = 0;
        for (Resume res: storage) {
            if (res != null) {
                resumes[i] = res;
                i++;
            } else break;
        }
        return Arrays.copyOf(resumes,i);
    }

    int size() {
        return getAll().length;
    }
}

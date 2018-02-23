package storage;

import model.Resume;

import java.util.ArrayList;

/**
 * Created by Marisha on 23/02/2018.
 */
public class ListStorage extends AbstractStorage {
    protected ArrayList<Resume> storage = new ArrayList();

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    protected void doSave(Resume r, Object index) {
        storage.add(r);
    }

    @Override
    protected boolean exists(Object index) {
        return index != null;
    }

    @Override
    protected void doUpdate(Resume r, Object index) {
        storage.set((Integer) index, r);
    }

    @Override
    protected Resume doGet(Object index) {
        return storage.get((Integer) index);
    }

    @Override
    protected void doDelete(Object index) {
        int ind = (Integer) index;
        storage.remove(ind);
    }

    @Override
    public Resume[] getAll() {
        return storage.toArray(new Resume[getSize()]);
    }

    @Override
    public int getSize() {
        return storage.size();
    }

    @Override
    protected Integer getSearchKey(String uuid) {
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).getUuid().equals(uuid)){
                return i;
            }
        }
        return null;
    }
}

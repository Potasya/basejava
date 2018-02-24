package storage;

import model.Resume;

import java.util.*;

/**
 * Created by Marisha on 23/02/2018.
 */
public class MapUuidStorage extends AbstractStorage {
    protected Map<String, Resume> storage = new HashMap<>();

    @Override
    protected void doSave(Resume r, Object uuid) {
        storage.put((String) uuid, r);
    }

    @Override
    protected boolean exists(Object uuid) {
        return storage.containsKey(uuid);
    }

    @Override
    protected Object getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected void doUpdate(Resume r, Object uuid) {
        doSave(r, uuid);
    }

    @Override
    protected Resume doGet(Object uuid) {
        return storage.get(uuid);
    }

    @Override
    protected void doDelete(Object uuid) {
        storage.remove(uuid);
    }

    @Override
    public void clear() {
        storage.clear();
    }

    @Override
    protected List<Resume> doCopyAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public int getSize() {
        return storage.size();
    }
}

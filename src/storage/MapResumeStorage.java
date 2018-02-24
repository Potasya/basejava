package storage;

import model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Marisha on 23/02/2018.
 */
public class MapResumeStorage extends AbstractStorage {
    protected Map<String, Resume> storage = new HashMap<>();

    @Override
    protected void doSave(Resume r, Object resumeKey) {
        storage.put(r.getUuid(), r);
    }

    @Override
    protected boolean exists(Object resumeKey) {
        return resumeKey != null;
    }

    @Override
    protected Resume getSearchKey(String uuid) {
        return storage.get(uuid);
    }

    @Override
    protected void doUpdate(Resume r, Object resumeKey) {
        doSave(r, resumeKey);
    }

    @Override
    protected Resume doGet(Object resumeKey) {
        return (Resume) resumeKey;
    }

    @Override
    protected void doDelete(Object resumeKey) {
        storage.remove(((Resume) resumeKey).getUuid());
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

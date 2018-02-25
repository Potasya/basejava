package storage;

import model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Marisha on 23/02/2018.
 */
public class MapResumeStorage extends AbstractStorage<Resume> {
    protected Map<String, Resume> storage = new HashMap<>();

    @Override
    protected void doSave(Resume r, Resume resumeKey) {
        storage.put(r.getUuid(), r);
    }

    @Override
    protected boolean exists(Resume resumeKey) {
        return resumeKey != null;
    }

    @Override
    protected Resume getSearchKey(String uuid) {
        return storage.get(uuid);
    }

    @Override
    protected void doUpdate(Resume r, Resume resumeKey) {
        doSave(r, resumeKey);
    }

    @Override
    protected Resume doGet(Resume resumeKey) {
        return resumeKey;
    }

    @Override
    protected void doDelete(Resume resumeKey) {
        storage.remove((resumeKey).getUuid());
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

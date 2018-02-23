package storage;

import exception.ResumeExistsStorageException;
import exception.ResumeNotExistsStorageException;
import model.Resume;

/**
 * Created by Marisha on 23/02/2018.
 */
public abstract class AbstractStorage implements Storage {

    @Override
    public void save(Resume r) {
        String uuid = r.getUuid();
        Object searchKey = getExistedSearchKey(uuid);
        doSave(r, searchKey);
    }

    protected abstract void doSave(Resume r, Object searchKey);

    protected abstract boolean exists(Object searchKey);

    protected abstract Object getSearchKey(String uuid);

    @Override
    public void update(Resume r) {
        Object searchKey = getNotExistedSearchKey(r.getUuid());
        doUpdate(r, searchKey);
    }

    protected abstract void doUpdate(Resume r, Object searchKey);

    public Resume get(String uuid) {
        Object searchKey = getNotExistedSearchKey(uuid);
        return doGet(searchKey);
    }

    protected abstract Resume doGet(Object searchKey);

    public void delete(String uuid) {
        Object searchKey = getNotExistedSearchKey(uuid);
        doDelete(searchKey);
    }

    protected abstract void doDelete(Object searchKey);

    private Object getExistedSearchKey(String uuid){
        Object searchKey = getSearchKey(uuid);
        if (exists(searchKey)){
            throw new ResumeExistsStorageException(uuid);
        }
        return searchKey;
    }

    private Object getNotExistedSearchKey(String uuid){
        Object searchKey = getSearchKey(uuid);
        if (!exists(searchKey)){
            throw new ResumeNotExistsStorageException(uuid);
        }
        return searchKey;
    }
}

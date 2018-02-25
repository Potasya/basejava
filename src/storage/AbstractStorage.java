package storage;

import exception.ResumeExistsStorageException;
import exception.ResumeNotExistsStorageException;
import model.Resume;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by Marisha on 23/02/2018.
 */
public abstract class AbstractStorage<SK> implements Storage {
    private static Logger LOG = Logger.getLogger(AbstractStorage.class.getName());

    @Override
    public void save(Resume r) {
        String uuid = r.getUuid();
        SK searchKey = getExistedSearchKey(uuid);
        doSave(r, searchKey);
    }

    protected abstract void doSave(Resume r, SK searchKey);

    protected abstract boolean exists(SK searchKey);

    protected abstract SK getSearchKey(String uuid);

    @Override
    public void update(Resume r) {
        LOG.info("update " + r);
        SK searchKey = getNotExistedSearchKey(r.getUuid());
        doUpdate(r, searchKey);
    }

    protected abstract void doUpdate(Resume r, SK searchKey);

    public Resume get(String uuid) {
        LOG.info("get " + uuid);
        SK searchKey = getNotExistedSearchKey(uuid);
        return doGet(searchKey);
    }

    protected abstract Resume doGet(SK searchKey);

    public void delete(String uuid) {
        LOG.info("delete " + uuid);
        SK searchKey = getNotExistedSearchKey(uuid);
        doDelete(searchKey);
    }

    protected abstract void doDelete(SK searchKey);

    private SK getExistedSearchKey(String uuid) {
        SK searchKey = getSearchKey(uuid);
        if (exists(searchKey)) {
            LOG.warning("Resume " + uuid + " already exists");
            throw new ResumeExistsStorageException(uuid);
        }
        return searchKey;
    }

    private SK getNotExistedSearchKey(String uuid) {
        SK searchKey = getSearchKey(uuid);
        if (!exists(searchKey)) {
            LOG.warning("Resume " + uuid + " doesn't exist");
            throw new ResumeNotExistsStorageException(uuid);
        }
        return searchKey;
    }

    @Override
    public List<Resume> getAllSorted() {
        LOG.info("getAllSorted");
        List<Resume> list = doCopyAll();
        Collections.sort(list);
        return list;
    }

    protected abstract List<Resume> doCopyAll();

}

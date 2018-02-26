package storage;

import exception.StorageException;
import model.Resume;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Marisha on 25/02/2018.
 */
public abstract class AbstractFileStorage extends AbstractStorage<File> {
    private final File dir;

    protected AbstractFileStorage(File dir) {
        Objects.requireNonNull(dir, "Storage directory mustn't be null");
        if(!dir.isDirectory()){
            throw new IllegalArgumentException(dir.getAbsolutePath() + " is not directory");
        }
        if (!dir.canRead() || !dir.canWrite()){
            throw new IllegalArgumentException(dir.getAbsolutePath() + " is not RW");
        }
        this.dir = dir;
    }

    @Override
    protected void doSave(Resume r, File file) {
        try {
            file.createNewFile();
            doWrite(r, file);
        } catch (IOException e) {
            throw new StorageException("IO error", r.getUuid(), e);
        }
    }

    protected abstract void doWrite(Resume r, File file) throws IOException;

    @Override
    protected boolean exists(File file) {
        return file.exists();
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(dir, uuid);
    }

    @Override
    protected void doUpdate(Resume r, File file) {
        try {
            doWrite(r, file);
        } catch (IOException e) {
            throw new StorageException("IO error", r.getUuid(), e);
        }
    }

    @Override
    protected Resume doGet(File file) {
        return doRead(file);
    }

    protected abstract Resume doRead(File file);

    @Override
    protected void doDelete(File file) {
        if (file.isDirectory()){
            throw new IllegalArgumentException(file.getAbsolutePath() + " is directory");
        }
        file.delete();
    }

    @Override
    protected List<Resume> doCopyAll() {
        List<Resume> list = new ArrayList<>();
        for(File f : dir.listFiles()){
            if (!f.isDirectory()) {
                list.add(doRead(f));
            }
        }
        return list;
    }

    @Override
    public void clear() {
        for(File f : dir.listFiles()){
            f.delete();
        }
    }

    @Override
    public int getSize() {
        return dir.listFiles(pathname -> !pathname.isDirectory()).length;
    }
}

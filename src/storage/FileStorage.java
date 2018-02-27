package storage;

import exception.StorageException;
import model.Resume;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Marisha on 25/02/2018.
 */
public class FileStorage<S extends SerializationStrategy> extends AbstractStorage<File> {
    private final File dir;
    private final S strategy;

    protected FileStorage(File dir, S strategy) {
        Objects.requireNonNull(dir, "Storage directory mustn't be null");
        Objects.requireNonNull(strategy, "Serialization strategy mustn't be null");
        if(!dir.isDirectory()){
            throw new IllegalArgumentException(dir.getAbsolutePath() + " is not directory");
        }
        if (!dir.canRead() || !dir.canWrite()){
            throw new IllegalArgumentException(dir.getAbsolutePath() + " is not RW");
        }
        this.dir = dir;
        this.strategy = strategy;
    }

    @Override
    protected void doSave(Resume r, File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new StorageException("Can't create file " + file.getAbsolutePath(), file.getName(), e);
        }
        doUpdate(r, file);
    }

    protected void doWrite(Resume r, OutputStream os) throws IOException{
        strategy.doWrite(r, os);
    }

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
            doWrite(r, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException("File write error: " + file.getAbsolutePath(), r.getUuid(), e);
        }
    }

    @Override
    protected Resume doGet(File file) {
        try {
            return doRead(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("File read error: " + file.getAbsolutePath(), file.getName(), e);
        }
    }

    protected Resume doRead(InputStream is) throws IOException{
        return strategy.doRead(is);
    }

    @Override
    protected void doDelete(File file) {
        if (!file.delete()){
            throw new StorageException("File delete error", file.getName());
        }
    }

    @Override
    protected List<Resume> doCopyAll() {
        List<Resume> list = new ArrayList<>();
        for(File f : getDirFiles()) {
            list.add(doGet(f));
        }
        return list;
    }

    @Override
    public void clear() {
        for (File f : getDirFiles()) {
            doDelete(f);
        }
    }

    @Override
    public int getSize() {
        return getDirFiles().length;
    }

    private File[] getDirFiles(){
        File[] files = dir.listFiles();
        if (files == null){
            throw new StorageException("Dir read error", null);
        }
        return files;
    }
}

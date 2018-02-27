package storage;

import exception.StorageException;
import model.Resume;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Created by Marisha on 27/02/2018.
 */
public class PathStorage<S extends SerializationStrategy> extends AbstractStorage<Path> {
    private final Path directory;
    private final S strategy;

    protected PathStorage(String dir, S strategy) {
        Objects.requireNonNull(dir, "Storage directory mustn't be null");
        Objects.requireNonNull(dir, "Storage directory mustn't be null");
        directory = Paths.get(dir);
        if(!Files.isDirectory(directory) || !Files.isReadable(directory) || !Files.isWritable(directory)){
            throw new IllegalArgumentException(dir + " is not directory or not readable/writable");
        }
        this.strategy = strategy;
    }

    @Override
    protected void doSave(Resume r, Path path) {
        try {
            Files.createFile(path);
        } catch (IOException e) {
            throw new StorageException("Can't create path " + path, path.toString(), e);
        }
        doUpdate(r, path);
    }

    protected void doWrite(Resume r, OutputStream os) throws IOException{
        strategy.doWrite(r, os);
    }

    @Override
    protected boolean exists(Path path) {
        return Files.exists(path);
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return directory.resolve(uuid);
    }

    @Override
    protected void doUpdate(Resume r, Path path) {
        try {
            doWrite(r, Files.newOutputStream(path));
        } catch (IOException e) {
            throw new StorageException("Path write error: " + path, r.getUuid(), e);
        }
    }

    @Override
    protected Resume doGet(Path path) {
        try {
            return doRead(new BufferedInputStream(Files.newInputStream(path)));
        } catch (IOException e) {
            throw new StorageException("Path read error: " + path, path.toString(), e);
        }
    }

    protected Resume doRead(InputStream is) throws IOException{
        return strategy.doRead(is);
    }

    @Override
    protected void doDelete(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new StorageException("Path delete error", path.toString());
        }
    }

    @Override
    protected List<Resume> doCopyAll() {
        List<Resume> list = new ArrayList<>();
        getDirPaths().forEach(path -> list.add(doGet(path)));
        return list;
    }

    @Override
    public void clear() {
        getDirPaths().forEach(this::doDelete);
    }

    @Override
    public int getSize() {
        return (int) getDirPaths().count();
    }

    private Stream<Path> getDirPaths(){
        try {
            return Files.list(directory);
        } catch (IOException e) {
            throw new StorageException("Directory read error", null);
        }
    }
}

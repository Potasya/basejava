package storage.serializer;

import exception.StorageException;
import model.Resume;
import storage.serializer.StreamSerializer;

import java.io.*;

/**
 * Created by Marisha on 27/02/2018.
 */
public class ObjectStreamSerializer implements StreamSerializer {

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try(ObjectOutputStream oos = new ObjectOutputStream(os)){
            oos.writeObject(r);
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try(ObjectInputStream ois = new ObjectInputStream(is)){
            return (Resume) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new StorageException("Can't read resume", e);
        }
    }
}

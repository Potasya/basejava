package storage;

import model.Resume;
import storage.serializer.StreamSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

/**
 * Created by Marisha on 28/02/2018.
 */
public abstract class AbstractStreamStorage<SK, S extends StreamSerializer> extends AbstractStorage<SK> {
    protected final S serializer;

    protected AbstractStreamStorage(S serializer) {
        Objects.requireNonNull(serializer, "Serializer mustn't be null");
        this.serializer = serializer;
    }

    protected void doWrite(Resume r, OutputStream os) throws IOException {
        serializer.doWrite(r, os);
    }

    protected Resume doRead(InputStream is) throws IOException{
        return serializer.doRead(is);
    }
}

package storage.serializer;

import model.Resume;
import util.JsonParser;

import java.io.*;

/**
 * Created by Marisha on 03/03/2018.
 */
public class JsonSerializer implements StreamSerializer{

    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try(Writer writer = new OutputStreamWriter(os)) {
            JsonParser.write(r, writer);
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try(Reader reader = new InputStreamReader(is)) {
            return JsonParser.read(reader, Resume.class);
        }
    }
}

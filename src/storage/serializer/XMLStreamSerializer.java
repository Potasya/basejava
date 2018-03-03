package storage.serializer;

import model.*;
import util.XMLParser;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Created by Marisha on 28/02/2018.
 */
public class XMLStreamSerializer implements StreamSerializer {
    private XMLParser xmlParser;

    public XMLStreamSerializer() {
        this.xmlParser = new XMLParser(Resume.class, Link.class,
                ListSection.class, TextSection.class, Organization.class, Organization.Position.class);
    }


    @Override
    public void doWrite(Resume r, OutputStream os) throws IOException {
        try(Writer wr = new OutputStreamWriter(os, StandardCharsets.UTF_8)) {
            xmlParser.marshall(r, wr);
        }
    }

    @Override
    public Resume doRead(InputStream is) throws IOException {
        try(Reader r = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            return xmlParser.unmarshall(r);
        }
    }
}

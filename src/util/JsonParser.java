package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.ListSection;
import model.Section;

import java.io.Reader;
import java.io.Writer;
import java.util.List;

/**
 * Created by Marisha on 03/03/2018.
 */
public class JsonParser {
    private static Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Section.class, new JsonSectionAdapter())
            .registerTypeAdapter(List.class, new JsonListAdapter())
            .create();

    public static <T> T read(Reader reader, Class<T> clazz){
        return GSON.fromJson(reader, clazz);
    }

    public static <T> void write(T object, Writer writer){
        GSON.toJson(object, writer);
    }
}

package util;

import com.google.gson.*;
import model.ListSection;

import java.lang.reflect.Type;

/**
 * Created by Marisha on 03/03/2018.
 */
public class JsonListSectionAdapter<T> implements JsonDeserializer<ListSection<T>>, JsonSerializer<ListSection<T>>{
    @Override
    public ListSection<T> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return null;
    }

    @Override
    public JsonElement serialize(ListSection<T> src, Type typeOfSrc, JsonSerializationContext context) {
        return null;
    }
}

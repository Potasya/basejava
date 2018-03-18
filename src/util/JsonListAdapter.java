package util;

import com.google.gson.*;
import model.ListSection;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marisha on 03/03/2018.
 */
public class JsonListAdapter<T> implements JsonDeserializer<List<T>>, JsonSerializer<List<T>> {

    public static final String CLASSNAME = "CLASSNAME";
    public static final String ARRAY_INSTANCE = "ARRAY_INSTANCE";

    @Override
    public List<T> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonPrimitive prim = (JsonPrimitive) jsonObject.get(CLASSNAME);
        Class clazz = Object.class;
        try {
            clazz = Class.forName(prim.getAsString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        List<T> list = new ArrayList<>();
        JsonArray favouritesArray = jsonObject.get(ARRAY_INSTANCE).getAsJsonArray();
        for (JsonElement jsonElement : favouritesArray)
            list.add(context.deserialize(jsonElement, clazz));
        return list;
    }

    @Override
    public JsonElement serialize(List<T> src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        String clazzName = src.isEmpty() ? Object.class.getName() : src.get(0).getClass().getName();
        jsonObject.addProperty(CLASSNAME, clazzName);
        JsonArray result = new JsonArray();
        for (T item : src) {
            result.add(context.serialize(item, item.getClass()));
        }
        jsonObject.add(ARRAY_INSTANCE, result);
        return jsonObject;
    }
}

package org.schabi.newpipe.extractor.utils;

import com.grack.nanojson.JsonArray;
import com.grack.nanojson.JsonObject;
import com.grack.nanojson.JsonParser;
import com.grack.nanojson.JsonParserException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.schabi.newpipe.extractor.exceptions.ParsingException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class JsonUtils {
    public static final JsonObject EMPTY_OBJECT = new JsonObject();
    public static final JsonArray EMPTY_ARRAY = new JsonArray();

    private JsonUtils() {
    }

    @Nonnull
    public static Object getValue(@Nonnull JsonObject object, @Nonnull String path) throws ParsingException {

        List<String> keys = Arrays.asList(path.split("\\."));
        object = getObject(object, keys.subList(0, keys.size() - 1));
        if (null == object) throw new ParsingException("Unable to get " + path);
        Object result = object.get(keys.get(keys.size() - 1));
        if (null == result) throw new ParsingException("Unable to get " + path);
        return result;
    }

    @Nonnull
    public static String getString(@Nonnull JsonObject object, @Nonnull String path) throws ParsingException {
        Object value = getValue(object, path);
        if (value instanceof String) {
            return (String) value;
        } else {
            throw new ParsingException("Unable to get " + path);
        }
    }

    @Nonnull
    public static Boolean getBoolean(@Nonnull JsonObject object, @Nonnull String path) throws ParsingException {
        Object value = getValue(object, path);
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else {
            throw new ParsingException("Unable to get " + path);
        }
    }

    @Nonnull
    public static Number getNumber(@Nonnull JsonObject object, @Nonnull String path) throws ParsingException {
        Object value = getValue(object, path);
        if (value instanceof Number) {
            return (Number) value;
        } else {
            throw new ParsingException("Unable to get " + path);
        }
    }

    @Nonnull
    public static JsonObject getObject(@Nonnull JsonObject object, @Nonnull String path) throws ParsingException {
        Object value = getValue(object, path);
        if (value instanceof JsonObject) {
            return (JsonObject) value;
        } else {
            throw new ParsingException("Unable to get " + path);
        }
    }

    @Nonnull
    public static JsonArray getArray(@Nonnull JsonObject object, @Nonnull String path) throws ParsingException {
        Object value = getValue(object, path);
        if (value instanceof JsonArray) {
            return (JsonArray) value;
        } else {
            throw new ParsingException("Unable to get " + path);
        }
    }

    @Nonnull
    public static List<Object> getValues(@Nonnull JsonArray array, @Nonnull String path) throws ParsingException {

        List<Object> result = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            JsonObject obj = array.getObject(i);
            result.add(getValue(obj, path));
        }
        return result;
    }

    @Nullable
    private static JsonObject getObject(@Nonnull JsonObject object, @Nonnull List<String> keys) {
        JsonObject result = object;
        for (String key : keys) {
            result = result.getObject(key);
            if (null == result) break;
        }
        return result;
    }

    public static JsonArray toJsonArray(final String responseBody) throws ParsingException {
        try {
            return JsonParser.array().from(responseBody);
        } catch (JsonParserException e) {
            throw new ParsingException("Could not parse JSON", e);
        }
    }

    public static JsonObject toJsonObject(final String responseBody) throws ParsingException {
        try {
            return JsonParser.object().from(responseBody);
        } catch (JsonParserException e) {
            throw new ParsingException("Could not parse JSON", e);
        }
    }

    /**
     * <p>Get an attribute of a web page as JSON
     *
     * <p>Originally a part of bandcampDirect.</p>
     * <p>Example HTML:</p>
     * <pre>
     * {@code
     * <p data-town="{&quot;name&quot;:&quot;Mycenae&quot;,&quot;country&quot;:&quot;Greece&quot;}">This is Sparta!</p>
     * }
     * </pre>
     * <p>Calling this function to get the attribute <code>data-town</code> returns the JsonObject for</p>
     * <pre>
     * {@code
     *   {
     *     "name": "Mycenae",
     *     "country": "Greece"
     *   }
     * }
     * </pre>
     * @param html     The HTML where the JSON we're looking for is stored inside a
     *                 variable inside some JavaScript block
     * @param variable Name of the variable
     * @return The JsonObject stored in the variable with this name
     */
    public static JsonObject getJsonData(final String html, final String variable)
            throws JsonParserException, ArrayIndexOutOfBoundsException {
        final Document document = Jsoup.parse(html);
        final String json = document.getElementsByAttribute(variable).attr(variable);
        return JsonParser.object().from(json);
    }

    public static List<String> getStringListFromJsonArray(@Nonnull final JsonArray array) {
        final List<String> stringList = new ArrayList<>(array.size());
        for (Object o : array) {
            if (o instanceof String) {
                stringList.add((String) o);
            }
        }
        return stringList;
    }
}

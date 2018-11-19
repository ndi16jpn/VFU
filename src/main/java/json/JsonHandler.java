package json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import email.SmtpSettings;

import java.util.List;

/**
 * Json hanterar implementation som använder Gson
 * @see <a href="https://github.com/google/gson">https://github.com/google/gson</a>
 */
public class JsonHandler implements JsonHelper {

    private Gson gson = new Gson();

    private JsonHandler() {
    }

    public static JsonHelper getJsonHelper() {
        return new JsonHandler();
    }

    @Override
    public String keyBooleanToJson(String key, Boolean value) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(key, value);
        return gson.toJson(jsonObject);
    }

    @Override
    public String keyBooleanKeyListToJson(String key, Boolean value, String keyList, List<String> valueList) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(key, value);
        JsonArray jsonArray = new JsonArray();
        valueList.forEach(jsonArray::add);
        jsonObject.add(keyList, jsonArray);
        return gson.toJson(jsonObject);
    }

    @Override
    public String smtpSettingsToJson(SmtpSettings smtpSettings) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        return gsonBuilder.create().toJson(smtpSettings);
    }

    @Override
    public SmtpSettings jsonToSmtpSettings(String json) {
        return gson.fromJson(json, SmtpSettings.class);
    }
}

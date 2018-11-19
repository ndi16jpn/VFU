package json;

import email.SmtpSettings;

import java.util.List;

/**
 * Json hanterare
 */
public interface JsonHelper {

    /**
     * Genererar json från (key,value)
     * @param key key
     * @param value value
     * @return json
     */
    String keyBooleanToJson(String key, Boolean value);

    /**
     * Genererar json från (key,value) och (keyList,valueList)
     * @param key key
     * @param value value
     * @param keyList keyList
     * @param valueList valueList
     * @return json
     */
    String keyBooleanKeyListToJson(String key, Boolean value, String keyList, List<String> valueList);

    /**
     * Genererar json från {@link SmtpSettings}
     * @param smtpSettings smtpSettings
     * @return json
     */
    String smtpSettingsToJson(SmtpSettings smtpSettings);

    /**
     * Parsar json till {@link SmtpSettings}
     * @param json json
     * @return {@link SmtpSettings}
     */
    SmtpSettings jsonToSmtpSettings(String json);

}

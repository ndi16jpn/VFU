package json;

import email.SmtpSettings;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class JsonHandlerTest {

    private JsonHelper jsonHelper;

    @Before
    public void setUp() {
        jsonHelper = JsonHandler.getJsonHelper();
    }

    @Test
    public void keyBooleanToJson() {
        String json = jsonHelper.keyBooleanToJson("key", true);
        assertEquals("{\"key\":true}", json);

        json = jsonHelper.keyBooleanToJson("keyASHdj", false);
        assertEquals("{\"keyASHdj\":false}", json);
    }

    @Test
    public void keyBooleanKeyListToJson() {
        String key = "key13";
        boolean value = false;
        String keyList = "listKey";
        List<String> valueList = new ArrayList<>();
        valueList.add("listVal0");
        valueList.add("listVal0");
        valueList.add("listVal1");
        valueList.add("agwioan");

        String expected = "{\"key13\":false,"
                + "\"listKey\":[\"listVal0\",\"listVal0\",\"listVal1\",\"agwioan\"]}";
        assertEquals(expected, jsonHelper.keyBooleanKeyListToJson(key, value, keyList, valueList));
    }

    @Test
    public void smtpSettingsToJson() {
        SmtpSettings smtpSettings = new SmtpSettings(
                "smtp.hig.se", 25,
                "", "",
                SmtpSettings.SmtpProtocol.plain
        );
        String expected = "{\n"
                + "  \"host\": \"smtp.hig.se\",\n"
                + "  \"port\": 25,\n"
                + "  \"username\": \"\",\n"
                + "  \"password\": \"\",\n"
                + "  \"smtpProtocol\": \"plain\"\n"
                + "}";
        assertEquals(expected, jsonHelper.smtpSettingsToJson(smtpSettings));
    }

    @Test
    public void smtpSettingsToJson2() {
        SmtpSettings smtpSettings = new SmtpSettings(
                "smtp.hig.se", 587,
                "user", "pw",
                SmtpSettings.SmtpProtocol.tls
        );
        String expected = "{\n"
                + "  \"host\": \"smtp.hig.se\",\n"
                + "  \"port\": 587,\n"
                + "  \"username\": \"user\",\n"
                + "  \"password\": \"pw\",\n"
                + "  \"smtpProtocol\": \"tls\"\n"
                + "}";
        assertEquals(expected, jsonHelper.smtpSettingsToJson(smtpSettings));
    }

    @Test
    public void jsonToSmtpSettings() {
        SmtpSettings smtpSettings = new SmtpSettings(
                "smtp.hig.se", 25,
                "", "",
                SmtpSettings.SmtpProtocol.plain
        );
        String json = jsonHelper.smtpSettingsToJson(smtpSettings);

        SmtpSettings smtpSettingsFromJson = jsonHelper.jsonToSmtpSettings(json);
        assertEquals("smtp.hig.se", smtpSettingsFromJson.getHost());
        assertEquals(25, smtpSettingsFromJson.getPort());
        assertEquals("", smtpSettingsFromJson.getUsername());
        assertEquals("", smtpSettingsFromJson.getPassword());
        assertEquals(SmtpSettings.SmtpProtocol.plain, smtpSettingsFromJson.getSmtpProtocol());
    }

    @Test
    public void jsonToSmtpSettings2() {
        SmtpSettings smtpSettings = new SmtpSettings(
                "smtp.hig.se", 443,
                "user", "pw",
                SmtpSettings.SmtpProtocol.ssl
        );
        String json = jsonHelper.smtpSettingsToJson(smtpSettings);

        SmtpSettings smtpSettingsFromJson = jsonHelper.jsonToSmtpSettings(json);
        assertEquals("smtp.hig.se", smtpSettingsFromJson.getHost());
        assertEquals(443, smtpSettingsFromJson.getPort());
        assertEquals("user", smtpSettingsFromJson.getUsername());
        assertEquals("pw", smtpSettingsFromJson.getPassword());
        assertEquals(SmtpSettings.SmtpProtocol.ssl, smtpSettingsFromJson.getSmtpProtocol());
    }

}

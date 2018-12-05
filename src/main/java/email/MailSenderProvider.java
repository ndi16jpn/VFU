package email;

import json.JsonHandler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Läser in smtp.json och ställer in smtpsettings
 */
public class MailSenderProvider {

    private static final SmtpSettings defaultSmtpSettings = new SmtpSettings(
            "vm-smtp-01.hig.se", 25,
            "", "",
            SmtpSettings.SmtpProtocol.plain
    );
    private static final Path SMTP_CONF_PATH = Paths.get("gmailsmtp.json");

    private static SmtpSettings smtpSettings;

    public static MailSender getMailSender() {
        if (smtpSettings == null) {
            try {
                String json = new String(Files.readAllBytes(SMTP_CONF_PATH), StandardCharsets.UTF_8);
                smtpSettings = JsonHandler.getJsonHelper().jsonToSmtpSettings(json);
            } catch (IOException e) {
                System.err.println("Error reading smtp conf file: " + e.getMessage());
                smtpSettings = defaultSmtpSettings;
            }
        }
        return new SimpleMailer(smtpSettings);
    }

    public static void createDefaultConfFileIfNotExists() {
        if (Files.notExists(SMTP_CONF_PATH)) {
            try {
                Files.write(
                        SMTP_CONF_PATH,
                        JsonHandler.getJsonHelper().smtpSettingsToJson(defaultSmtpSettings)
                                .getBytes(StandardCharsets.UTF_8)
                );
            } catch (IOException e) {
                System.err.println("Error writing smtp conf file: " + e.getMessage());
            }
        }
    }

}

package email;

import org.simplejavamail.email.Email;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.config.ServerConfig;
import org.simplejavamail.mailer.config.TransportStrategy;

import javax.mail.Message;

class SimpleMailer implements MailSender {

    private ServerConfig serverConfig;
    private TransportStrategy transportStrategy;

    SimpleMailer(SmtpSettings smtpSettings) {
        String host = smtpSettings.getHost();
        int port = smtpSettings.getPort();
        String username = smtpSettings.getUsername();
        String password = smtpSettings.getPassword();

        if (username.equals("")) {
            serverConfig = new ServerConfig(host, port);
        } else {
            serverConfig = new ServerConfig(host, port, username, password);
        }

        switch (smtpSettings.getSmtpProtocol()) {
            case plain:
                transportStrategy = TransportStrategy.SMTP_PLAIN;
                break;
            case ssl:
                transportStrategy = TransportStrategy.SMTP_SSL;
                break;
            case tls:
                transportStrategy = TransportStrategy.SMTP_TLS;
                break;
        }
    }

    @Override
    public void sendMail(String to, String subject, String message) {
        try {
            Email email = new Email();
            String from = "vfusocionom-noreply@hig.se";
            email.setFromAddress(from, from);
            email.setReplyToAddress(from, from);
            email.addRecipient(to, to, Message.RecipientType.TO);
            email.setSubject(subject);
            email.setTextHTML(message);
            new Mailer(serverConfig, transportStrategy).sendMail(email, true);
        } catch (Exception e) {
            System.err.println("Failed to send email to: " + to + "\nerr:" + e.getMessage());
        }
    }

}

package email;

/**
 * Datahållare för smtpsettings
 */
public class SmtpSettings {

    private String host;
    private int port;
    private String username;
    private String password;
    private SmtpProtocol smtpProtocol;

    public enum SmtpProtocol {
        plain,
        ssl,
        tls
    }

    public SmtpSettings(String host, int port, String username, String password, SmtpProtocol smtpProtocol) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.smtpProtocol = smtpProtocol;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public SmtpProtocol getSmtpProtocol() {
        return smtpProtocol;
    }

    @Override
    public String toString() {
        return String.format("host: '%s'\n"
                        +  "port: '%d'\n"
                        +  "username: '%s'\n"
                        +  "password: '%s'\n"
                        +  "smtpProtocol: '%s'\n",
                host, port, username, password, smtpProtocol);
    }
}

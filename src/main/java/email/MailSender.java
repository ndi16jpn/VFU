package email;

/**
 * Inteface för att skicka mail
 */
public interface MailSender {
    /**
     * Skicka mail
     * @param to till vem
     * @param subject ämne
     * @param message meddelande
     */
    void sendMail(String to, String subject, String message);

}

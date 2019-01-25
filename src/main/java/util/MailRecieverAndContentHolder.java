package util;

public class MailRecieverAndContentHolder {
    private String reciever;
    private String content;

    public MailRecieverAndContentHolder(String reciever, String content) {
        this.reciever = reciever;
        this.content = content;
    }

    public String getReciever() {
        return reciever;
    }

    public String getContent() {
        return content;
    }
}

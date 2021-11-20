package domain;


import java.io.Serializable;
import java.time.LocalDate;

public class Message implements Serializable {
    private long senderID;
    private String usernameSender;
    private String destConversation;


    private String message;
    private LocalDate date;

    public Message(long senderID, String usernameSender, String usernameReceiver, String message) {
        this.senderID = senderID;
        this.usernameSender = usernameSender;

        this.destConversation = usernameReceiver;

        this.message = message;
        this.date = LocalDate.now();
    }

    public String getUsernameReceiver() {
        return usernameReceiver;
    }

    public Message(String usernameSender, String destConversation, String message) {
        this.usernameSender = usernameSender;
        this.destConversation = destConversation;
        this.message = message;
        this.date = LocalDate.now();
    }

    public String getDestConversation() {
        return destConversation;
    }

    public Message(String message) {
        this.message = message;
    }

    public long getSenderID() {
        return senderID;
    }

    public String getUsernameSender() {
        return usernameSender;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public String toString() {

        return usernameSender+ ": " + message;

    }

    public String getMessage() {
        return message;
    }
}

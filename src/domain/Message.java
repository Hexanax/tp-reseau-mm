package domain;


import java.io.Serializable;
import java.time.LocalDate;

public class Message implements Serializable {
    private long senderID;
    private String usernameSender;

    private String usernameReceiver;

    private String message;
    private LocalDate date;

    public Message(long senderID, String usernameSender, String usernameReceiver, String message) {
        this.senderID = senderID;
        this.usernameSender = usernameSender;
        this.usernameReceiver = usernameReceiver;
        this.message = message;
    }

    public Message(String usernameSender, String usernameReceiver, String message) {
        this.usernameSender = usernameSender;
        this.usernameReceiver = usernameReceiver;
        this.message = message;
        this.date = LocalDate.now();
    }

    public String getUsernameReceiver() {
        return usernameReceiver;
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
        return usernameSender + ": " + message;
    }

    public String getMessage() {
        return message;
    }
}

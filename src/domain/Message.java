package domain;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class Message implements Serializable {
    private long senderID;
    private String usernameSender;

    private String receiverID;
    private String message;
    private LocalDate date;


    public Message(long senderID, String usernameSender, String receiverID, String message) {
        this.senderID = senderID;
        this.usernameSender = usernameSender;
        this.receiverID = receiverID;
        this.message = message;
    }

    public Message(String usernameSender, String receiverID, String message) {
        this.usernameSender = usernameSender;
        this.receiverID = receiverID;
        this.message = message;
        this.date = LocalDate.now();
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
        return senderID + " : " + message;
    }

    public String getMessage() {
        return message;
    }
}

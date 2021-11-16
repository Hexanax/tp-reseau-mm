package stream;


import java.io.Serializable;

public class Message implements Serializable {
    private long senderID;
    private String usernameSender;

    private long receiverID;
    private String message;

    public Message(long senderID, String usernameSender, long receiverID, String message) {
        this.senderID = senderID;
        this.usernameSender = usernameSender;
        this.receiverID = receiverID;
        this.message = message;
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

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                '}';
    }
}

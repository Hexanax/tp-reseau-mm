package stream;

public class Message {
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

    public long getSenderID() {
        return senderID;
    }

    public String getUsernameSender() {
        return usernameSender;
    }



}

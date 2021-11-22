package server;

import domain.Conversation;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Data implements Serializable {
    public Map<String, Conversation> conversations;

    public Data(Map<String, Conversation> conversations) {
        this.conversations = conversations;
    }
}

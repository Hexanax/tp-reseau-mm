package domain;

import java.io.Serializable;

public class SystemMessage implements Serializable {
    public SystemMessageType type;

    public String content;

    private SystemMessage(SystemMessageType type, String content) {
        this.type = type;
        this.content = content;
    }

    public static SystemMessage newLoginRequest(String senderUsername){
        return new SystemMessage(SystemMessageType.LOGIN_REQUEST, senderUsername);
    }
    public static SystemMessage newConversationRequest(String conversationDetails){
        return new SystemMessage(SystemMessageType.NEW_CONVERSATION_REQUEST, conversationDetails);
    }
    public static SystemMessage privateConversationRequest(String conversationDetails){
        return new SystemMessage(SystemMessageType.PRIVATE_CONVERSATION_REQUEST, conversationDetails);
    }
    public static SystemMessage conversationConnectRequest(String connectionDetails){
        return new SystemMessage(SystemMessageType.CONVERSATION_CONNECT_REQUEST, connectionDetails);
    }

    public static SystemMessage conversationConnectOK(String connectionDetails){
        return new SystemMessage(SystemMessageType.CONVERSATION_CONNECT_OK, connectionDetails);
    }


}


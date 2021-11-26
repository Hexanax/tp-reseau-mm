package domain;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class SystemMessage implements Serializable {
    public SystemMessageType type;

    public Map<String, String> params;

    private SystemMessage(SystemMessageType type, Map<String, String> params) {
        this.type = type;
        params = new HashMap<>();
    }


    public static SystemMessage newLoginRequest(String username){
        return new SystemMessage(SystemMessageType.LOGIN_REQUEST, Map.of("username", username));
    }
    public static SystemMessage newConversationRequest(Map<String, String> parameters){
        return new SystemMessage(SystemMessageType.NEW_CONVERSATION_REQUEST, parameters);
    }
    public static SystemMessage privateConversationRequest(Map<String, String> parameters){
        return new SystemMessage(SystemMessageType.PRIVATE_CONVERSATION_REQUEST, parameters);
    }
    public static SystemMessage conversationConnectRequest(Map<String, String> parameters){
        return new SystemMessage(SystemMessageType.CONVERSATION_CONNECT_REQUEST, parameters);
    }

    public static SystemMessage conversationConnectOK(Map<String, String> parameters){
        return new SystemMessage(SystemMessageType.CONVERSATION_CONNECT_OK, parameters);
    }


}


package domain;

import java.io.Serializable;

/**
 * SystemMessage contain system command information and facilitate the communication of commands between
 */
public class SystemMessage implements Serializable {
    public SystemMessageType type;

    public String content;

    private SystemMessage(SystemMessageType type, String content) {
        this.type = type;
        this.content = content;
    }

    /**
     * Creates a System message that request a login
     * @param senderUsername logged in username
     * @return a SystemMessage with the proper request type and parameters
     */
    public static SystemMessage newLoginRequest(String senderUsername){
        return new SystemMessage(SystemMessageType.LOGIN_REQUEST, senderUsername);
    }
    /**
     * Creates a System message that requests the creation of a new conversation
     * @param conversationDetails relevant details of the conversation
     * @return a SystemMessage with the proper request type and parameters
     */
    public static SystemMessage newConversationRequest(String conversationDetails){
        return new SystemMessage(SystemMessageType.NEW_CONVERSATION_REQUEST, conversationDetails);
    }
    /**
     * Creates a System message that request a private conversation with another user
     * @param conversationDetails relevant details of the conversation
     * @return a SystemMessage with the proper request type and parameters
     */
    public static SystemMessage privateConversationRequest(String conversationDetails){
        return new SystemMessage(SystemMessageType.PRIVATE_CONVERSATION_REQUEST, conversationDetails);
    }
    /**
     * Creates a System message that requests a connection to a conversation
     * @param connectionDetails relevant details of the conversation
     * @return a SystemMessage with the proper request type and parameters
     */
    public static SystemMessage conversationConnectRequest(String connectionDetails){
        return new SystemMessage(SystemMessageType.CONVERSATION_CONNECT_REQUEST, connectionDetails);
    }

    public static SystemMessage conversationConnectOK(String connectionDetails){
        return new SystemMessage(SystemMessageType.CONVERSATION_CONNECT_OK, connectionDetails);
    }


}


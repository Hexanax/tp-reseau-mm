package domain;

import java.io.Serializable;

public class SystemMessage implements Serializable {
    public SystemMessageType type;
    public String senderUsername;

    private SystemMessage(SystemMessageType type, String senderUsername) {
        this.type = type;
        this.senderUsername = senderUsername;
    }

    public static SystemMessage newLoginRequest(String senderUsername){
        return new SystemMessage(SystemMessageType.LOGIN_REQUEST, senderUsername);
    }
}


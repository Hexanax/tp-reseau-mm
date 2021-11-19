package domain;

import java.io.Serializable;

public class SystemMessage implements Serializable {
    public SystemMessageType type;
    public String requestedUsername;

    private SystemMessage(SystemMessageType type, String requestedUsername) {
        this.type = type;
        this.requestedUsername = requestedUsername;
    }

    public static SystemMessage newLoginRequest(String requestedUsername){
        return new SystemMessage(SystemMessageType.LOGIN_REQUEST, requestedUsername);
    }
}


package server;

import domain.Message;
import domain.SystemMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static domain.SystemMessageType.LOGIN_REQUEST;

public class Service {

    private List<ClientSocketThread> clientSocketThreadList;
    private Map<String, ClientSocketThread> onlineClientThreadMap;

    public Service() {
        clientSocketThreadList = new ArrayList<>();
        onlineClientThreadMap = new HashMap<>();
    }


    public void addClientSocketThread(ClientSocketThread clientSocketThread) {
        this.clientSocketThreadList.add(clientSocketThread);
    }

    public void sendMessageToClient(Message message) {
        int j = 0;
        if (message != null) {
            System.out.println(message);
            // Send message to all client
            ClientSocketThread cst = onlineClientThreadMap.get(message.getUsernameSender());
            cst.sendMessage(message);
        }
    }

    public void handleSystemMessage(SystemMessage systemMessage) {
        if (LOGIN_REQUEST.equals(systemMessage.type)) {
            String username = systemMessage.requestedUsername;
            for (ClientSocketThread cst : clientSocketThreadList) {
                if (cst.getUsername().equals(username)){
                    onlineClientThreadMap.put(username, cst);
                    return;
                }
            }
        }
    }
}

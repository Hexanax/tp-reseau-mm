package server;

import domain.Message;

import java.util.ArrayList;
import java.util.List;

public class Service {

    List<ClientSocketThread> clientSocketThreadList = new ArrayList<>();

    public Service(){

    }

    public void setClientSocketThreadList(List<ClientSocketThread> clientSocketThreadList) {
        this.clientSocketThreadList = clientSocketThreadList;
    }

    public void addClientSocketThreadList(ClientSocketThread clientSocketThread){
        this.clientSocketThreadList.add(clientSocketThread);
    }

    public void sendMessageToClient(String  nameClientDest, Message message){
        int j=0;
        if(message != null){
            System.out.println(message);
            // Send message to all client
            for (int k = 0; k < clientSocketThreadList.size(); k++) {
                if(k == j) {
                    continue;
                }
                ClientSocketThread temp = clientSocketThreadList.get(k);
                if(nameClientDest.equals( temp.getUserID())) temp.sendMessage(message);
            }
        }
    }
}

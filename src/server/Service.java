package server;

import domain.Conversation;
import domain.Message;
import domain.Pair;
import domain.SystemMessage;

import java.util.*;

import static domain.SystemMessageType.LOGIN_REQUEST;

public class Service {

    private List<ClientSocketThread> clientSocketThreadList;
    private Map<String, ClientSocketThread> onlineClientThreadMap;
    private Map<String, Conversation> conversationsMap;

    public Service() {
        clientSocketThreadList = new ArrayList<>();
        onlineClientThreadMap = new HashMap<>();
        conversationsMap = new HashMap<>();

        createConversation("helloworld",Arrays.asList("Matheus","Luiza","Nilson"));
    }

    public void testCreateConversation(String conversationID, List<String>membersUsernames){
        createConversation(conversationID, membersUsernames);
    }
    public void createConversation(String conversationID, List<String>membersUsernames){
        if (membersUsernames.size() < 2){
            System.out.println("A conversation needs more than one interlocutor.");
            return;
        }
        Conversation newConversation = new Conversation(conversationID, membersUsernames);
        conversationsMap.put(conversationID,newConversation);
    }

    public void addMemberToConversation(String conversationID, String memberID){
        Conversation  conversation = conversationsMap.get(conversationID);
        if(conversation == null) return;
        conversation.addMember(memberID);
    }

    public void removeMemberOfConversation(String conversationID, String memberID){
        Conversation  conversation = conversationsMap.get(conversationID);
        if(conversation == null) return;
        conversation.removeMember(memberID);
    }


    public void addClientSocketThread(ClientSocketThread clientSocketThread) {
        this.clientSocketThreadList.add(clientSocketThread);
    }

    public void sendMessageToOnlineClients(Message message) {
        if (message == null) return ;
        System.out.println(message);
        // Send message to all clients

        // We get the members of the conversation
        Conversation destConversation = conversationsMap.get(message.getDestConversation());
        if(destConversation == null) return;

        List<Pair<String, Integer>> members = destConversation.getMembers();
        List<String> onlineMembers = new ArrayList<>();

        // Send the message to the connected members
        for (Pair<String, Integer> member: members) {
            ClientSocketThread cst;
            cst = onlineClientThreadMap.get(member.getKey());
            if(cst == null) continue; // TODO : saveMessage
            if( ! member.getKey().equals(message.getUsernameSender()))cst.sendMessage(message);
            onlineMembers.add(member.getKey());
        }

        destConversation.addMessage(message);
        //destConversation.updateIndexOnlineMembers(onlineMembers);
    }

    public void handleSystemMessage(SystemMessage systemMessage) {
        switch(systemMessage.type){
            case LOGIN_REQUEST -> {
                String username = systemMessage.senderUsername;
                System.out.println("Received login request from: " + username);
                for (ClientSocketThread cst : clientSocketThreadList) {
                    if (cst.getUsername() != null && cst.getUsername().equals(username)){
                        onlineClientThreadMap.put(username, cst);
                        return;
                    }
                }
            }
            case LOGIN_ACCEPT -> {
                System.out.println("LOGIN_ACCEPT MESSAGE");
            }

            default -> throw new IllegalStateException("Unexpected value: " + systemMessage.type);
        }
    }
}

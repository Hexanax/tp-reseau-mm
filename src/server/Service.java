package server;

import domain.Conversation;
import domain.Message;

import domain.SystemMessage;

import java.util.*;



import static domain.SystemMessageType.LOGIN_REQUEST;

    private List<ClientSocketThread> clientSocketThreadList;
    private Map<String, ClientSocketThread> onlineClientThreadMap;
    private Map<String, Conversation> conversationsMap;

    public Service() {
        clientSocketThreadList = new ArrayList<>();
        onlineClientThreadMap = new HashMap<>();
        conversationsMap = new HashMap<>();

        createConversation("helloworld",Arrays.asList("Matheus","Luiza","Nilson"));
    }

    public void createConversation(String conversationID, String creatorUsername){
        if(conversationsMap.containsKey(conversationID)){
            System.out.println("Conversation : " + conversationID + " already exists.");
            return;
        }
        Conversation newConversation = new Conversation(conversationID, creatorUsername);
        conversationsMap.put(conversationID,newConversation);
    }
    public void createConversation(String conversationID, List<String> membersUsernames){
        if(conversationsMap.containsKey(conversationID)){
            System.out.println("Conversation : " + conversationID + " already exists.");
            return;
        }
        Conversation newConversation = new Conversation(conversationID, membersUsernames);
        conversationsMap.put(conversationID,newConversation);
    }

    public void showConversationsToClient(ClientSocketThread clientSocketThread){
        StringBuilder conversations  = new StringBuilder();
        for (Conversation conversation : conversationsMap.values()) {
            conversations.append(conversation);
        }
        clientSocketThread.showConversations(conversations.toString());
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

        Map<String,Integer> members = destConversation.getMembers();
        List<String> onlineMembers = new ArrayList<>();

        // Send the message to the connected members
        for (String memberUsername: members.keySet()) {
            ClientSocketThread cst;
            cst = onlineClientThreadMap.get(memberUsername);
            if(cst == null) continue; // TODO : saveMessage
            if( ! memberUsername.equals(message.getUsernameSender()))cst.sendMessage(message);
            onlineMembers.add(memberUsername);
        }

        destConversation.addMessage(message);
        //destConversation.updateIndexOnlineMembers(onlineMembers);
    }

    public void handleSystemMessage(SystemMessage systemMessage) {
        switch(systemMessage.type){
            case LOGIN_REQUEST -> {
                String username = systemMessage.content;
                System.out.println("Received login request from: " + username);
                for (ClientSocketThread cst : clientSocketThreadList) {
                    if (cst.getUsername() != null && cst.getUsername().equals(username)){
                        onlineClientThreadMap.put(username, cst);
                        return;
                    }

                }
            }
            case LOGIN_OK -> {
                System.out.println("LOGIN_OK MESSAGE");
            }
            case NEW_CONVERSATION_REQUEST -> {
                // first case contains the name of the newConversation. All the others contain usernames to add
                String senderUsername  = systemMessage.content.split(";")[0];
                String newConversationID  = systemMessage.content.split(";")[1];

                if(conversationsMap.containsKey(newConversationID)){
                    addMemberToConversation(newConversationID, senderUsername);
                    System.out.println("Conversation "+ newConversationID + " already existed. You have been added to " +
                            "it.");
                }else{
                    createConversation(newConversationID, senderUsername);
                    addMemberToConversation(newConversationID, senderUsername);
                    System.out.println("Conversation "+ newConversationID + " created with success");
                }
                String details = senderUsername + ";" + newConversationID;
                onlineClientThreadMap.get(senderUsername).sendSystemMessage(SystemMessage.conversationConnectOK(details));

            }
            case CONVERSATION_CONNECT_REQUEST -> {

                String senderUsername  = systemMessage.content.split(";")[0];
                String conversationID  = systemMessage.content.split(";")[1];

                if(conversationsMap.containsKey(conversationID)){
                    addMemberToConversation(conversationID, senderUsername);
                    System.out.println("Conversation "+ conversationID + " already existed. You have been added to " +
                            "it.");
                    String details = senderUsername + ";" + conversationID;
                    onlineClientThreadMap.get(senderUsername).sendSystemMessage(SystemMessage.conversationConnectOK(details));

                    return;
                }else{
                    System.out.println("Conversation "+ conversationID + " created with success");
                }
                /*  - Recuperer l'username demandeur
                    - Vers quelle conversation il souhaite se connecter?
                        - Existante ?
                            - OUI-> Déjà membre?
                                - oui -> connecter
                                - non -> add aux membres
                            - NON
                                - Create new conversation avec le nom recupéré
                                - add l'user à la conversation
                                - demander à l'user de renseigner les autres users à add dans la conversation
                                - add les users à la conversation
                *
                */

            } case ADD_MEMBER -> {
                // first case contains the name of the newConversation. All the others contain usernames to add
                String senderUsername  = systemMessage.content.split(";")[0];
                String conversationID  = systemMessage.content.split(";")[1];
                String newMemberUsername  = systemMessage.content.split(";")[2];

                if(conversationsMap.containsKey(conversationID)){
                    addMemberToConversation(conversationID, newMemberUsername);
                    return;
                }
                System.out.println("Conversation "+ conversationID + " does not exist.");
            }


            default -> throw new IllegalStateException("Unexpected value: " + systemMessage.type);
        }
    }
}

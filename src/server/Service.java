package server;

import domain.Conversation;
import domain.Message;

import domain.SystemMessage;

import java.util.*;


/**
 * Service is the layer that handles all business logic of the app
 */
public class Service {
    private List<ClientSocketThread> clientSocketThreadList;
    private Map<String, ClientSocketThread> onlineClientThreadMap;
    private Map<String, Conversation> conversationsMap;
    private Database db;
    public Service() {
        clientSocketThreadList = new ArrayList<>();
        onlineClientThreadMap = new HashMap<>();
        conversationsMap = new HashMap<>();
        db = new Database();
        conversationsMap = db.getData().conversations;

    }

    /**
     * createConversation creates a new conversation
     * @param conversationID The id of the conversation
     * @param creatorUsername The username of who created the conversation
     */
    public void createConversation(String conversationID, String creatorUsername){
        if(conversationsMap.containsKey(conversationID)){
            System.out.println("Conversation : " + conversationID + " already exists.");
            return;
        }
        Conversation newConversation = new Conversation(conversationID, creatorUsername);
        conversationsMap.put(conversationID,newConversation);
        db.save(new Data(conversationsMap));
    }

    /**
     * createConversation creates a new conversation
     * @param conversationID the id of the converastion
     * @param membersUsernames the usernames of the members in the conversation
     */
    public void createConversation(String conversationID, List<String> membersUsernames){
        if(conversationsMap.containsKey(conversationID)){
            System.out.println("Conversation : " + conversationID + " already exists.");
            return;
        }
        Conversation newConversation = new Conversation(conversationID, membersUsernames);
        conversationsMap.put(conversationID,newConversation);
    }

    /**
     * showConversationsToClient sends a list of conversations to the client
     * @param clientSocketThread the socket used to communicate to the client
     */
    public void showConversationsToClient(ClientSocketThread clientSocketThread){
        StringBuilder conversations  = new StringBuilder();
        for (Conversation conversation : conversationsMap.values()) {
            conversations.append(conversation);
        }
        clientSocketThread.showConversations(conversations.toString());
        clientSocketThread.showConversations(conversations.toString());
    }


    /**
     * addMemberToConversation adds a member to the conversation
     * @param conversationID id of the converation
     * @param memberID id (also known as username) of them member
     */
    public void addMemberToConversation(String conversationID, String memberID){
        Conversation  conversation = conversationsMap.get(conversationID);
        if(conversation == null) return;
        conversation.addMember(memberID);
        db.save(new Data(conversationsMap));
    }

    /** removeMemberOfConversation removes a member from a conversation
     * @param conversationID id of the converation
     * @param memberID id (also known as username) of them member
     */
    public void removeMemberOfConversation(String conversationID, String memberID){
        Conversation  conversation = conversationsMap.get(conversationID);
        if(conversation == null) return;
        conversation.removeMember(memberID);
    }


    /**
     * addClientSocketThread adds the client socket thread to the list of threads that the service is aware of
     * @param clientSocketThread the socket thread of the client
     */
    public void addClientSocketThread(ClientSocketThread clientSocketThread) {
        this.clientSocketThreadList.add(clientSocketThread);
    }

    /**
     * handleMessage handles the message that was sent to the server
     * @param message is the message that must be handled
     */
    public void handleMessage(Message message) {
        if (message == null) return ;
        System.out.println(message);

        // We get the members of the conversation
        Conversation destConversation = conversationsMap.get(message.getDestConversation());
        if(destConversation == null) return;
        sendMessageToOnlineClients(destConversation, message);
        destConversation.addMessage(message);
        db.save(new Data(conversationsMap));
    }

    /**
     * sendMessageToOnlineClients sends a message to all concerned online clients
     * @param conversation the conversation the message is in which contains the memebrs that will recieve the message
     * @param message the message that will be sent
     */
    private void sendMessageToOnlineClients(Conversation conversation, Message message) {

        // Send message to all clients
        Map<String,Integer> members = conversation.getMembers();
        List<String> onlineMembers = new ArrayList<>();

        // Send the message to the connected members
        for (String memberUsername: members.keySet()) {
            ClientSocketThread cst;
            cst = onlineClientThreadMap.get(memberUsername);
            if(cst == null) continue;
            if( ! memberUsername.equals(message.getUsernameSender()))cst.sendMessage(message);
            onlineMembers.add(memberUsername);
        }
    }

    /**
     * Connects a user to a conversation
     * @param username the username of the user
     * @param connectionDetails the details of the connection
     */
    private void connectUserToConversation(String username, String connectionDetails){
        onlineClientThreadMap.get(username).sendSystemMessage(SystemMessage.conversationConnectOK(connectionDetails));
    }

    /**
     * disconnectUser disconnects the user from the server gracefully
     * @param username the username that is disconnecting
     */
    public void disconnectUser(String username){
        this.clientSocketThreadList.remove(onlineClientThreadMap.get(username));
        this.onlineClientThreadMap.remove(username);
    }

    /**
     * handleSystemMessage handles a system message (command) from a user
     * @param systemMessage
     */
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
                connectUserToConversation(senderUsername,details);
                db.save(new Data(conversationsMap));
            }
            case PRIVATE_CONVERSATION_REQUEST -> {
                String senderUsername  = systemMessage.content.split(";")[0];
                String requestedUsername  = systemMessage.content.split(";")[1];

                String privateConversationID = senderUsername.compareTo(requestedUsername) < 0 ?
                        senderUsername+requestedUsername : requestedUsername+senderUsername;
                String details = senderUsername + ";" + privateConversationID;

                if(conversationsMap.containsKey(privateConversationID)){
                    connectUserToConversation(senderUsername, details);
                    // set input thread conversation id == privateconversationID
                }else{
                    //create conversation having id == private conversationid
                    createConversation(privateConversationID,senderUsername);

                    // set input thread conversation id == privateconversationID
                    connectUserToConversation(senderUsername, details);
                }
                db.save(new Data(conversationsMap));
            }
            case CONVERSATION_CONNECT_REQUEST -> {

                String senderUsername  = systemMessage.content.split(";")[0];
                String conversationID  = systemMessage.content.split(";")[1];

                if(conversationsMap.containsKey(conversationID)){
                    addMemberToConversation(conversationID, senderUsername);
                    System.out.println("Conversation "+ conversationID + " already existed. You have been added to " +
                            "it.");
                    String details = senderUsername + ";" + conversationID;
                    connectUserToConversation(senderUsername,details);
                    ClientSocketThread cst = onlineClientThreadMap.get(senderUsername);
                    Conversation conversation = conversationsMap.get(conversationID);
                    for ( Message message : conversation.getMessages()) {
                        cst.sendMessage(message);
                    }
                    return;
                }else{
                    //create conversation having id == conversationID
                    createConversation(conversationID,senderUsername);

                    connectUserToConversation(senderUsername,conversationID);

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
                db.save(new Data(conversationsMap));
            }
            case ADD_MEMBER -> {
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

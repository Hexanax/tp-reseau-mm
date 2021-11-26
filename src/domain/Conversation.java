package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Conversation represents conversations between users, which are groups of users talking together
 */
public class Conversation implements Serializable {
    private Integer index = 0;

    private String conversationID;
    // For each user, we save the index of the last message they read
    private Map<String, Integer> members;

    private List<Message> messages;

    public Conversation(String conversationID, List<String> usernamesUsers){
        this.conversationID = conversationID;
        this.members = new HashMap<>();
        this.messages = new ArrayList<>();
        index = 0;
        for (String username:usernamesUsers) {
            this.members.put(username,index);
        }
    }

    public Conversation(String conversationID, String creatorUsername) {
        this.conversationID = conversationID;
        this.members = new HashMap<>();
        this.messages = new ArrayList<>();
        index = 0;
        this.members.put(creatorUsername,index);
    }

    /**
     * addMessage adds a message to the conversation
     * @param message the message added to the conversation
     */
    public void addMessage(Message message){
        messages.add(message);
        index++;
    }

    /**
     * getMessages gets the messages in the conversation
     * @return the list of messages
     */
    public List<Message> getMessages() {
        return messages;
    }

    public void updateIndexOnlineMembers(List<String> onlineMembers){
        for (String member: onlineMembers ) {
            System.out.print("remove warning");
            //TODO : rajouter 1 à l'index du Pair
        }
    }

    /**
     * showHistory shows the conversation history
     */
    public void showHistory(){
        System.out.println("======================");
        System.out.println("Conversation : " + conversationID);
        System.out.println("Users : ");
        for (String memberUsername : members.keySet() ) {
            System.out.println(" > " + memberUsername);
        }
        System.out.println("======================");
        for (Message msg:messages) {
            System.out.println(msg);
        }
    }
    public void showUnreadMessages(String memberUsername){
        Integer index = members.get(memberUsername);
        System.out.println("======================");
        System.out.println("Conversation : " + conversationID);
        System.out.println("Unread messages");
        System.out.println("======================");
        for (int i = index; i < messages.size() ; i++) {
            System.out.println(messages.get(i));
        }
    }

    /**
     * getMembers returns the map of members in this conversation
     * @return the map of members in this conversation
     */
    public Map<String, Integer> getMembers() {
        return members;
    }

    public Integer getIndex(){
        return index;
    }

    /**
     * addMember adds a member to the conversation
     * @param username member to be added
     */
    public void addMember(String username){
        if (members.keySet().contains(username)) {
            return;
        }
        this.members.put(username, 0);
    }

    /**
     * Removes the member from the conversation
     * @param username username of the member that must be removed
     */
    public void removeMember(String username){
        this.members.remove(username);
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "conversationID='" + conversationID + '\'' +
                '}';
    }
}

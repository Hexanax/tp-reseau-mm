package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void addMessage(Message message){
        messages.add(message);
        index++;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void updateIndexOnlineMembers(List<String> onlineMembers){
        for (String member: onlineMembers ) {
            System.out.print("remove warning");
            //TODO : rajouter 1 à l'index du Pair
        }
    }

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

    public Map<String, Integer> getMembers() {
        return members;
    }

    public Integer getIndex(){
        return index;
    }

    public void addMember(String username){
        if (members.keySet().contains(username)) {
            return;
        }
        this.members.put(username, 0);
    }

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

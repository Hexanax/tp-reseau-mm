package domain;

import java.util.ArrayList;
import java.util.List;

public class Conversation {
    private static Integer index;

    private String conversationID;
    // For each user, we save the index of the last message they read
    private List<Pair<String, Integer>> members;
    private List<Message> messages;

    public Conversation(String conversationID, List<String> usernamesUsers){
        this.conversationID = conversationID;
        this.members = new ArrayList<>();
        this.messages = new ArrayList<>();
        index = 0;
        for (String username:usernamesUsers) {
            Pair <String, Integer> temp = new Pair(username,0);
            this.members.add(temp);
        }
    }


    public void addMessage(Message message){
        messages.add(message);
        index++;
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
        for (Pair<String, Integer> member : members ) {
            System.out.println(" > " + member.getKey());
        }
        System.out.println("======================");
        for (Message msg:messages) {
            System.out.println(msg);
        }
    }

    public List<Pair<String, Integer>> getMembers() {
        return members;
    }

    public Integer getIndex(){
        return index;
    }
    public void addMember(String username){
        this.members.add(new Pair<>(username, getIndex()));
    }

    public void removeMember(String username){
        this.members.removeIf(member -> member.getKey().equals(username));
    }

}

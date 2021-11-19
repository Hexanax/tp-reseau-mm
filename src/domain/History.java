package domain;

import java.util.List;

public class History {
    private static Integer index;

    private List<Pair<String,Integer>> usernamesUsers;
    private List<Message> messages;

    public History(List<String> usernamesUsers){
        this.usernamesUsers = usernamesUsers;
    }

    public void addMessage(Message message){
        messages.add(message);
    }

    public void showHistory(){
        for (Message msg:messages) {
            System.out.println(msg);
        }
    }
}

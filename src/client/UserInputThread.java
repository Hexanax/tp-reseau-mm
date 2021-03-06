package client;

import domain.Message;
import domain.SystemMessage;

import java.io.*;
import java.util.List;

/**
 * User input thread is a thread responsible for loading the inputs from the user and sending them to the server
 */
public class UserInputThread extends Thread{
    ObjectOutputStream socOut;
    BufferedReader stdIn = null;
    private boolean run = true;

    private String senderUserName;

    private String conversationID;

    UserInputThread(ObjectOutputStream socOut, List<String> userInfo) {
        this.senderUserName = userInfo.get(0);
        this.conversationID = userInfo.get(1);
        this.socOut = socOut;
    }
    UserInputThread(ObjectOutputStream socOut, String username) {
        this.senderUserName = username;
        this.conversationID = "";

        this.socOut = socOut;
    }

    /**
     * Run launches the user input thread
     */
    public void run(){
        String line;
        try {
            stdIn = new BufferedReader(new InputStreamReader(System.in));
            if(conversationID.equals("")) {
                System.out.println("You are not connected to a conversation.");
                System.out.println("To connect to a conversation, type /open <conversationID>.");
            }
            while(running()){
                line=stdIn.readLine();

                if(line.startsWith("/")){
                    if(line.equals("/quit")){
                        this.run = false;
                        continue;
                    }
                    if(line.equals("/help")){
                        System.out.println("new\t\tCreates a new conversation\nopen\tOpen an existing conversation\nadd\t\tAdd a member to the conversation\nquit\tQuit the client");
                    }
                    String[] cmdLine = line.split(" ");
                    if(cmdLine.length <=1) continue;
                    String cmd = cmdLine[0];
                    String complement = cmdLine[1];
                    String sysMessageContent = "";
                    switch (cmd){
                        case("/new")->{
                            sysMessageContent = senderUserName + ";" +complement;
                            System.out.println("New conversation name : " + sysMessageContent);
                            socOut.writeObject(SystemMessage.newConversationRequest(sysMessageContent));
                        }
                        case("/private")->{
                            sysMessageContent = senderUserName + ";" +complement;
                            System.out.println("New conversation with : " + sysMessageContent);
                            socOut.writeObject(SystemMessage.privateConversationRequest(sysMessageContent));
                        }
                        case("/open")->{
                            sysMessageContent = senderUserName + ";" +complement;
                            socOut.writeObject(SystemMessage.conversationConnectRequest(sysMessageContent));
                        }
                        case("/add")->{
                            if(conversationID.equals("")){
                                System.err.println("Cannot add a user when you are not in a conversation!");
                                continue;
                            }
                            sysMessageContent = senderUserName + ";"+conversationID+";" +complement;
                            socOut.writeObject(SystemMessage.conversationConnectRequest(sysMessageContent));
                        }
                        default -> {
                            System.out.println("Invalid command, type /help for all commands");
                        }
                    }
                }else {
                    socOut.writeObject(new Message(senderUserName, conversationID, line));
                }

            }
            stdIn.close();
            socOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setConversationID(String conversationID) {
        this.conversationID = conversationID;
    }

    boolean running() {
        return this.run;
    }
}

package client;

import domain.Message;
import domain.SystemMessage;

import java.io.*;
import java.net.Socket;
import java.util.List;

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

    public void run(){
        String line;
        try {
            stdIn = new BufferedReader(new InputStreamReader(System.in));
            if(conversationID.equals("")) {
                System.out.println("You are not connected to a conversation.");
                System.out.println("To connect to a conversation, type $open_conversation:<conversationID>.");
            }
            while(running()){
                line=stdIn.readLine();
                if (line.equals(".")){
                    this.run = false;
                }
                if(line.startsWith("$")){
                    String[] cmdLine = line.split(":");
                    if(cmdLine.length <=1) continue;
                    String cmd = cmdLine[0];
                    String complement = cmdLine[1];
                    String sysMessageContent = "";
                    switch (cmd){
                        case("$new_conversation")->{
                            sysMessageContent = senderUserName + ";" +complement;
                            System.out.println("new conversation to create : " + sysMessageContent);
                            socOut.writeObject(SystemMessage.newConversationRequest(sysMessageContent));
                        }
                        case("$open_conversation")->{
                            sysMessageContent = senderUserName + ";" +complement;
                            socOut.writeObject(SystemMessage.conversationConnectRequest(sysMessageContent));
                        }
                        case("$add_member")->{
                            sysMessageContent = senderUserName + ";"+conversationID+";" +complement;
                            socOut.writeObject(SystemMessage.conversationConnectRequest(sysMessageContent));
                        }
                        default -> {
                            //socOut.writeObject(new Message(senderUserName, conversationID, line));
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

package client;

import domain.Message;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class UserInputThread extends Thread{
    ObjectOutputStream socOut;
    BufferedReader stdIn = null;
    private boolean run = true;

    private String senderUserName;
    private String receiverUsername;

    UserInputThread(ObjectOutputStream socOut, List<String> userInfo) {
        this.senderUserName = userInfo.get(0);
        this.receiverUsername = userInfo.get(1);
        this.socOut = socOut;
    }

    public void run(){
        String line;
        try {
            stdIn = new BufferedReader(new InputStreamReader(System.in));
            while(running()){
                line=stdIn.readLine();
                if (line.equals(".")){
                    this.run = false;
                }
                socOut.writeObject(new Message(senderUserName, receiverUsername, line));
            }
            stdIn.close();
            socOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    boolean running() {
        return this.run;
    }
}

package client;

import domain.Message;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class UserInputThread extends Thread{
    private Socket clientSocket;
    ObjectOutputStream socOut = null;
    BufferedReader stdIn = null;
    private boolean run = true;

    private String senderUserName;
    private String receiverUsername;

    UserInputThread(Socket s, List<String> userInfo) {
        this.clientSocket = s;
        this.senderUserName = userInfo.get(0);
        this.receiverUsername = userInfo.get(1);
    }

    public void run(){
        String line;
        try {
            socOut= new ObjectOutputStream(this.clientSocket.getOutputStream());
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

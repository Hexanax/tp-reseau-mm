package client;

import domain.Message;

import java.io.*;
import java.net.Socket;

public class UserInputThread extends Thread{
    private Socket clientSocket;
    ObjectOutputStream socOut = null;
    BufferedReader stdIn = null;
    private boolean run = true;

    UserInputThread(Socket s) {
        this.clientSocket = s;
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
                socOut.writeObject(new Message(line));
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

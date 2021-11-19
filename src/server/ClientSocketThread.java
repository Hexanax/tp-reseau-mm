/***
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

package server;

import domain.Message;

import java.io.*;
import java.net.*;

public class ClientSocketThread
        extends Thread {

    private String userID; // classe user

    private Service service;
    private Socket clientSocket;
    private Message message;
    private boolean hasNewMessage;
    private ObjectOutputStream socOut;

    ClientSocketThread(Socket socket, Service service) {
        this.clientSocket = socket;
        hasNewMessage = false;
        this.service = service;
    }


    /**
     * receives a request from client then sends an echo to the client
     * @param clientSocket the client socket
     **/
    public void run() {
        try {
            ObjectInputStream socIn = null;
            socOut = new ObjectOutputStream(clientSocket.getOutputStream());
            socIn =  new ObjectInputStream(clientSocket.getInputStream());
            System.out.println("Successfully started Client Socket Listener");
            while (true) {
                Message receivedMessage = (Message) socIn.readObject();
                // hasNewMessage = !messageRead.isEmpty();
                System.out.println(receivedMessage);
                this.message = receivedMessage;
                if(hasNewMessage) socOut.writeObject(message);
            }
        } catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
        }
    }

    public Message getMessage() {
        Message actMessage = message;
        message = null;
        //System.out.println(temp);
        return actMessage;
    }

    public void sendMessage(Message messageSend) {
        try{
            socOut.writeObject(messageSend);
        }catch(Exception e ){
            System.err.println("Error sendMessage ClientThread");
        }
    }

    public String getUserID() {
        return userID;
    }

    public boolean hasNewMessage() {
        return hasNewMessage;
    }
}



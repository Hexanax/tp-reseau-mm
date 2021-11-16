/***
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

package stream;

import java.io.*;
import java.net.*;

public class ClientThread
        extends Thread {

    private String userID; // classe user


    private Socket clientSocket;
    private Message message;
    private boolean hasNewMessage;
    private ObjectOutputStream socOut;

    ClientThread(Socket s) {
        this.clientSocket = s;
        hasNewMessage = false;
    }


    /**
     * receives a request from client then sends an echo to the client
     * @param clientSocket the client socket
     **/
    public void run() {
        try {
            ObjectInputStream socIn = null;
            socIn = new ObjectInputStream(clientSocket.getInputStream());
            socOut = new ObjectOutputStream(clientSocket.getOutputStream());
            while (true) {
                Message messageRead = (Message) socIn.readObject();
                // hasNewMessage = !messageRead.isEmpty();
                message = messageRead;
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



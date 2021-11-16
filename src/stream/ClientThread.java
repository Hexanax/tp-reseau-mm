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
    private PrintStream socOut;

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
            BufferedReader socIn = null;
            socIn = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            socOut = new PrintStream(clientSocket.getOutputStream());
            while (true) {
                String line = socIn.readLine();
                hasNewMessage = line.isEmpty() ?false  : true;
                socOut.println(line);

            }
        } catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
        }
    }

    public Message getMessage() {
        //String temp = message;
        //message = "";
        //System.out.println(temp);
        return null;
    }

    public void sendMessage(String message){
        socOut.println(message);
    }

    public String getUserID() {
        return userID;
    }

    public boolean hasNewMessage() {
        return hasNewMessage;
    }
}



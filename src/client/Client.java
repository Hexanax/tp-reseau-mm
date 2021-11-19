/***
 * EchoClient
 * Example of a TCP client 
 * Date: 10/01/04
 * Authors:
 */
package client;

import domain.Message;
import server.ClientSocketThread;

import java.io.*;
import java.net.*;



public class Client {

 
  /**
  *  main method
  *  accepts a connection, receives a message from client then sends an echo to the client
  **/
    public static void main(String[] args) throws IOException {

        Socket clientSocket = null;
        BufferedReader stdIn = null;
        ObjectInputStream socIn = null;

        if (args.length != 2) {
          System.out.println("Usage: java EchoClient <EchoServer host> <EchoServer port>");
          System.exit(1);
        }
        UserInputThread userInputThread = null;
        try {
      	    // creation socket ==> connexion
      	    clientSocket = new Socket(args[0],new Integer(args[1]).intValue());
            socIn = new ObjectInputStream(clientSocket.getInputStream());

            // Start the thread that will be handling user input
            userInputThread = new UserInputThread(clientSocket);
            userInputThread.start();
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host:" + args[0]);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                               + "the connection to:"+ args[0]);
            System.exit(1);
        }
                             
        String line;
        boolean run = true;
        Message incomingMessage = null;
        while (run) {
            try {
                incomingMessage = (Message) socIn.readObject();
                if (incomingMessage != null) {
                    System.out.println("Your Dear Friend: " + incomingMessage.getMessage());
                }
            if(!userInputThread.running()){
                run = false;
            }
            } catch (Exception e){
                System.err.println("Error in EchoClient: "+ e);
            }
        }
      stdIn.close();
      clientSocket.close();
    }
}



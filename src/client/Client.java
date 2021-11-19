/***
 * Client
 * Example of a TCP client 
 * Date: 10/01/04
 * Authors:
 */
package client;

import domain.Message;
import domain.SystemMessage;
import server.ClientSocketThread;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.List;


public class Client {

  /**
  *  main method
  *  accepts a connection, receives a message from client then sends an echo to the client
  **/
    public static void main(String[] args) throws IOException {

        BufferedReader stdIn = null;
        Socket clientSocket = null;
        ObjectInputStream socIn = null;

        if (args.length != 2) {
          System.out.println("Usage: java Client <Server host> <Server port>");
          System.exit(1);
        }
        UserInputThread userInputThread = null;
        ObjectOutputStream socOut;

        try {

            // creation socket ==> connexion
            clientSocket = new Socket(args[0], Integer.parseInt(args[1]));
            socOut= new ObjectOutputStream(clientSocket.getOutputStream());
            List<String> userInfo = login(stdIn, socOut);
            socIn = new ObjectInputStream(clientSocket.getInputStream());

            // Start the thread that will be handling user input
            userInputThread = new UserInputThread(socOut, userInfo);
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
        Message incomingMessage;
        while (run) {
            try {
                incomingMessage = (Message) socIn.readObject();
                if (incomingMessage != null) {
                    System.out.println(incomingMessage);
                }
            if(!userInputThread.running()){
                run = false;
            }
            } catch (Exception e){
                System.err.println("Error in EchoClient: "+ e);
            }
        }

      clientSocket.close();
    }

    private static List<String> login(BufferedReader stdIn, ObjectOutputStream socOut) throws IOException {
        stdIn = new BufferedReader(new InputStreamReader(System.in));
        String senderUserName = "";
        String receiverUsername = "";
        try{
            System.out.print("username: ");
            senderUserName = stdIn.readLine();
            socOut.writeObject(SystemMessage.newLoginRequest(senderUserName));

            System.out.print("conversation ID: ");
            receiverUsername = stdIn.readLine();
        } catch (IOException e){
            e.printStackTrace();
        }
        return Arrays.asList(senderUserName, receiverUsername);
    }
}



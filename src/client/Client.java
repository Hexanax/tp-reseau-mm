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

import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;



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
            clientSocket = initiateSocket(args, 15);
            socOut= new ObjectOutputStream(clientSocket.getOutputStream());
            List<String> userInfo = new ArrayList<>();
            //userInfo.add();
            socIn = new ObjectInputStream(clientSocket.getInputStream());

            // Start the thread that will be handling user input
            userInputThread = new UserInputThread(socOut, login(stdIn, socOut));

            userInputThread.start();
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host:" + args[0]);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                               + "the connection to:"+ args[0]);
            System.exit(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String line;
        boolean run = true;
        Object incomingMessage;
        while (run) {
            try {

                incomingMessage = socIn.readObject();
                if (incomingMessage == null) {
                    continue;
                }else if(incomingMessage instanceof Message){
                    System.out.println(incomingMessage);
                }else if(incomingMessage instanceof SystemMessage){
                    handleSystemMessage((SystemMessage) incomingMessage, userInputThread);

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

    private static Socket initiateSocket(String[] args, int maxConnectionAttpemts) throws InterruptedException {
        String connectionMessage= "Connecting to server";
        System.out.print(connectionMessage);
        String[] loading = {".  ", ".. ", "..."};
        int i = 0;
        Socket socket = null;
        while (socket == null && maxConnectionAttpemts > 0){
            try{
                System.out.print(loading[i]);
                socket = new Socket(args[0],new Integer(args[1]).intValue());
            } catch (Exception e)
            {
                i = (i+1) % 3;
                TimeUnit.MILLISECONDS.sleep(100);
                System.out.print("\b\b\b");
                maxConnectionAttpemts--;
            }
        }
        for (i = 0; i < connectionMessage.length() + 3; i++) {
            System.out.print("\b");
        }
        if(maxConnectionAttpemts == 0){
            System.out.println("Failed to connect to server, please try again later.");
        }
        return socket;
    }


    private static String login(BufferedReader stdIn, ObjectOutputStream socOut) throws IOException {
        stdIn = new BufferedReader(new InputStreamReader(System.in));
        String senderUserName = "";

        try{
            System.out.print("username: ");
            senderUserName = stdIn.readLine();
            socOut.writeObject(SystemMessage.newLoginRequest(senderUserName));
        } catch (IOException e){
            e.printStackTrace();
        }
        return senderUserName;
    }

    private static String conversationConnexion(ObjectOutputStream socOut) throws IOException{
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String conversationID = "";
        String details = "";
        try{
            System.out.println("Conversation ID : ");
            conversationID = stdIn.readLine();

            System.out.println("usernames to add on this format: ");
            details = conversationID + ";" + stdIn.readLine();
            socOut.writeObject(SystemMessage.newConversationRequest(details));
        }catch(IOException e){
            System.err.println("Error in Client : " + e);
        }
        return conversationID;
    }

    private static void handleSystemMessage(SystemMessage systemMessage, UserInputThread userInputThread){
        switch(systemMessage.type) {
            case CONVERSATION_CONNECT_OK -> {

                String conversationID = systemMessage.content.split(";")[1];
                userInputThread.setConversationID(conversationID);
                System.out.println("Connected to " + conversationID);

            }
        }
    }

}


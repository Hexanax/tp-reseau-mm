/***
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

package server;

import domain.Conversation;
import domain.Message;
import domain.SystemMessage;

import java.io.*;
import java.net.*;
import java.util.Set;

import static domain.SystemMessageType.LOGIN_REQUEST;

public class ClientSocketThread
        extends Thread {

    private String username;
    private Set<Conversation> conversationSet;

    private final Service service;
    private Socket clientSocket;
    private ObjectOutputStream socOut;

    ClientSocketThread(Socket socket, Service service) {
        this.clientSocket = socket;
        this.service = service;
    }


    /**
     * receives a request from client then sends an echo to the client
     * @class ClientSocketThread the client socket
     **/
    public void run() {
        try {
            ObjectInputStream socIn;
            socOut = new ObjectOutputStream(clientSocket.getOutputStream());
            socIn =  new ObjectInputStream(clientSocket.getInputStream());
            System.out.println("Successfully started Client Socket Listener");
            while (true) {

                Object receivedMessage = socIn.readObject();
                if (receivedMessage == null){
                    continue;
                } else if (receivedMessage instanceof Message){
                    if (username != null) {
                        System.out.println("Message to be sent : " + receivedMessage );
                        service.sendMessageToOnlineClients((Message) receivedMessage);
                    }
                } else if (receivedMessage instanceof SystemMessage){
                    if (LOGIN_REQUEST.equals(((SystemMessage) receivedMessage).type)) {
                        username = ((SystemMessage)receivedMessage).content;
                    }
                    service.handleSystemMessage(( SystemMessage ) receivedMessage);
                } else {
                    continue;
                }

                System.out.println(receivedMessage);
            }
        } catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
        }
    }

    public void sendMessage(Message messageSend) {
        try{
            socOut.writeObject(messageSend);
        }catch(Exception e ){
            System.err.println("Error sendMessage ClientThread");
        }
    }
    public void showConversations(String conversations) {
        try{
            socOut.writeObject(conversations);
        }catch(Exception e ){
            System.err.println("Error sendMessage ClientThread");
        }
    }
    public void sendSystemMessage(SystemMessage systemMessage) {
        try{
            socOut.writeObject(systemMessage);
        }catch(Exception e ){
            System.err.println("Error sendSystemMessage ClientThread");
        }
    }



    public String getUsername() {
        return username;
    }
}



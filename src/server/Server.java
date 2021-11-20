/***
 * EchoServer
 * Example of a TCP server
 * Date: 10/01/04
 * Authors:
 */

package server;

import domain.Message;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {

    public static void main(String args[]){
        ServerSocket listenSocket;
        Service service = new Service();


        // TODO : Get all the conversations from database
        // TODO : Instantiate the conversations set of the service
        if (args.length != 1) {
            System.out.println("Usage: java Server <Server port>");
            System.exit(1);
        }
        try {
            listenSocket = new ServerSocket(Integer.parseInt(args[0])); //port
            System.out.println("Server ready...");

            while (!listenSocket.isClosed()) {

                Socket clientSocket = listenSocket.accept();
                System.out.println("Connexion from:" + clientSocket.getInetAddress());
                ClientSocketThread ct = new ClientSocketThread(clientSocket, service);
                ct.start();
                service.addClientSocketThread(ct);
            }
        } catch (Exception e) {
            System.err.println("Error in Server:" + e);
        }

    }

}



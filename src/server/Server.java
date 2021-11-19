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


    static void doService(Socket clientSocket) {
        try {
            BufferedReader socIn;
            socIn = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            PrintStream socOut = new PrintStream(clientSocket.getOutputStream());
            if (true) {
                String line = socIn.readLine();
                socOut.println(line);

            }
        } catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
        }
    }

    public static void main(String args[]){
        ServerSocket listenSocket;
        Service service = new Service();
        List<ClientSocketThread> clientSocketThreadList = new ArrayList<>();

        // TODO : Get all the conversations
        // TODO : Instantiate the conversations set of the service
        if (args.length != 1) {
            System.out.println("Usage: java Server <Server port>");
            System.exit(1);
        }
        try {
            listenSocket = new ServerSocket(Integer.parseInt(args[0])); //port
            System.out.println("Server ready...");
            while (true) {
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



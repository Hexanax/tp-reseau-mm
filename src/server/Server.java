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
            BufferedReader socIn = null;
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
    /**
     * main method
     * @param EchoServer port
     *
     **/
    public static void main(String args[]){
        ServerSocket listenSocket;
        Service service = new Service();
        List<Socket> socketList = new ArrayList<>();
        Map<String, ClientSocketThread> clientThreadMap = new HashMap<>();
        List<ClientSocketThread> clientSocketThreadList = new ArrayList<>();

        if (args.length != 1) {
            System.out.println("Usage: java EchoServer <EchoServer port>");
            System.exit(1);
        }
        try {
            listenSocket = new ServerSocket(Integer.parseInt(args[0])); //port
            System.out.println("Server ready...");
            int i =0;
            while (i<2) {
                Socket clientSocket = listenSocket.accept();
                System.out.println("Connexion from:" + clientSocket.getInetAddress());
                ClientSocketThread ct = new ClientSocketThread(clientSocket, service);
                ct.start();
                clientSocketThreadList.add(ct);
                i++;
            }
            int index = 0;
            while(true){

                for (int j = 0; j < clientSocketThreadList.size(); j++) {
                    Message message = clientSocketThreadList.get(j).getMessage();

                }

            }
        } catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
        }
    }
}



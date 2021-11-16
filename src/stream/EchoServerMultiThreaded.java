/***
 * EchoServer
 * Example of a TCP server
 * Date: 10/01/04
 * Authors:
 */

package stream;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EchoServerMultiThreaded  {


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
        List<Socket> socketList = new ArrayList<>();
        Map<String, ClientThread> clientThreadMap = new HashMap<>();
        List<ClientThread> clientThreadList = new ArrayList<>();

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
                ClientThread ct = new ClientThread(clientSocket);
                ct.start();
                clientThreadMap.put(ct.getUserID(), ct);
                i++;
            }
            int index = 0;
            while(true){

                for (int j = 0; j < clientThreadList.size(); j++) {
                    Message message = clientThreadList.get(j).getMessage();
                    if(clientThreadList.get(j).hasNewMessage()){
                        System.out.println(message);
                        // Send message to all client
                        for (int k = 0; k < clientThreadList.size(); k++) {
                            if(k == j) {
                                continue;
                            }
                            clientThreadList.get(k).sendMessage(message);
                        }
                    }

                }

            }
        } catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
        }
    }
}



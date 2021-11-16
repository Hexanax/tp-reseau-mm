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
        List<ClientThread> clientThreadList = new ArrayList<>();
        Map<String, ClientThread> clientThreadMap = new HashMap<>();

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
                for (ClientThread ct:
                        clientThreadMap.values()) {
                    Message message = ct.getMessage();
                    if(ct.hasNewMessage()){
                        System.out.println(message);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
        }
    }
}



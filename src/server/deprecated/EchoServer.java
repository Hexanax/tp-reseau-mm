/***
 * EchoServer
 * Example of a TCP server
 * Date: 10/01/04
 * Authors:
 */

package server.deprecated;

import java.io.*;
import java.net.*;

public class EchoServer  {

 	/**
  	* receives a request from client then sends an echo to the client
  	* @param clientSocket the client socket
  	**/
	static void doService(Socket clientSocket) {
    	  try {
    		BufferedReader socIn = null;
    		socIn = new BufferedReader(
    			new InputStreamReader(clientSocket.getInputStream()));    
    		PrintStream socOut = new PrintStream(clientSocket.getOutputStream());
    		if (true) {
    		  String line = socIn.readLine();
    		  socOut.println(line);
			  System.out.println(" > message form client " + line);
    		}
    	} catch (Exception e) {
        	System.err.println("Error in EchoServer:" + e); 
        }
       }

       public static void main(String args[]){ 
        ServerSocket listenSocket;
        
  	if (args.length != 1) {
          System.out.println("Usage: java EchoServer <EchoServer port>");
          System.exit(1);
  	}
	try {
		listenSocket = new ServerSocket(Integer.parseInt(args[0])); //port
		while (true) {
			Socket clientSocket = listenSocket.accept();
			System.out.println("connexion from:" + clientSocket.getInetAddress());
			doService(clientSocket);
		}
        } catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
        }
      }
  }

  

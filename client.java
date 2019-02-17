//Cameron Cook

import java.io.*;
import java.net.*;
 
public class Client {

    static Socket echoSocket;
    static PrintWriter out;
    static BufferedReader in;
    static BufferedReader stdIn;

    public static void setUpClient(String hostName, int portNumber) {
        try {
            echoSocket = new Socket(hostName, portNumber);
            out = new PrintWriter(echoSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            stdIn = new BufferedReader(new InputStreamReader(System.in));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        } 
    }
    
    public static void main(String[] args) throws IOException {
         
        if (args.length != 2) {
            System.err.println("Usage: java Client <host name> <port number>");
            System.exit(1);
        }
 
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
 
        setUpClient(hostName, portNumber);
        String userInput;
        while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
                // System.out.println("Client received: " + in.readLine());

                // Need to take messages from server
                // if (in.readLine() != null){
                //     System.out.println(in.readLine());
                // }
        }

    }
}
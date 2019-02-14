/*Written by Cameron Cook
*Some code was taken from https://docs.oracle.com/javase/tutorial/networking/sockets/
*/

import java.net.*;
import java.io.*;
 
public class Server {
    public static void main(String[] args) throws IOException {
         
        if (args.length != 1) {
            System.err.println("Usage: java EchoServer <port number>");
            System.exit(1);
        }
         
        int portNumber = Integer.parseInt(args[0]);

        while(true){
            try (
                //Set up server socket
                ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
                Socket clientSocket = serverSocket.accept();     
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);                   
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                //Set up file writer
                FileWriter fileWriter = new FileWriter("qbank.5"); //should close later



            ) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    
                    fileWriter.write(inputLine);

                    out.println(inputLine);
                }
            } catch (IOException e) {
                System.out.println("Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
                System.out.println(e.getMessage());
            }
        }

    }
}
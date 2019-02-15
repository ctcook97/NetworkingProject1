/*Written by Cameron Cook
*Some code was taken from https://docs.oracle.com/javase/tutorial/networking/sockets/
*/

import java.net.*;
import java.io.*;
import java.nio.file.*;

public class Server {

    public static void main(String[] args) throws IOException {
         
        if (args.length != 1) {
            System.err.println("Usage: java Server <port number>");
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
            ) {
                //Read lines from client
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    
                    writeQuestion(inputLine);

                    out.println(inputLine);
                }
            } catch (IOException e) {
                System.out.println("Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
                System.out.println(e.getMessage());
            }
        }

    }

    //Write question to a file
    public static void writeQuestion(String question){
        try {
            question += "\n";
            Files.write(Paths.get("qbank.5"), question.getBytes(), StandardOpenOption.APPEND);
        }
        catch (IOException e) {
            System.out.println("Could not write to question bank");
            System.out.println(e.getMessage());
        }
    }

    //Read for a file
    public static String readQuestion(){
        
    }

}
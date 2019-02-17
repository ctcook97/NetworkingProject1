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

    public static void putMode(){
        
        try {
            System.out.println("putMode");
            out.println("p");
            out.println(stdIn.readLine()); //Question tag
            boolean cont = true;
            while(cont){ //Question and answers
                String s;
                cont = false;
                while((s = stdIn.readLine()) != null) {
                    out.println(s);
                    if (s.equals(".")) { 
                        break;
                    }
                    else {
                        cont = true;                        
                    }
                }
            }
            out.println(stdIn.readLine()); //Correct answer
            System.out.println(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }




    }

    public static void deleteMode(String input){
        try {
            System.out.println("deleteMode");
            out.println(input);
            System.out.println(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getMode(){
        out.println("g");
        System.out.println("getMode");
    }

    public static void randomMode(){
        out.println("r");
        System.out.println("randomMode");
    }

    public static void checkMode(){
        out.println("c");
        System.out.println("checkMode");
    }

    public static void killMode(){
        out.println("k");
        System.out.println("killMode");
    }

    public static void quitMode() {
        System.out.println("quitMode");
    }

    public static void helpMode() {
        System.out.println("helpMode");
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
            switch(userInput.charAt(0)){
                case 'p':
                    putMode();
                    break;
                case 'd':
                    deleteMode(userInput);
                    break;
                case 'g':
                    getMode();
                    break;
                case 'r':
                    randomMode();
                    break;
                case 'c':
                    checkMode();
                    break;
                case 'k':
                    killMode();
                    break;
                case 'q':
                    quitMode();
                    break;
                case 'h':
                    helpMode();
                    break;
                default:
                    System.out.println("Command not recognized. Please type 'h' for help.");
                    break;
            }

                //out.println(userInput);
        }

    }
}
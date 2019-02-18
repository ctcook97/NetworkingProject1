//Cameron Cook

import java.io.*;
import java.net.*;
 
public class Client {

    static Socket clientSocket;
    static PrintWriter out;
    static BufferedReader in;
    static BufferedReader stdIn;

    public static void setUpClient(String hostName, int portNumber) {
        try {
            clientSocket = new Socket(hostName, portNumber);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
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
            out.println(input);
            System.out.println(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getMode(String input){
        out.println(input);
        try {
            String s;
            while((s = in.readLine()) != null){
                if(s.equals(".")) {
                    break;
                }
                System.out.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void randomMode(){
        out.println("r");
        try {
            String s;
            boolean questionsFound = true;
            while((s = in.readLine()) != null){
                if(s.equals(".")) {
                    break;
                }
                System.out.println(s);
                if (s.equals("No questions we found in the bank.")){ //This will not work properly if you put this as a question or answer choice
                    questionsFound = false;
                }
            }
            if (questionsFound) {
                out.println(stdIn.readLine());
                System.out.println(in.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void checkMode(String inputLine){
        try {
            out.println(inputLine);
            System.out.println(in.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void helpMode() {
        System.out.println("Help mode");
        System.out.println("Commands: ");
        System.out.println("Please pay careful attention to the formatting. Incorrect formatting could cause issues.");
        System.out.println("Commands must be lowercase.");
        System.out.println("'p': put question");
        System.out.println("    Adds a qustion to the bank");
        System.out.println("    The next line you enter will be the question tags");
        System.out.println("    Then the next lines you enter will be the question text, until you put a '.' on its own line");
        System.out.println("    Then enter the answers one by one, starting each with their letter and ending each with a '.' on its own line");
        System.out.println("    Enter another '.' on its own line when you are done adding answers");
        System.out.println("    The next line you enter will be the correct answer");
        System.out.println("    The assigned question number will get printed");
        System.out.println("'d <number>': delete question");
        System.out.println("    Question <number> will get deleted.");
        System.out.println("    Please only use one space and do not include the brackets");
        System.out.println("'g <number>': get question");
        System.out.println("    Question <number> and its answers will get printed.");
        System.out.println("'r': random question");
        System.out.println("    Prints a random question.");
        System.out.println("    Enter your answer on the next line and it will tell you if you are right or not");
        System.out.println("'c <number> <answer>': check question");
        System.out.println("    Will check if the answer to question <number> is <answer>");
        System.out.println("'k': Kill Server");
        System.out.println("    This will also shut down the client");
        System.out.println("'q': Quit");
        System.out.println("    This will shut down this program");
        System.out.println("'h': help");
        System.out.println("    This will print out the commands");
    }
    
    public static void main(String[] args) throws IOException {
         
        if (args.length != 2) {
            System.err.println("Usage: ./qserver <host name> <port number>");
            System.out.println("If running on same computer as server, just use 'localhost' as hostname");
            System.exit(1);
        }
 
        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);
 
        setUpClient(hostName, portNumber);
        String userInput;
        while ((userInput = stdIn.readLine()) != null) {
            if(userInput.length() == 0){
                continue;
            }
            if(userInput.length() > 1 && userInput.charAt(1) != (' ')){
                System.out.println("Invalid command");
                continue;
            }
            switch(userInput.charAt(0)){
                case 'p':
                    putMode();
                    break;
                case 'd':
                    deleteMode(userInput);
                    break;
                case 'g':
                    getMode(userInput);
                    break;
                case 'r':
                    randomMode();
                    break;
                case 'c':
                    checkMode(userInput);
                    break;
                case 'k':
                    out.println("k");
                    System.out.println("Server killed.");
                    return;
                case 'q':
                    System.out.println("Shutting Down");
                    return;
                case 'h':
                    helpMode();
                    break;
                default:
                    System.out.println("Command not recognized. Please type 'h' for help.");
                    break;
            }
        }

    }
}
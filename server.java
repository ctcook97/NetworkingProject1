//Cameron Cook

import java.net.*;
import java.io.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.Iterator;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Server {

    static JSONArray questions = new JSONArray();

    //Write question to a file
    @SuppressWarnings("unchecked")
    public static void writeQuestion(String input){
        
        JSONObject question = new JSONObject();
        question.put("number", 21);
        question.put("tag", "presidents, US history");
        question.put("text", "Which is the first president of the USA");
        question.put("answer", "c");

        JSONArray list = new JSONArray();
        list.add("(a) Thomas Jefferson");
        list.add("(b) Abraham Lincoln");
        list.add("(c) George Washington");
        list.add("(d) Benjamin Franklin");
        question.put("answers", list);

        questions.add(question);

        //Adding to test with multiple questions
        JSONObject question2 = new JSONObject();
        question2.put("number", 24);
        question2.put("tag", "presidents, US future");
        question2.put("text", "Who will be the next president of the United States");
        question2.put("answer", "a");
        JSONArray list2 = new JSONArray();
        list2.add("(a) Cameron Cook");
        list2.add("(b) Donald Trump");
        list2.add("(c) Kamala Harris");
        list2.add("(d) Ben Franklin");
        question2.put("answers", list2);
        questions.add(question2);
        //End of multiple questions test

        try (FileWriter file = new FileWriter("qbank.json")) {
            file.write(questions.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Read for a file
    @SuppressWarnings("unchecked")
    public static void printQuestion(int n){
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader("qbank.json"));
            JSONObject jsonObject = (JSONObject) obj;

            long number = (long) jsonObject.get("number");
            System.out.println("number: " + number);

            String question = (String) jsonObject.get("text");
            System.out.println(question);

            JSONArray answers = (JSONArray) jsonObject.get("answers");
            Iterator<String> iterator = answers.iterator();
            while (iterator.hasNext()) {
                System.out.println(iterator.next());
            }
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

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
                    //printQuestion(0);

                    out.println(inputLine);
                }
            } catch (IOException e) {
                System.out.println("Exception caught when trying to listen on port " + portNumber + " or listening for a connection");
                System.out.println(e.getMessage());
            }
        }

    }

}
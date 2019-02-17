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

    static ServerSocket serverSocket;
    static Socket clientSocket;     
    static PrintWriter out;                   
    static BufferedReader in;

    public static void setUpServer(int port){
        try {
            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();     
            out = new PrintWriter(clientSocket.getOutputStream(), true);                   
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port " + port + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }

    //To be ran on start, loads the questions from file
    public static void loadQuestions(){
        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(new FileReader("qbank.json"));
            questions = (JSONArray) obj;
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static void writeQuestion(){

        try {
            JSONObject newQuestion = new JSONObject();
            String s = in.readLine();
            newQuestion.put("tag", s);
            String questionText = "";
            while ((s = in.readLine()) != null) {
                if (s.equals(".")){
                    break;
                }  
                questionText = questionText + s + "\n";
            }
            questionText = questionText.substring(0, questionText.length()-1);
            newQuestion.put("text", questionText); 
            JSONArray answers = new JSONArray();
            String answerText = "placeholder";
            while(! answerText.equals("")){
                answerText = "";
                while((s = in.readLine()) != null) {
                    if (s.equals(".")) { 
                        break;
                    }
                    else {
                        answerText = answerText + s + "\n";
                    }
                }
                if(! answerText.equals("")){
                    answerText = answerText.substring(0,answerText.length()-1);
                    answers.add(answerText);
                }
            }
            newQuestion.put("answers", answers);
            String answer = in.readLine();
            newQuestion.put("answer", answer);
            newQuestion.put("number", 12);
            if (questions.size() == 0){
                newQuestion.put("number", 1);
                out.println(1);
            }
            else {
                JSONObject obj = (JSONObject) questions.get(questions.size()-1);
                int num = Integer.parseInt(obj.get("number").toString()) + 1; 
                newQuestion.put("number", num);
                out.println(num);
            }
            questions.add(newQuestion);

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        //Write to file
        try (FileWriter file = new FileWriter("qbank.json")) {
            file.write(questions.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Need to switch to print on client side
    @SuppressWarnings("unchecked")
    public static void getRandomQuestion(){

        int num = (int) (Math.random()*questions.size());
        JSONObject obj = (JSONObject) questions.get(num);
        System.out.println((String) obj.get("text"));
        JSONArray answers = (JSONArray) obj.get("answers");
        Iterator<String> iterator = answers.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    //Need to switch to print on client side
    public static void deleteQuestion(int n){
        int index = -1;
        for(int i = 0; i < questions.size(); ++i) {
            JSONObject obj = (JSONObject) questions.get(i);
            if(Integer.parseInt(obj.get("number").toString()) == n){
                index = i;
                break;
            }
        }
        if (index > -1) {
            questions.remove(index);
        }
        else {
            System.out.println("Error: question " + n + " not found");
        }

        //Write to file
        try (FileWriter file = new FileWriter("qbank.json")) {
            file.write(questions.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void checkQuestion(String s){
        int index = s.indexOf(" ");
        int num = Integer.parseInt(s.substring(0,index));
        s = s.substring(index+1);
        index = -1;
        for(int i = 0; i < questions.size(); ++i) {
            JSONObject obj = (JSONObject) questions.get(i);
            if(Integer.parseInt(obj.get("number").toString()) == num){
                index = i;
                break;
            }
        }
        if (index > -1) {
            JSONObject obj = (JSONObject) questions.get(index);
            String answer = (String) obj.get("answer");
            if (s.equals(answer)){
                System.out.println("Correct");
            }
            else {
                System.out.println("Incorrect");
            }
        }
        else {
            System.out.println("Error: question " + num + " not found");
        }
    }

    //get question is pretty much this (taken from random question) but printed to client
        // boolean questionFound = false;
        // for(int i = 0; i < questions.size(); ++i){
        //     JSONObject obj = (JSONObject) questions.get(i);
        //     if( Integer.parseInt(obj.get("number").toString()) == n){
        //         questionFound = true;
        //         System.out.println((String) jsonObject.get("text"));
        //         JSONArray answers = (JSONArray) obj.get("answers");
        //         Iterator<String> iterator = obj.iterator();
        //         while (iterator.hasNext()) {
        //             System.out.println(iterator.next());
        //         }
        //     }  
        // }
        // if (! questionFound){
        //     System.out.println("Question " + n + " was not found");
        // }


    //TO IMPLEMENT
    //help option
    //close server
    

    public static void main(String[] args) throws IOException { //server currently stops if client does
         
        if (args.length != 1) {
            System.err.println("Usage: java Server <port number>");
            System.exit(1);
        }
         
        int portNumber = Integer.parseInt(args[0]);
        //loadQuestions(); - currently an error if there are no questions
        setUpServer(portNumber);
        // writeQuestion();
        // checkQuestion("1 a");
        // checkQuestion("1 b");
        // checkQuestion("2 c");
        // writeQuestion();
        // deleteQuestion(1);


        String inputLine;
        while ((inputLine = in.readLine()) != null) { //quits because this becomes false when while loop is executed. Whole thing needs to be wrapped in while loop
            switch(inputLine.charAt(0)) {
                case 'p':
                    System.out.println("put mode");
                    writeQuestion();
                    break;
                case 'd':
                    System.out.println("delete mode");
                    deleteQuestion(0); //change to actual int
                    break;
                case 'g':
                    System.out.println("get mode");
                    //getQuestion();
                    break;
                case 'r':
                    System.out.println("get random mode");
                    getRandomQuestion();
                    break;
                case 'c':
                    checkQuestion(inputLine);
                    break;
                case 'k':
                    //shutDownServer();
                    break;
                default:
                    System.out.println("An unrecognized command was passed to the server");
                    //shutDownServer();
                    break;
            }
            
            //getRandomQuestion();
        }

        out.println("Question was written");
        

    }

}


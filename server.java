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
        try {
            if (questions.size() == 0){
                out.println("No questions we found in the bank.");
                out.println(".");
                return;
            }
            int num = (int) (Math.random()*questions.size());
            JSONObject obj = (JSONObject) questions.get(num);
            out.println((String) obj.get("text"));
            JSONArray answers = (JSONArray) obj.get("answers");
            Iterator<String> iterator = answers.iterator();
            while (iterator.hasNext()) {
                out.println(iterator.next());
            }
            out.println(".");
            String response = in.readLine();
            if (response.equals(obj.get("answer"))){
                out.println("Correct");
            }
            else {
                out.println("Incorrect");
            }
        } catch (IOException e) {
            e.printStackTrace();
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
            out.println("Deleted question " + n);
        }
        else {
            out.println("Error: question " + n + " not found");
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
        s = s.substring(2);
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
                out.println("Correct");
            }
            else {
                out.println("Incorrect");
            }
        }
        else {
            out.println("Error: question " + num + " not found");
        }
    }

    @SuppressWarnings("unchecked")
    public static void getQuestion(int num){
        boolean questionFound = false;
        for(int i = 0; i < questions.size(); ++i){
            JSONObject obj = (JSONObject) questions.get(i);
            if (Integer.parseInt(obj.get("number").toString()) == num) {
                questionFound = true;
                out.println((String) obj.get("text"));
                JSONArray answers = (JSONArray) obj.get("answers");
                Iterator<String> iterator = answers.iterator();
                while (iterator.hasNext()) {
                    out.println(iterator.next());
                }
                out.println("."); //To signal to the client to stop listening
            }
        }
        if (! questionFound){
            out.println("Question " + num + " was not found");
            out.println(".");
        }
    }

    public static void main(String[] args) throws IOException { //server currently stops if client does

        try {
            serverSocket = new ServerSocket(0);
        }
        catch (BindException e) {
            e.printStackTrace();
        }
        System.out.println("Server running on port " + serverSocket.getLocalPort());
        loadQuestions();

        while(true) {
            try {  
                clientSocket = serverSocket.accept();     
                out = new PrintWriter(clientSocket.getOutputStream(), true);                   
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            String inputLine;
            while ((inputLine = in.readLine()) != null) { 
                switch(inputLine.charAt(0)) {
                    case 'p':
                        writeQuestion();
                        break;
                    case 'd':
                        deleteQuestion(Integer.parseInt(inputLine.substring(2)));
                        break;
                    case 'g':
                        getQuestion(Integer.parseInt(inputLine.substring(2)));
                        break;
                    case 'r':
                        getRandomQuestion();
                        break;
                    case 'c':
                        checkQuestion(inputLine);
                        break;
                    case 'k':
                        System.out.println("Shutting down");
                        return;
                    default:
                        System.out.println("An unrecognized command was passed to the server"); //for logging purposes
                        return;
                }
            }    
        }    

    }

}


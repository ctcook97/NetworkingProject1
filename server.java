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

    //Write question to a file
    @SuppressWarnings("unchecked")
    public static void writeQuestion(){
        
        // //Add example question
        // JSONObject question = new JSONObject();
        // question.put("number", 21);
        // question.put("tag", "presidents, US history");
        // question.put("text", "Which is the first president of the USA");
        // question.put("answer", "c");
        // JSONArray list = new JSONArray();
        // list.add("(a) Thomas Jefferson");
        // list.add("(b) Abraham Lincoln");
        // list.add("(c) George Washington");
        // list.add("(d) Benjamin Franklin");
        // question.put("answers", list);
        // questions.add(question);
        // //End example question

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
            newQuestion.put("text", questionText); //take off trailing new line

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

            newQuestion.put("number", 12); //Assign in a minute

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


    @SuppressWarnings("unchecked")
    public static void getRandomQuestion(){
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
        int num = (int) (Math.random()*questions.size());
        JSONObject obj = (JSONObject) questions.get(num);
        System.out.println((String) obj.get("text"));
        JSONArray answers = (JSONArray) obj.get("answers");
        Iterator<String> iterator = answers.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
    }

    public static void main(String[] args) throws IOException {
         
        if (args.length != 1) {
            System.err.println("Usage: java Server <port number>");
            System.exit(1);
        }
         
        int portNumber = Integer.parseInt(args[0]);
        //loadQuestions(); - currently an error if there are no questions
        setUpServer(portNumber);



        writeQuestion();

        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            getRandomQuestion();
        }

        out.println("Question was written");
        

    }

}


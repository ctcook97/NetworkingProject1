import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class Test {

    public static void main(String[] args) {

        JSONObject obj = new JSONObject();
        obj.put("name", "Cameron");
        obj.put("age", 10210);

        JSONArray list = new JSONArray();
        list.add("(a) Thomas Jefferson");
        list.add("(b) Abraham Lincoln");
        list.add("(c) George Washington");
        list.add("(d) Benjamin Franklin");

        obj.put("answers", list);

        try (FileWriter file = new FileWriter("test.json")) {

            file.write(obj.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.print(obj);

    }

}
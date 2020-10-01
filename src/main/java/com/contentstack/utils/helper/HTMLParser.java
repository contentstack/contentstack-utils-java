package com.contentstack.utils.helper;
import java.io.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.util.Arrays;
import java.util.List;

public class HTMLParser {

    public static void main(String[] args) {

        // Parse HTML String using JSoup library
        String rich_text_editor = "<div class=\"embedded-entry block-entry\" type=\"entry\" data-sys-entry-uid=\"blt55f6d8cbd7e03a1f\" data-sys-content-type-uid=\"article\" sys-style-type = \"block\"> \n" +
                "<span>{{title}}</span>\n" +
                "</div>";


        Document html = Jsoup.parse(rich_text_editor);
        String classTag = html.body().getElementsByTag("div").attr("class");
        System.out.println("Class Tag : " + classTag);
        String uid = html.body().getElementsByTag("div").attr("data-sys-entry-uid");
        System.out.println("Entry UID : " + uid);


        List<String> myList =
                Arrays.asList("a1", "a2", "b1", "c2", "c1");

        myList.stream()
                .filter(s -> s.startsWith("c"))
                .map(String::toUpperCase)
                .sorted()
                .forEach(System.out::println);

        try {
            JSONArray response = new HTMLParser().readJsonFile();
            JSONObject objJson = (JSONObject) response.get(0);
            System.out.println(objJson);
            boolean available = objJson.has("rich_text_editor");
            if (available){
                Object RTE = objJson.get("rich_text_editor");
                JSONArray jsonArray = ((JSONArray) RTE);
                System.out.println(jsonArray);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    JSONArray readJsonFile() throws IOException {
        String path = "src/test/resources/response.json";
        File file = new File(path);
        String absolutePath = file.getAbsolutePath();
        System.out.println(absolutePath);

        BufferedReader reader = new BufferedReader(new FileReader(file));
        StringBuilder stringBuilder = new StringBuilder();
        char[] buffer = new char[10];
        while (reader.read(buffer) != -1) {
            stringBuilder.append(new String(buffer));
            buffer = new char[10];
        }
        reader.close();

        String response = stringBuilder.toString();
        JSONArray objectsArray = new JSONArray(response);
        return objectsArray;
    }

}
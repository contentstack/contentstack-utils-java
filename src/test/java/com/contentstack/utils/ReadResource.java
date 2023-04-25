package com.contentstack.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ReadResource {

    public JSONObject readJson(String filename) throws IOException {
        File file = new File(filename);
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
        return new JSONObject(response);
    }

    public Attributes returnEntryAttributes(JSONObject entryObject) {
        boolean available = entryObject.has("rich_text_editor");
        JSONArray rteArray = null;
        if (available) {
            Object RTE = entryObject.get("rich_text_editor");
            rteArray = ((JSONArray) RTE);
        }

        Document html = null;
        assert rteArray != null;
        for (Object RTE : rteArray) {
            String stringify = (String) RTE;
            html = Jsoup.parse(stringify);
        }

        assert html != null;
        Elements embeddedEntries = html.body().getElementsByClass("embedded-entry");
        return embeddedEntries.get(0).attributes();
    }

    public Attributes returnAssetAttributes(JSONObject entryObject) {
        boolean available = entryObject.has("rich_text_editor");
        JSONArray rteArray = null;
        if (available) {
            Object RTE = entryObject.get("rich_text_editor");
            rteArray = ((JSONArray) RTE);
        }

        Document html = null;
        assert rteArray != null;
        for (Object RTE : rteArray) {
            String stringify = (String) RTE;
            html = Jsoup.parse(stringify);
        }
        assert html != null;
        Elements embeddedEntries = html.body().getElementsByClass("embedded-asset");
        return embeddedEntries.get(0).attributes();
    }

}

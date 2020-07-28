package ru.jobj4;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * ru.job4j
 * Class use json.org library
 *
 * @author romanvohmin
 * @since 28.07.2020
 */
public class JsonParser {
    private final Storage storage;

    public JsonParser(Storage storage) {
        this.storage = storage;
    }

    public String getJsonFromUrl(String url) {
        String json;
        Document doc = null;
        try {
            doc = Jsoup.connect(url).ignoreContentType(true).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (doc != null) {
            json = doc.body().text();
        } else {
            json = null;
        }
        return json;
    }

    public void getJsonArray(String jsonArray) {
        JSONParser parser = new JSONParser();
        Object obj = null;
        try {
            obj = parser.parse(jsonArray);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        JSONArray array = (JSONArray) obj;
        for (Object item : array) {
            storage.getQueue().offer(item.toString());
        }
    }

    public String getId(String json) {
        JSONObject jsonObject = (JSONObject) JSONValue.parse(json);
        return jsonObject.get("id").toString();
    }

    public String getsourceDataUrl(String json) {
        JSONObject jsonObject = (JSONObject) JSONValue.parse(json);
        return jsonObject.get("sourceDataUrl").toString();
    }

    public String getTokenDataUrl(String json) {
        JSONObject jsonObject = (JSONObject) JSONValue.parse(json);
        return jsonObject.get("tokenDataUrl").toString();
    }

    public String getUrlType(String json) throws ParseException {
        JSONObject jsonObject = (JSONObject) JSONValue.parse(json);
        return jsonObject.get("urlType").toString();
    }

    public String getVideoUrl(String json) {
        JSONObject jsonObject = (JSONObject) JSONValue.parse(json);
        return jsonObject.get("videoUrl").toString();
    }

    public String getValue(String json) {
        JSONObject jsonObject = (JSONObject) JSONValue.parse(json);
        return jsonObject.get("value").toString();
    }

    public String getTtl(String json) {
        var jsonObject = (JSONObject) JSONValue.parse(json);
        return jsonObject.get("ttl").toString();
    }

    public void generateNewJson() {
        JSONObject obj = new JSONObject();
        Integer id = storage.getKeys().poll();
        obj.put("id", id);
        obj.put("urlType", storage.getStorage().get(id).get("urlType"));
        obj.put("videoUrl", storage.getStorage().get(id).get("videoUrl"));
        obj.put("value", storage.getStorage().get(id).get("value"));
        obj.put("ttl", Integer.parseInt(storage.getStorage().get(id).get("ttl")));
        storage.getResult().add(obj.toJSONString());
    }

}

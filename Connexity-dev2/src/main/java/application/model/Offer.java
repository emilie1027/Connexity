package application.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.util.ArrayList;
import java.util.List;

public class Offer {
    static private JSONParser jsonParser = new JSONParser();
    public final String title;
    public final String sku;
    public final String upc;
    public final double price;

    public Offer(JSONObject offerJson) {
        //TODO: some element may not in every jsonObject (eg. relevancy and upc)
        //Need a way to check and parse it accordingly
        title = (String) offerJson.get("title");
        sku = (String) offerJson.get("sku");
        upc = (String) offerJson.get("upc");
        price = ((long)(((JSONObject) offerJson.get("price")).get("integral"))/100.0);
    }


    static public List<Offer> parseString(String input) {
        try {
            List<Offer> result = new ArrayList<>();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(input);
            JSONArray jsonArray = (JSONArray) ((JSONObject) jsonObject.get("offers")).get("offer");
            for(int i = 0; i < jsonArray.size(); i++) {
                JSONObject offerJson = (JSONObject) jsonArray.get(i);
                result.add(new Offer(offerJson));
            }
            return result;
        }
        catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}

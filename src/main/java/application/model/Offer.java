package application.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Offer {
    static private JSONParser jsonParser = new JSONParser();
    public String title = null;
    public String sku = null;
    public String upc = null;
    public String merchantId = null;
    public String description = null;
    public String manufacturer = null;
    public URL url = null;
    public String price = null;
    public List<URL> images = null;
    public String merchantName = null;
    public URL merchantLogoUrl = null;
    public String condition = null;
    public Double relevancy = new Double(0.0);
    public Double markdownPercent = new Double(0.0); //not sure if this is the discount

    public Offer(){}

    public Offer(JSONObject offerJson) throws IOException, NullPointerException {
        //some element may not in every jsonObject (eg. relevancy and upc)
        //Need a way to check and parse it accordingly
        title = (String) offerJson.get("title");
        try {
            sku = (String) offerJson.get("sku");
        }
        catch (NullPointerException e) {
            sku = null;
        }
        try{
            upc = (String) offerJson.get("upc");
        }
        catch (NullPointerException e) {
            upc = null;
        }
        try {
            merchantId = ((Long) offerJson.get("merchantId")).toString();
        }
        catch (NullPointerException e){
            merchantId = null;
        }
        try {
            Double priceValue = new Double((long)(((JSONObject) offerJson.get("price")).get("integral"))/100.0);
            price = String.format("%.2f", priceValue);
        }
        catch (NullPointerException e) {
            price = null;
        }
        try {
            description = (String) offerJson.get("description");
        }
        catch (NullPointerException e) {
            description = null;
        }
        try {
            manufacturer = (String) offerJson.get("manufacturer");
        }
        catch (NullPointerException e) {
            manufacturer = null;
        }
        try {
            url =  new URL((String) ((JSONObject) offerJson.get("url")).get("value"));
        }
        catch (NullPointerException e) {
            url = null;
        }
        try {
            images = new ArrayList<>(); //it can be a null array, pointed by the API I guess...
            JSONArray imgJSONArray = (JSONArray) ((JSONObject) offerJson.get("images")).get("image");
            for(int i = 0; i < imgJSONArray.size(); i++) {
                JSONObject imgJSON = (JSONObject) imgJSONArray.get(i);
                URL imgURL = new URL((String) imgJSON.get("value"));
                images.add(imgURL);
            }
        }
        catch (NullPointerException e){
            images = null;
        }
        try {
            merchantName = (String) offerJson.get("merchantName");
        }
        catch (NullPointerException e) {
            merchantName = null;
        }
        try {
            merchantLogoUrl = new URL((String) offerJson.get("merchantLogoUrl"));
        }
        catch (Exception e) {
            merchantLogoUrl = null;
        }
        try {
            condition = (String) offerJson.get("condition");
        }
        catch (NullPointerException e)  {
            condition = null;
        }
        try {
            relevancy = (Double)(offerJson.get("relevancy"));
        }
        catch (NullPointerException e){
            relevancy = 0.0;
        }
        try {
            markdownPercent = (Double)(offerJson.get("markdownPercent"));
        }
        catch (NullPointerException e) {
            markdownPercent = 0.0;
        }
    }


    static  public Offer parseStringForSingleElement(String input) throws IOException {
        if(input == null)
            return null;

        List<Offer> result = parseString(input);
        if(result.size() == 0) {
            return null;
        }
        else if (result.size() != 1) {
            System.out.println("Warning: parseStringForSingleElement receive more than one result");
        }
        return result.get(0);
    }

    static public List<Offer> parseString(String input) throws IOException {
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


    public static JSONParser getJsonParser() {
        return jsonParser;
    }


    public static void setJsonParser(JSONParser jsonParser) {
        Offer.jsonParser = jsonParser;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public String getSku() {
        return sku;
    }


    public void setSku(String sku) {
        this.sku = sku;
    }


    public String getUpc() {
        return upc;
    }


    public void setUpc(String upc) {
        this.upc = upc;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public String getManufacturer() {
        return manufacturer;
    }


    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }


    public URL getUrl() {
        return url;
    }


    public void setUrl(URL url) {
        this.url = url;
    }


    public String getPrice() {
        return price;
    }


    public void setPrice(String price) {
        this.price = price;
    }


    public List<URL> getImages() {
        return images;
    }


    public void setImages(List<URL> images) {
        this.images = images;
    }


    public String getMerchantName() {
        return merchantName;
    }


    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }


    public URL getMerchantLogoUrl() {
        return merchantLogoUrl;
    }


    public void setMerchantLogoUrl(URL merchantLogoUrl) {
        this.merchantLogoUrl = merchantLogoUrl;
    }


    public String getCondition() {
        return condition;
    }


    public void setCondition(String condition) {
        this.condition = condition;
    }


    public Double getRelevancy() {
        return relevancy;
    }


    public void setRelevancy(Double relevancy) {
        this.relevancy = relevancy;
    }


    public Double getMarkdownPercent() {
        return markdownPercent;
    }


    public void setMarkdownPercent(Double markdownPercent) {
        this.markdownPercent = markdownPercent;
    }
}

package application.model;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class Offer {
    static private JSONParser jsonParser = new JSONParser();
    public String title = null;
    public String sku = null;
    public String upc = null;
    public String description = null;
    public String manufacturer = null;
    public String url = null;
    public Double price = null;
    public List<Image> images = null;
    public String merchantName = null;
    public String merchantLogoUrl = null;
    public String condition = null;
    public Double relevancy = null;
    public Double markdownPercent = null;
    
    public Offer(JSONObject offerJson) throws IOException, NullPointerException {
        //some element may not in every jsonObject (eg. relevancy and upc)
        //Need a way to check and parse it accordingly
        title = (String) offerJson.get("title");
        try {
            sku = (String) offerJson.get("sku");
        }
        catch (Exception e) {
            sku = null;
        }
        try{
            upc = (String) offerJson.get("upc");
        }
        catch (Exception e) {
            upc = null;
        }
        try {
            price = new Double((long)(((JSONObject) offerJson.get("price")).get("integral"))/100.0);
        }
        catch (Exception e) {
            price = null;
        }
        try {
            description = (String) offerJson.get("description");
        }
        catch (Exception e) {
            description = null;
        }
        try {
            manufacturer = (String) offerJson.get("manufacturer");
        }
        catch (Exception e) {
            manufacturer = null;
        }
        try {
            url = (String) offerJson.get("url");
        }
        catch (Exception e) {
            url = null;
        }
        try {
            images = new ArrayList<>(); //it can be a null array, pointed by the API I guess...
            JSONArray imgJSONArray = (JSONArray) ((JSONObject) offerJson.get("images")).get("image");
            for(int i = 0; i < imgJSONArray.size(); i++) {
                JSONObject imgJSON = (JSONObject) imgJSONArray.get(i);
                URL imgURL = new URL((String) imgJSON.get("value"));
                Image img = ImageIO.read(imgURL);
                images.add(img);
            }
        }
        catch (Exception e){
            images = null;
        }
        try {
            merchantName = (String) offerJson.get("merchantName");
        }
        catch (Exception e) {
            merchantName = null;
        }
        try {
            merchantLogoUrl = (String) offerJson.get("merchantLogoUrl");
        }
        catch (Exception e) {
            merchantLogoUrl = null;
        }
        try {
            condition = (String) offerJson.get("condition");
        }
        catch (Exception e)  {
            condition = null;
        }
        try {
            relevancy = new Double((long) offerJson.get("relevancy"));
        }
        catch (Exception e){
            relevancy = null;
        }
        try {
            markdownPercent = new Double((long) offerJson.get("markdownPercent"));
        }
        catch (Exception e) {
            markdownPercent = null;
        }
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
    
    
    public String getUrl() {
        return url;
    }
    
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    
    public Double getPrice() {
        return price;
    }
    
    
    public void setPrice(Double price) {
        this.price = price;
    }
    
    
    public List<Image> getImages() {
        return images;
    }
    
    
    public void setImages(List<Image> images) {
        this.images = images;
    }
    
    
    public String getMerchantName() {
        return merchantName;
    }
    
    
    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }
    
    
    public String getMerchantLogoUrl() {
        return merchantLogoUrl;
    }
    
    
    public void setMerchantLogoUrl(String merchantLogoUrl) {
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

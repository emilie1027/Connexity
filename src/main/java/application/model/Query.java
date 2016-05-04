package application.model;

import java.util.Map;
import java.util.HashMap;

/**
 * Created by lijiayu on 5/1/16.
 */
public class Query {
    
    private Map<String, String> params;
    
    public Query(){
        params = new HashMap<>();
        params.put("keyword", "connexity");
        params.put("sort", "relevancy_desc");
    }
    
    public Query(String keyword) {
        params = new HashMap<>();
        params.put("keyword", keyword);
        params.put("sort", "relevancy_desc");
    }
    
    public Map<String, String> getParams() {
        return params;
    }
    
    public void setParams(Map<String, String> params) {
        this.params = params;
    }
    
    public Map<String, String> updateSingleParam(String key, String value){
        if (isValidUpdate(key, value)) {
            this.params.put(key, value);
        }
        return this.params;
    }
    
    public String getSingleParam(String key) {
        if (this.params.containsKey(key))
            return this.params.get(key);
        else
            return null;
    }
    
    public boolean isValidUpdate(String key, String value) {
        if (key.equals("sort")){
            if (value.equals("relevancy_desc") || value.equals("price_asc") || value.equals("price_desc"))
                return true;
            else
                return false;
        }
        return true;
    }
    
}

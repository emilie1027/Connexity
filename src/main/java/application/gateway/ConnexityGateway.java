package application.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpMethodParams;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ConnexityGateway {
    @Value("${catalog.apiKey}")
    private String apiKey;
    @Value("${catalog.publisherId}")
    private String publisherId;
    @Value("${catalog.hostAddress}")
    private String hostAddress;
    private String productDomain = "product";

    public String getByUpc(String upc) throws IOException {
        if(upc == null || upc.equals(""))
            throw new IllegalArgumentException("upc cannot be empty!");
        if(upc.length() != 12)
            throw new IllegalArgumentException("upc need to be 12 char long");

        Map<String, String> defaultParameter = returnDefaultParameter();
        defaultParameter.put("productIdType","UPC");
        defaultParameter.put("productId",upc);
        return generalGet(defaultParameter);
    }

    public String getBySku(String sku, String merchantId) throws IOException {
        if(sku == null || sku.equals("") || merchantId == null || merchantId.equals(""))
            throw new IllegalArgumentException("sku and merchantId cannot be empty!");

        Map<String, String> defaultParameter = returnDefaultParameter();
        defaultParameter.put("productIdType","sku");
        defaultParameter.put("productId",sku);
        defaultParameter.put("merchantId", merchantId);
        return generalGet(defaultParameter);
    }

    public String getByUpcOrSku(String upc, String sku, String merchantId) throws IOException {
        String result = null;
        //the api won't work for upc length != 12, and the upc returned by api can be more than 12 char long
        //for some products, the api will not recognize its upc/sku returned
        if(upc != null && !upc.equals("") && upc.length() == 12) {
            result = getByUpc(upc);
            if(result != null)
                return result;
        }

        if(sku != null && !sku.equals("") && merchantId != null && !merchantId.equals(""))
            return getBySku(sku, merchantId);

        return null;
    }

    public String getByKeyWord(String key) throws IOException {
        if(key == null || key.equals(""))
            throw new IllegalArgumentException("search key should not be empty nor null");

        return getByKeyWord(key, null);
    }

    //search the catalog according to key, overideParameter can add new parameter or override existing ones
    //overideParameter should not contain key, if so it will be overridden by the key provided in the method
    public String getByKeyWord(String key, Map<String, String> overrideParameter) throws IOException{
        if(key == null || key.equals(""))
            throw new IllegalArgumentException("search key should not be empty nor null");

        Map<String, String> defaultParameter = returnDefaultParameter();
        if(overrideParameter != null) {
            for (Map.Entry<String, String> entry : overrideParameter.entrySet()) {
                defaultParameter.put(entry.getKey(), entry.getValue());
            }
        }
        defaultParameter.put("keyword",key);
        return generalGet(defaultParameter);
    }

    //a general method to send get request, parameters are passed by requestParameter
    //apiKey and publisherId cannot be overridden, or an exception will be thrown
    //return null if an error occurred during the connection
    //by default the request will return offers only for 10 results in json format
    private String generalGet(Map<String, String> requestParameter) throws IOException {
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(hostAddress+productDomain+"?");

        List<NameValuePair> queryStringList = new ArrayList<>();
        queryStringList.add(new NameValuePair("publisherId",publisherId));
        queryStringList.add(new NameValuePair("apiKey", apiKey));
        for (Map.Entry<String, String> entry : requestParameter.entrySet()) {
            String key = entry.getKey();
            if(key.equals("apiKey") || key.equals("publisherId"))
                throw new IllegalArgumentException("apiKey and publisherId should not be modified");
            else{
                queryStringList.add(new NameValuePair(key, entry.getValue()));
            }
        }
        method.setQueryString(queryStringList.toArray(new NameValuePair[queryStringList.size()]));

        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));

        int statusCode = client.executeMethod(method);
        if (statusCode != HttpStatus.SC_OK) {
            return null;
        }
        byte[] responseBody = method.getResponseBody();
        return new String(responseBody);
    }

    //reset the parameters to be default
    private Map returnDefaultParameter(){
        Map<String, String> defaultParameter = new HashMap<>();
        defaultParameter.put("offersOnly", "true");
        defaultParameter.put("results","100");
        defaultParameter.put("format", "json");

        //for some reason, without these default values
        //the api call will be unstable
        defaultParameter.put("start","0");
        defaultParameter.put("backfillResults","0");
        defaultParameter.put("startOffers","0");
        defaultParameter.put("resultsOffers","0");
        defaultParameter.put("sort","relevancy_desc");
        defaultParameter.put("resultsAttribute","10");
        defaultParameter.put("resultsAttributeValues","10");
        defaultParameter.put("reviews","none");
        defaultParameter.put("callback","callback");
        return defaultParameter;
    }
}

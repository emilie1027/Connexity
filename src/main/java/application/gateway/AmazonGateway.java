package application.gateway;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.jdom2.JDOMException;
import org.springframework.beans.factory.annotation.Value;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import application.utilities.Signature;

@Component
public class AmazonGateway {
    enum IdType {UPC, SKU};
    @Value("${amazon.associateId}")
    private String associateId;
    @Value("${amazon.accessKey}")
    private String accessKey;
    @Value("${amazon.hostAddress}")
    private String hostAddress;
    @Value("${amazon.secretKey}")
    private String secretKey;
    @Value("${amazon.requestURI}")
    private String requestURI;
    private String service = "AWSECommerceService";

    static Logger log = Logger.getLogger(AmazonGateway.class.getName());

    public String lookupASINbyUPCorSKU(String upc, String sku) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        String result;
        result = lookupASINByUPC(upc);
        if(result != null)
            return result;
        else
            return lookupASINBySKU(sku);
    }


    public String lookupASINByUPC(String upc) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        try {
            String result = lookupByProductIdInXML(upc, IdType.UPC);
            result = extractASINFromString(result);
            return result;
        }
        catch (JDOMException e) {
            log.warn(e);
            return null;
        }
    }

    public String lookupASINBySKU(String sku) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        try {
            String result = lookupByProductIdInXML(sku, IdType.SKU  );
            result = extractASINFromString(result);
            return result;
        }
        catch (JDOMException e) {
            log.warn(e);
            return null;
        }
    }

    private String lookupByProductIdInXML(String itemId, IdType type) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        if(itemId == null)
            return null;
        Signature sig = new Signature(hostAddress,accessKey,secretKey,associateId);
        String operation = "ItemLookup";
        String searchIndex = "All";
        String timeStamp = sig.timestamp();
        String signaturedUrl;
        String idType;
        if(type == IdType.SKU) {
            idType = "SKU";
        }
        else{
            idType = "UPC";
        }

        Map<String, String> queryParam = new HashMap<>();
        queryParam.put("AWSAccessKeyId", accessKey);
        queryParam.put("AssociateTag", associateId);
        queryParam.put("IdType", idType);
        queryParam.put("ItemId", itemId);
        queryParam.put("Operation", operation);
        queryParam.put("Service", service);
        queryParam.put("Timestamp", timeStamp);
        queryParam.put("SearchIndex",searchIndex);
        signaturedUrl = sig.sign(queryParam);
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(signaturedUrl);
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));

        int statusCode = client.executeMethod(method);
        if (statusCode != HttpStatus.SC_OK) {
            log.error(method.getResponseBodyAsString());
            return null;
        }
        byte[] responseBody = method.getResponseBody();
        return new String(responseBody);
    }

    private String extractASINFromString(String input)throws IOException, JDOMException{
        if(input == null) {
            return null;
        }
        else if (input.indexOf("<ASIN>") == -1 || input.indexOf("</ASIN>") == -1) {
            return null;
        }
        else {
            return input.substring(input.indexOf("<ASIN>")+6, input.indexOf("</ASIN>"));
        }
    }

    public String similarityLookupByASIN(List<String> ASINs) throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        if(ASINs.size() == 0)
            return null;
        Signature sig = new Signature(hostAddress,accessKey,secretKey,associateId);
        String operation = "SimilarityLookup";
        String similarityType = "Random";
        String timeStamp = sig.timestamp();
        String signaturedUrl;
        String ItemId = "";
        String ResponseGroup = " Images,ItemAttributes"; //added for extracting images
        for (String asin : ASINs) ItemId = ItemId + asin + ",";
        ItemId = ItemId.substring(0, ItemId.length() - 1);

        Map<String, String> queryParam = new HashMap<>();
        queryParam.put("Service",service);
        queryParam.put("AWSAccessKeyId", accessKey);
        queryParam.put("AssociateTag", associateId);
        queryParam.put("Operation", operation);
        queryParam.put("ItemId", ItemId);
        queryParam.put("ResponseGroup", ResponseGroup);
        queryParam.put("SimilarityType", similarityType);
        queryParam.put("Timestamp", timeStamp);
        signaturedUrl = sig.sign(queryParam);
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(signaturedUrl);
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
                new DefaultHttpMethodRetryHandler(3, false));

        int statusCode = client.executeMethod(method);
        if (statusCode != HttpStatus.SC_OK) {
            return null;
        }
        byte[] responseBody = method.getResponseBody();
        return new String(responseBody);
    }

}

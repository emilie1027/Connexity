package application.gateway;

import application.model.Offer;
import application.utilities.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

@Component
public class RedisGateway {
    @Value("${trend.maxNumberOfTrends}")
    private int numberOfTrends;
    @Value("${redis.address}")
    private String hostAddress;
    @Autowired
    private ConnexityGateway connexityGateway;
    private Jedis jedis;

    @PostConstruct
    public void init() {
        jedis = new Jedis(hostAddress);
    }

    public void insertRecord(String upc, String sku, String merchantId) {
        upc = upc == null? "":upc;
        sku = sku == null? "":sku;
        merchantId = merchantId == null? "":merchantId;

        String record = String.format("upc=%s;sku=%s;merchantId=%s", upc, sku, merchantId);
        String currentDate = Utility.currentDate();
        Transaction transaction = jedis.multi();
        transaction.zincrby(currentDate, 1, record);
        transaction.expire(currentDate, (int)Utility.timeUntilMidnightInSecs());
        transaction.exec();
    }

    public List<Offer> getTopTrends() throws IOException {
        List<Offer> listOfTrends = new ArrayList<>();
        Set<String> resultInString = getTopTrendsInString();
        for(String result:resultInString) {
            Offer offer = getOfferFromRecord(result);
            if(offer != null) {
                listOfTrends.add(offer);
            }
        }
        return listOfTrends;
    }

    public Set<String> getTopTrendsInString()
    {
        return jedis.zrevrange(Utility.currentDate(), 0, numberOfTrends);
    }

    public String getASINByUPC(String upc) {
        if(upc == null) return null;
        return jedis.hget("UPC",upc);
    }

    public String getASINBySKU(String sku) {
        if(sku == null) return null;
        return jedis.hget("SKU",sku);
    }

    public void cacheASINForUPC(String upc, String ASIN) {
        jedis.hset("UPC",upc,ASIN);
    }

    public void cacheASINForSKU(String sku, String ASIN) {
        jedis.hset("SKU", sku,ASIN);
    }

    //for test only
    public void deleteTodayTrend() {
        jedis.del(Utility.currentDate());
    }

    public void deleteUPCCache(String upc) {
        jedis.hdel("UPC", upc);
    }

    public void deleteSKUCache(String sku) {
        jedis.hdel("SKU", sku);
    }

    private Map<String, String> parseRecord(String input) {
        Map<String, String> result = new HashMap<>();
        String[] keyValuePairs = input.split(";");
        for(String keyValuePair : keyValuePairs) {
            String[] keyValue = keyValuePair.split("=");
            //some of the field may be empty
            if(keyValue.length == 2)
                result.put(keyValue[0], keyValue[1]);
        }
        return result;
    }

    private Offer getOfferFromRecord(String input) throws IOException {
        Map<String, String> offerInfo = parseRecord(input);
        String offerString = connexityGateway.getByUpcOrSku(offerInfo.get("upc"), offerInfo.get("sku"), offerInfo.get("merchantId"));
        return Offer.parseStringForSingleElement(offerString);
    }
}

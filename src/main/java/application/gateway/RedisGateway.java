package application.gateway;

import application.model.Offer;
import application.utilities.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Component
public class RedisGateway {
    @Value("${trend.maxNumberOfTrends}")
    private int numberOfTrends;
    @Value("${redis.address}")
    private String hostAddress;
    @Value("${redis.uri}")
    private String redisUri;
    @Autowired
    private ConnexityGateway connexityGateway;
    static private JedisPool jedisPool;

    @PostConstruct
    public void init() throws URISyntaxException {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(20);
        poolConfig.setMaxIdle(5);
        poolConfig.setMinIdle(1);
        poolConfig.setBlockWhenExhausted(true);
        if(!redisUri.equals("")) {
            URI uri = new URI(redisUri);
            jedisPool = new JedisPool(poolConfig, uri);
        }
        else if(!hostAddress.equals("")) {
            jedisPool = new JedisPool(hostAddress);
        }
        else {
            throw new IllegalArgumentException("Redis address is not hosted!");
        }
    }

    public void insertRecord(String upc, String sku, String merchantId) {
        Jedis jedis = jedisPool.getResource();
        upc = upc == null? "":upc;
        sku = sku == null? "":sku;
        merchantId = merchantId == null? "":merchantId;

        String record = String.format("upc=%s;sku=%s;merchantId=%s", upc, sku, merchantId);
        String currentDate = Utility.currentDate();
        Transaction transaction = jedis.multi();
        transaction.zincrby(currentDate, 1, record);
        transaction.expire(currentDate, (int)Utility.timeUntilMidnightInSecs());
        transaction.exec();
        jedis.close();
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
        Jedis jedis = jedisPool.getResource();
        Set<String> result = jedis.zrevrange(Utility.currentDate(), 0, numberOfTrends-1);
        jedis.close();
        return result;
    }

    public String getASINByUPC(String upc) {
        if(upc == null) return null;
        Jedis jedis = jedisPool.getResource();
        String result = jedis.hget("UPC",upc);
        jedis.close();
        return result;
    }

    public String getASINBySKU(String sku) {
        if(sku == null) return null;
        Jedis jedis = jedisPool.getResource();
        String result = jedis.hget("SKU",sku);
        jedis.close();
        return result;
    }

    public void cacheASINForUPC(String upc, String ASIN)
    {
        Jedis jedis = jedisPool.getResource();
        jedis.hset("UPC",upc,ASIN);
        jedis.close();
    }

    public void cacheASINForSKU(String sku, String ASIN) {
        Jedis jedis = jedisPool.getResource();
        jedis.hset("SKU", sku,ASIN);
        jedis.close();
    }

    //for test only
    public void deleteTodayTrend() {
        Jedis jedis = jedisPool.getResource();
        jedis.del(Utility.currentDate());
        jedis.close();
    }

    public void deleteUPCCache(String upc) {
        Jedis jedis = jedisPool.getResource();
        jedis.hdel("UPC", upc);
        jedis.close();
    }

    public void deleteSKUCache(String sku) {
        Jedis jedis = jedisPool.getResource();
        jedis.hdel("SKU", sku);
        jedis.close();
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

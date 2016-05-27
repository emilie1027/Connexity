import application.gateway.RedisGateway;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.util.Assert;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = application.Application.class, loader = AnnotationConfigContextLoader.class)
public class RedisGatewayTest {
    @Autowired
    RedisGateway redisGateway;

    //not a complete test, need to change some private methods to public to make the test work
    @Test
    public void testTrend() throws IOException {
        redisGateway.insertRecord("upc1","sku1","merchantId1");
        redisGateway.insertRecord("upc2","sku2","merchantId2");
        redisGateway.insertRecord("upc2","sku2","merchantId2");
        redisGateway.insertRecord("upc3","sku3","merchantId3");
        redisGateway.insertRecord("upc3","sku3","merchantId3");
        redisGateway.insertRecord("upc3","sku3","merchantId3");
        redisGateway.deleteTodayTrend();
    }

    @Test
    public void testCache() {
        Assert.isNull(redisGateway.getASINBySKU("sku1"));
        Assert.isNull(redisGateway.getASINByUPC("upc1"));
        redisGateway.cacheASINForSKU("sku1","skuASIN");
        redisGateway.cacheASINForUPC("upc1", "upcASIN");
        Assert.isTrue(redisGateway.getASINBySKU("sku1").equals("skuASIN"));
        Assert.isTrue(redisGateway.getASINByUPC("upc1").equals("upcASIN"));
        redisGateway.deleteSKUCache("sku1");
        redisGateway.deleteUPCCache("upc1");
        Assert.isNull(redisGateway.getASINBySKU("sku1"));
        Assert.isNull(redisGateway.getASINByUPC("upc1"));
    }
}

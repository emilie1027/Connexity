import application.gateway.TrendGateway;
import application.model.Offer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = application.Application.class, loader = AnnotationConfigContextLoader.class)
public class TrendGatewayTest {
    @Autowired
    TrendGateway trendGateway;

    //not a complete test, need to change some private methods to public to make the test work
    @Test
    public void testTrend() throws IOException {
        trendGateway.insertRecord("upc1","sku1","merchantId1");
        trendGateway.insertRecord("upc2","sku2","merchantId2");
        trendGateway.insertRecord("upc2","sku2","merchantId2");
        trendGateway.insertRecord("upc3","sku3","merchantId3");
        trendGateway.insertRecord("upc3","sku3","merchantId3");
        trendGateway.insertRecord("upc3","sku3","merchantId3");
        trendGateway.deleteTodayTrend();
    }
}

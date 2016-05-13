package unit_test;


import application.gateway.HistoryGateway;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.util.Assert;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = application.Application.class, loader = AnnotationConfigContextLoader.class)
public class HistoryGatewayUnitTest {
    @Autowired
    private HistoryGateway historyGateway;
    @Value("${history.maxNumberOfRecord}")
    private  int maxNumberOfRecord;

    @Test
    public void testInsertNonexistentId() {
        String randomId = UUID.randomUUID().toString();
        historyGateway.insertHistory(randomId, "upc1", "sku1", "merchantId1");
        List<Map<String, String>> result = historyGateway.findHistory(randomId);
        Assert.notNull(result);
        Assert.notEmpty(result);
        Map<String, String> history = result.iterator().next();
        Assert.isTrue(history.get("upc").equals("upc1"));
        Assert.isTrue(history.get("sku").equals("sku1"));
        Assert.isTrue(history.get("merchantId").equals("merchantId1"));

        historyGateway.deleteHistory(randomId, HistoryGateway.Option.All);
        result = historyGateway.findHistory(randomId);
        Assert.isTrue(result.size() == 0);
        historyGateway.deleteId(randomId);
        result = historyGateway.findHistory(randomId);
        Assert.isNull(result);
    }

    @Test
    public void testInsertMultipleHistory() {
        String randomId = UUID.randomUUID().toString();
        historyGateway.insertHistory(randomId, "upc1", "sku1", "merchantId1");
        historyGateway.insertHistory(randomId, "upc2", "sku2", "merchantId2");
        List<Map<String, String>> result = historyGateway.findHistory(randomId);
        Assert.notNull(result);
        Assert.notEmpty(result);
        Assert.isTrue(result.size() == 2);
        Iterator<Map<String, String>> iterator = result.iterator();
        Map<String, String> history = iterator.next();
        Assert.isTrue(history.get("upc").equals("upc1"));
        Assert.isTrue(history.get("sku").equals("sku1"));
        Assert.isTrue(history.get("merchantId").equals("merchantId1"));
        history = iterator.next();
        Assert.isTrue(history.get("upc").equals("upc2"));
        Assert.isTrue(history.get("sku").equals("sku2"));
        Assert.isTrue(history.get("merchantId").equals("merchantId2"));

        historyGateway.deleteHistory(randomId, HistoryGateway.Option.Earliest);
        result = historyGateway.findHistory(randomId);
        Assert.notNull(result);
        Assert.notEmpty(result);
        Assert.isTrue(result.size() == 1);
        history = result.iterator().next();
        Assert.isTrue(history.get("upc").equals("upc2"));
        Assert.isTrue(history.get("sku").equals("sku2"));
        Assert.isTrue(history.get("merchantId").equals("merchantId2"));

        historyGateway.deleteHistory(randomId, HistoryGateway.Option.Earliest);
        result = historyGateway.findHistory(randomId);
        Assert.isTrue(result.size() == 0);

        historyGateway.deleteId(randomId);
        result = historyGateway.findHistory(randomId);
        Assert.isNull(result);
    }

    @Test
    public void testInsertMoreThanMaximumHistory() {
        String randomId = UUID.randomUUID().toString();
        for (int i = 0; i < maxNumberOfRecord*2; i++) {
            String number = new Integer(i).toString();
            historyGateway.insertHistory(randomId, "upc" + number, "sku" + number, "merchantId" + number);
        }
        List<Map<String, String>> result = historyGateway.findHistory(randomId);
        Assert.isTrue(result.size() == maxNumberOfRecord);

        historyGateway.deleteHistory(randomId, HistoryGateway.Option.Earliest);
        result = historyGateway.findHistory(randomId);
        Assert.isTrue(result.size() == maxNumberOfRecord - 1);
        String numberString = new Integer(maxNumberOfRecord+1).toString();
        Map<String, String> history = result.iterator().next();
        Assert.isTrue(history.get("upc").equals("upc" + numberString));
        Assert.isTrue(history.get("sku").equals("sku" + numberString));
        Assert.isTrue(history.get("merchantId").equals("merchantId" + numberString));

        historyGateway.deleteHistory(randomId, HistoryGateway.Option.Latest);
        result = historyGateway.findHistory(randomId);
        Assert.isTrue(result.size() == maxNumberOfRecord - 2);
        numberString = new Integer(2*maxNumberOfRecord-2).toString();
        history = result.get(result.size()-1);
        Assert.isTrue(history.get("upc").equals("upc" + numberString));
        Assert.isTrue(history.get("sku").equals("sku" + numberString));
        Assert.isTrue(history.get("merchantId").equals("merchantId" + numberString));

        historyGateway.deleteHistory(randomId, HistoryGateway.Option.All);
        result = historyGateway.findHistory(randomId);
        Assert.isTrue(result.size() == 0);
        historyGateway.deleteId(randomId);
        result = historyGateway.findHistory(randomId);
        Assert.isNull(result);
    }
}

package unit_test;

import application.gateway.ConnexityGateway;
import application.model.Offer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = application.Application.class, loader = AnnotationConfigContextLoader.class)

public class OfferUnitTest {
    @Autowired
    private ConnexityGateway connexityGateway;
    private String testInput;

    @PostConstruct
    public void  init() throws IOException{
        testInput = connexityGateway.getByKeyWord("apple");
    }

    @Test
    //not strictly a unit test, need to revise
    public void testParseStringToObject() throws IOException{
        List<Offer> parserReusult = Offer.parseString(testInput);
        Assert.notNull(parserReusult);
    }

    @Test
    //not strictly a unit test, need to revise
    public void testParseStringToObjectReturnByProdcutIdSearch() throws IOException{
        List<Offer> parserReusult = Offer.parseString(testInput);
        Iterator<Offer> iterator = parserReusult.iterator();
        List<Offer> finalResult = new ArrayList<>();
        int i = 0;
        while(iterator.hasNext()) {
            Offer offer = iterator.next();
            String resultString = connexityGateway.getByUpcOrSku(offer.upc, offer.sku, offer.merchantId);
            if(resultString != null) {
                Offer result = Offer.parseStringForSingleElement(resultString);
                if(result != null) {
                    finalResult.add(result);
                    i++;
                }
            }
            System.out.println(i);
        }
        Assert.notNull(parserReusult);
        Assert.isTrue(finalResult.size() > 0);
    }

}

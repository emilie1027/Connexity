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
}

package unit_test;

import application.gateway.ConnexityGateway;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = application.Application.class, loader = AnnotationConfigContextLoader.class)

public class ConnexityGatewayUnitTest {
    @Autowired
    private ConnexityGateway connexityGateway;

    @Test
    public void testSearchByKey() {
        try {
            String result = connexityGateway.getByKeyWord("apple");
            Assert.notNull(result);
        }
        catch (Exception e){
            Assert.isTrue(false, "Exception is caught");
        }
    }

    @Test
    public void testSearchByKeyWithSpace() {
        try {
            String result = connexityGateway.getByKeyWord("Code Complete");
            Assert.notNull(result);
        }
        catch (Exception e){
            Assert.isTrue(false, "Exception is caught");
        }
    }

    @Test
    public void testSearchByKeyWithOverriddenParameter() {
        try {
            Map<String, String> param = new HashMap<String, String>();
            param.put("format","xml");
            String result = connexityGateway.getByKeyWord("Code Complete",param);
            Assert.notNull(result);
            Assert.isTrue(result.contains("xml"));
        }
        catch (Exception e){
            Assert.isTrue(false, "Exception is caught");
        }
    }
}

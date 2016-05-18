import application.gateway.AmazonGateway;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.util.Assert;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = application.Application.class, loader = AnnotationConfigContextLoader.class)
public class AmazonGatewayUnitTest {
    @Autowired
    AmazonGateway amazonGateway;

    @Test
    public void looukupValidUCP() throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        String result = amazonGateway.lookupASINByUPC("888462021852");
        Assert.isTrue(result.equals("B00OTXG05S"), result);
    }

    @Test
    public void lookupInValidUCP() throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        String result = amazonGateway.lookupASINByUPC("Invalid");
        Assert.isNull(result);
    }

    @Test
    public void lookupValidSKU() throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        String result = amazonGateway.lookupASINBySKU("MD785LLB");
        Assert.isTrue(result.equals("B00G2TK76A"), result);
    }

    @Test
    public void lookupInvalidSKU() throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        String result = amazonGateway.lookupASINBySKU("invalidSKU");
        Assert.isNull(result);
    }

    @Test
    public void lookupBySKUAndUPC() throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        String result = amazonGateway.lookupASINbyUPCorSKU("888462021852","MD785LLB");
        Assert.isTrue(result.equals("B00OTXG05S"),result);
    }

}

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
import java.util.ArrayList;
import java.util.List;

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
    public void looukupInValidUCP() throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        String result = amazonGateway.lookupASINByUPC("Invalid");
        Assert.isNull(result);
    }

    @Test
    public void similarityCheckForSingleASIN() throws NoSuchAlgorithmException, InvalidKeyException, IOException {
        List<String> list = new ArrayList<>();
        list.add("B00OTXG05S");
        String result = amazonGateway.similarityLookupByASIN(list);
        Assert.isNull(result);
    }
}

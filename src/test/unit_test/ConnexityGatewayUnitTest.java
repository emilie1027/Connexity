package unit_test;

import application.gateway.ConnexityGateway;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.util.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = application.Application.class, loader = AnnotationConfigContextLoader.class)

public class ConnexityGatewayUnitTest {
    @Autowired
    private ConnexityGateway connexityGateway;

    @Test
    public void testSearchByKey() {
        System.out.print(connexityGateway.getByKeyword("key"));
        Assert.isTrue(true);
    }
}

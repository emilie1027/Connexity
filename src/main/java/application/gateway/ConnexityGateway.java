package application.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConnexityGateway {
    @Value("${catalog.apiKey}")
    private String apiKey;
    @Value("${catalog.publisherId}")
    private String publisherId;
    @Value("${catalog.hostAddress}")
    private String hostAddress;

    public String getByKeyword(String key) {
        return hostAddress+ " " +publisherId+" "+apiKey;
    }
}

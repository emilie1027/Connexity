package application.search;

import application.gateway.ConnexityGateway;
import application.model.Offer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class SearchStrategy {
    @Autowired
    private ConnexityGateway connexityGateway;

    public List<Offer> basicSearch(String key) throws IOException{
        String searchResult = connexityGateway.getByKeyWord(key);
        return Offer.parseString(searchResult);
    }

    public List<Offer> basicSearch(String key, Map<String, String> overrideParameter) throws IOException{
        String searchResult = connexityGateway.getByKeyWord(key, overrideParameter);
        return Offer.parseString(searchResult);
    }
}
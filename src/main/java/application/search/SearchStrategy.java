package application.search;

import application.gateway.ConnexityGateway;
import application.model.Offer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class SearchStrategy {
    @Autowired
    private ConnexityGateway connexityGateway;
    public List<Offer> search(String key) throws IOException{
        String searchResult = connexityGateway.getByKeyWord(key);
        return Offer.parseString(searchResult);
    }
}
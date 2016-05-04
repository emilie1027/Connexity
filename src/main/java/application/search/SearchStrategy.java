package application.search;

import application.gateway.ConnexityGateway;
import application.model.Offer;
import org.codehaus.groovy.tools.shell.IO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
public class SearchStrategy {
    @Autowired
    private ConnexityGateway connexityGateway;
    
    public List<Offer> basicSearch(String key) throws IOException {
        String searchResult = connexityGateway.getByKeyWord(key);
        return Offer.parseString(searchResult);
    }
    
    public List<Offer> basicSearch(String key, Map<String, String> overrideParameter) throws IOException {
        String searchResult = connexityGateway.getByKeyWord(key, overrideParameter);
        return Offer.parseString(searchResult);
    }
    
    public List<Offer> advancedSearch(String key) throws IOException {
        List<Offer> searchResult = basicSearch(key);
        Collections.sort(searchResult, new Comparator<Offer>() {
            public int compare(Offer o1, Offer o2) {
                if (o1.getMarkdownPercent() == null && o2.getMarkdownPercent() == null)
                    return 0;
                else if (o1.getMarkdownPercent() == null)
                    return -1;
                else if (o2.getMarkdownPercent() == null)
                    return 1;
                else
                    return Double.compare(o1.getMarkdownPercent(), o2.getRelevancy());
            }
        });
        return searchResult;
    }
    
}
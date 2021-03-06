package application.search;

import application.gateway.BingSpellCheckGateway;
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
    @Autowired
    private BingSpellCheckGateway bingSpellCheckGateway;
    
    public List<Offer> basicSearch(String key) throws IOException {
        String searchResult = connexityGateway.getByKeyWord(key);
        List<Offer> offerlist = Offer.parseString(searchResult);
        if (offerlist.size() == 0) {
            String newKey = bingSpellCheckGateway.bingSpellCheck(key);
            return Offer.parseString(connexityGateway.getByKeyWord(newKey));
        }
        return offerlist;
    }

    public List<Offer> basicSearch(String key, Map<String, String> overrideParameter) throws IOException {
        String searchResult = connexityGateway.getByKeyWord(key, overrideParameter);
        List<Offer> offerlist = Offer.parseString(searchResult);
        if (offerlist.size() == 0) {
            String newKey = bingSpellCheckGateway.bingSpellCheck(key);
            return Offer.parseString(connexityGateway.getByKeyWord(newKey, overrideParameter));
        }
        return offerlist;
    }

    public List<Offer> advancedSearch(Map<String, String> searchParams) throws IOException {
        String key = searchParams.get("key");
        String sort = searchParams.get("sort");
        if (sort == null)
            return basicSearch(key);
        else {
            switch (sort) {
                case "discount_desc":
                    return markdownSearch(key);
                case "price_desc":
                case "price_asc":
                    return basicSearch(key, searchParams);
                default:
                    return basicSearch(key);
            }
        }
    }
    
    public List<Offer> markdownSearch(String key) throws IOException {
        List<Offer> searchResult = basicSearch(key);
        Collections.sort(searchResult, new Comparator<Offer>() {
            public int compare(Offer o1, Offer o2) {
                return Double.compare(o2.getMarkdownPercent(), o1.getMarkdownPercent());
            }
        });
        return searchResult;
    }
   
    //added by xiangning on 5/12 to allow history offer search
    public List<Offer> historySearch(Map<String, String> history) throws IOException {
            String upc = history.get("upc");
            String sku = history.get("sku");
            String merchantId = history.get("merchantId");
            String searchResult = connexityGateway.getByUpcOrSku(upc, sku, merchantId);
        if(searchResult != null) return Offer.parseString(searchResult);
        else return null;
    }
    
}
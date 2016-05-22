package application.controller;

/**
 * Created by lijiayu on 4/30/16.
 */
import application.gateway.ConnexityGateway;
import application.gateway.RedisGateway;
import application.model.AmazonOffer;
import application.model.Offer;
import application.search.SearchStrategy;
import application.gateway.AmazonGateway;
import application.gateway.HistoryGateway;
import application.utilities.Utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

import org.apache.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/home")
public class HomeController {
	@Autowired
    private SearchStrategy searchStrategy;
	@Autowired
    private HistoryGateway historyGateway;
	@Autowired
	private AmazonGateway amazonGateway;
    @Autowired
    private RedisGateway redisGateway;
    @Autowired
    private ConnexityGateway connexityGateway;

    static private final int numberOfRecommendation = 10;
    static private Logger logger = Logger.getLogger(HomeController.class.getName());

    @RequestMapping("")
    public String HomeController(@CookieValue(value="ConnexityUserID", required=false, defaultValue="connexityuserid") String cookieValue, @CookieValue(value="ConnexityUserCategory", required=false, defaultValue="connexityusercategory") String categoryCookieValue, Model model, HttpServletResponse response) throws IOException, InvalidKeyException, NoSuchAlgorithmException{
        List<Offer> historyOffers = new ArrayList();
        List<AmazonOffer> amazonOffers = new ArrayList();
        List<Offer> trendOffers = redisGateway.getTopTrends();
        if (cookieValue.equals("connexityuserid")) {
            String uniqueID = UUID.randomUUID().toString();
            Cookie cookie = new Cookie("ConnexityUserID", uniqueID);
            cookie.setMaxAge(Utility.TenYearInSeconds);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        else {
                List<Map<String, String>> historyResult = historyGateway.findHistory(cookieValue);
                if (historyResult != null) {
                    for (Iterator it = historyResult.iterator(); it.hasNext();) {
                        Map<String, String> history = (Map<String, String>) it.next();
                        List<Offer> historyOffer = searchStrategy.historySearch(history);
                        if (historyOffer != null) historyOffers.addAll(historyOffer);
                    }
                }
                //from historyOffers to amazonOffers 
                List<String> ASINs = new ArrayList<>();
                Iterator it = historyOffers.iterator();
                while (it.hasNext()) {
                		Offer history = (Offer) it.next();
                		String asin = amazonGateway.lookupASINbyUPCorSKU(history.upc, history.sku);
                		if (asin != null) ASINs.add(asin);
                }  
                String xml = amazonGateway.similarityLookupByASIN(ASINs);
                if(xml != null){
                    amazonOffers.addAll(AmazonOffer.parseString(xml));
                }
            //for cases that recommendation has no element either due to amazon error
            //or first time login
            if(amazonOffers.size() == 0) {
                String key = null;
                if(categoryCookieValue.equals("tech")){
                    key = "laptop";
                }
                else if(categoryCookieValue.equals("book")){
                    key = "book mark";
                }
                else if(categoryCookieValue.equals("outdoor")){
                    key = "mountain climb";
                }
                else if(categoryCookieValue.equals("fashion")) {
                    key = "fashion";
                }
                else{
                    logger.error("Category cookie not recognized");
                }

                if(key != null) {
                    String result = connexityGateway.getByKeyWord(key);
                    amazonOffers = convertConnexityOfferToAmazonOffer(result);
                }
            }
        }
        model.addAttribute("historyOffers", historyOffers);
        model.addAttribute("amazonOffers", amazonOffers);
        model.addAttribute("trendOffers", trendOffers);

        return "home";
    }

    private List<AmazonOffer> convertConnexityOfferToAmazonOffer(String input) throws IOException {
        List<Offer> offers = Offer.parseString(input);
        int endIndex = offers.size() > numberOfRecommendation ? numberOfRecommendation : offers.size();
        offers = offers.subList(0,endIndex);
        List<AmazonOffer> result = new ArrayList<>();
        for(Offer offer:offers) {
            AmazonOffer amazonOffer = new AmazonOffer();
            amazonOffer.price = offer.price.toString();
            amazonOffer.url = offer.url.toString();
            amazonOffer.title = offer.title;
            for(URL url : offer.images) {
                amazonOffer.images.add(url.toString());
            }
            result.add(amazonOffer);
        }
        return result;
    }
}

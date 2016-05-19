package application.controller;

/**
 * Created by lijiayu on 4/30/16.
 */
import application.model.AmazonOffer;
import application.model.Offer;
import application.search.SearchStrategy;
import application.gateway.AmazonGateway;
import application.gateway.HistoryGateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
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

    @RequestMapping("")
    public String HomeController(@CookieValue(value="ConnexityUserID", required=false, defaultValue="connexityuserid") String cookieValue, Model model, HttpServletResponse response) throws IOException, InvalidKeyException, NoSuchAlgorithmException{
        List<Offer> historyOffers = new ArrayList();
        if (cookieValue.equals("connexityuserid")) {
            String uniqueID = UUID.randomUUID().toString();
            Cookie cookie = new Cookie("ConnexityUserID", uniqueID);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        else {
            //    model.addAttribute("history",getUserHistoryFromDB());
                // written by xiangning on 5/12
                List<Map<String, String>> historyResult = historyGateway.findHistory(cookieValue);
                if (historyResult != null) {
                    for (Iterator it = historyResult.iterator(); it.hasNext();) {
                        Map<String, String> history = (Map<String, String>) it.next();
                        List<Offer> historyOffer = searchStrategy.historySearch(history);
                        if (historyOffer != null) historyOffers.addAll(historyOffer);
                    }
                }
        }
        model.addAttribute("historyOffers", historyOffers);
        //below: check amazon return results
        List<String> list = new ArrayList<>();
        list.add("B000W7JWUA");
        String xml = amazonGateway.similarityLookupByASIN(list);
        List<AmazonOffer> amazonOffers = AmazonOffer.parseString(xml);
        model.addAttribute("amazonOffers", amazonOffers);
        return "home";
    }
}

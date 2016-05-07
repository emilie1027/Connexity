package application.controller;

import application.model.Offer;
import application.model.Query;
import application.search.SearchStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/search")
public class SearchController {
    
    @Autowired
    private SearchStrategy searchStrategy;
    
    @RequestMapping("/*")
    public String search(@RequestParam(value = "key", required = false, defaultValue = "World") String key, Model model) throws IOException {
        List<Offer> searchResult = searchStrategy.basicSearch(key);
        model.addAttribute("offers", searchResult);
        return "query";
    }
    
    public String search(@ModelAttribute("Query") Query query, Model model) throws IOException{
        return search(query.getSingleParam("keyword"), model);
    }

    @RequestMapping("/redirect")
    public String redirect(@RequestParam Map<String, String> requestParams, @CookieValue(value="ConnexityUserID", required=false, defaultValue="connexityuserid") String cookieValue, HttpServletRequest request, HttpServletResponse response) throws Exception{
        //String redirectUrl = request.getScheme() + "://www.google.com";
        String redirectUrl = request.getScheme() + requestParams.get("url");
        String sku = requestParams.get("sku");
        String upc = requestParams.get("upc");
        String merchantId = requestParams.get("merchantId");

        if (cookieValue.equals("connexityuserid")) {
            String uniqueID = UUID.randomUUID().toString();
            response.addCookie(new Cookie("ConnexityUserID", uniqueID));
        }
        // putUserHistoryIntoDB(cookieValue, sku, upc, mercharId);

        return "redirect:" + redirectUrl;
    }
    
}

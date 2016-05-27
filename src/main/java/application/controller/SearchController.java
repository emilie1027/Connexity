package application.controller;

import application.gateway.RedisGateway;
import application.model.Offer;
import application.search.SearchStrategy;
import application.gateway.HistoryGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
    @Autowired
    private HistoryGateway historyGateway;
    @Autowired
    private RedisGateway redisGateway;
    @RequestMapping("/*")
    /*
    public String search(@RequestParam(value = "key", required = false, defaultValue = "World") String key, Model model) throws IOException {
        List<Offer> searchResult = searchStrategy.basicSearch(key);
        model.addAttribute("offers", searchResult);
        return "query";
    }
    */
    /*
    public String search(@ModelAttribute("Query") Query query, Model model) throws IOException{
        return search(query.getSingleParam("keyword"), model);
    }
    */
    public String advancedSearch(@RequestParam Map<String, String> requestParams, Model model) throws IOException {
        List<Offer> searchResult = searchStrategy.advancedSearch(requestParams);
        model.addAttribute("offers", searchResult);
        return "query";
    }

    @RequestMapping("/redirect")
    public String redirect(@RequestParam Map<String, String> requestParams, @CookieValue(value="ConnexityUserID", required=false, defaultValue="connexityuserid") String cookieValue, HttpServletRequest request, HttpServletResponse response) throws Exception{
        String redirectUrl = requestParams.get("url");
        String sku = requestParams.get("sku");
        String upc = requestParams.get("upc");
        String merchantId = requestParams.get("merchantId");

        if (cookieValue.equals("connexityuserid")) {
            String uniqueID = UUID.randomUUID().toString();
            response.addCookie(new Cookie("ConnexityUserID", uniqueID));
        }
        else {
        		historyGateway.insertHistory(cookieValue, upc, sku, merchantId);
        }
        redisGateway.insertRecord(upc,sku, merchantId);
        return "redirect:" + redirectUrl;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException e) {
        if (e.getMessage().equals("search key should not be empty nor null")) {
            return "redirect:/";
        }
        throw e;
    }
}

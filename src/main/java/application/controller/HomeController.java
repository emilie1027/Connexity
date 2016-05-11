package application.controller;

/**
 * Created by lijiayu on 4/30/16.
 */
import application.model.Offer;
import application.search.SearchStrategy;
import application.gateway.HistoryGateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/home")
public class HomeController {
	
	@Autowired
    private HistoryGateway historyGateway;

    @RequestMapping("")
    public String HomeController(@CookieValue(value="ConnexityUserID", required=false, defaultValue="connexityuserid") String cookieValue, Model model, HttpServletResponse response) throws IOException{
        if (cookieValue.equals("connexityuserid")) {
            String uniqueID = UUID.randomUUID().toString();
            response.addCookie(new Cookie("ConnexityUserID", uniqueID));
        }
        else {
            //    model.addAttribute("history",getUserHistoryFromDB());
        		List<Map<String, String>> historyResult = historyGateway.findHistory(cookieValue);
        		model.addAttribute("history", historyResult);	
        }
        return "home";
    }
}

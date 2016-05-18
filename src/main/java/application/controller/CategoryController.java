package application.controller;
import application.model.Offer;
import application.search.SearchStrategy;
import application.gateway.ConnexityGateway;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/category")
public class CategoryController {
    @RequestMapping("")
    public String category() throws IOException{
        return "category";
    }

    @RequestMapping("/getCategory")
    public String addUserCategroy(@RequestParam(value = "category", required = false, defaultValue = "default") String category, @CookieValue(value="ConnexityUserCategory", required=false, defaultValue="connexityusercategory") String cookieValue,HttpServletResponse response) throws IOException{
        if ("connexityusercategory".equals(cookieValue) || !cookieValue.equals(category)) {
            response.addCookie(new Cookie("ConnexityUserCategory", category));
        }
        return "category";
    }
}




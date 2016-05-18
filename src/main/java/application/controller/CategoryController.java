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
    public String category(@RequestParam(value = "category", required = false, defaultValue = "default") String category) throws IOException{
        return "category";
    }

    @RequestMapping("/getCategory")
    public String addUserCategory(@RequestParam(value = "category", required = false, defaultValue = "default") String category) throws IOException{
        return "category";
    }

}




package application.controller;

/**
 * Created by lijiayu on 4/30/16.
 */
import application.model.Offer;
import application.search.SearchStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {

    @RequestMapping("")
    public String HomeController(){
        return "home";
    }
}

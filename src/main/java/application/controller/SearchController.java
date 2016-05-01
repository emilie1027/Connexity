package application.controller;

import application.model.Offer;
import application.model.Query;
import application.search.SearchStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchStrategy searchStrategy;
    private List<Offer> searchResult;
    private String currentKey;

    public SearchController() {
        searchResult = new ArrayList<Offer>();
        currentKey = "";
    }

    @RequestMapping("/*")
    public String search(@RequestParam(value = "key", required = false, defaultValue = "World") String key, Model model) throws IOException {
        searchResult = searchStrategy.basicSearch(key);
        currentKey = key;
        model.addAttribute("offers", searchResult);
        return "query";
    }

    public String search(@ModelAttribute("query") Query query, Model model) throws IOException{
        if (query.getKey() == currentKey) {
            return "query";
        }
        else {
            return search(query.getKey(), model);
        }
    }

}

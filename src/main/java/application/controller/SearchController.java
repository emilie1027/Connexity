package application.controller;

import application.search.SearchStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchStrategy searchStrategy;

    @RequestMapping("/*")
    public String search(@RequestParam(value = "key", required = false, defaultValue = "World") String key, Model model) {
        String searchResult = searchStrategy.search(key);
        model.addAttribute("key", searchResult);
        return "home";
    }

}

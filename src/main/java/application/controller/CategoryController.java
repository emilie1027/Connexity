package application.controller;

import application.utilities.Utility;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;


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
            Cookie cookie = new Cookie("ConnexityUserCategory", category);
            cookie.setMaxAge(Utility.TenYearInSeconds);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        return "category";
    }
}




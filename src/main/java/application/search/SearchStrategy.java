package application.search;

import org.springframework.stereotype.Component;

@Component
public class SearchStrategy {
    public String search(String key) {
        return key;
    }
}

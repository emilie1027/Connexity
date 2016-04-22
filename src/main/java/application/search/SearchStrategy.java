package application.search;

import application.gateway.ConnexityGateway;
import application.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SearchStrategy {
    @Autowired
    private ConnexityGateway connexityGateway;
    public List<Product> search(String key) {
        return null;
    }
}
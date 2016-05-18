package application.gateway;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import static com.mongodb.client.model.Filters.*;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lijiayu on 5/17/16.
 */

/*
user = {
    _id:
    cookies: []
    email:
    category: []
    history: {

    }
}
 */

/*
@Component
public class UserGateway {
    @Value("${mongo.db}")
    private String db;

    private MongoCollection<Document> collection;

    @PostConstruct
    public void init() {
        MongoClient mongoClient = DBManager.INSTANCE.getClient();
        //MongoClient mongoClient = new MongoClient( host , port);
        collection = mongoClient.getDatabase(db).getCollection("user");
    }

    public void insertCategory(String id, List<String> categories) {
        if(id == null)
            throw new NullPointerException("id cannot be null");
        if(categories == null)
            throw new NullPointerException("user category has to be specified!");
        //create and insert a new doc if id does not exist
        if (findCategory(id) == null) {
            Document doc = new Document("_id", id).append("categories", categories);
            collection.insertOne(doc);
        }
    }

    public List<String> findCategory(String id){
        if(id == null)
            throw new NullPointerException("id cannot be null");
        Document result = collection.find(eq("_id", id)).first();

        if (result == null)
            return null;
        else {
            return result.get("categories", List.class);
        }
    }

    public void deleteCategory(){

    }

}
*/

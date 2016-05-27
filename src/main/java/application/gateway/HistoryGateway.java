package application.gateway;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import static com.mongodb.client.model.Filters.*;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static java.util.Arrays.asList;


@Component
public class HistoryGateway {
    @Value("${mongo.uri}")
    private String mongodbURI;
    @Value("${mongo.db}")
    private  String db;
    @Value("${history.maxNumberOfRecord}")
    private  int maxNumberOfRecord;

    private MongoCollection<Document> collection;
    public enum Option {
        //all the history related with an id
        All,
        //earliest history related with an id
        Earliest,
        //latest history related with an id
        Latest}

    @PostConstruct
    public void init() {
        MongoClientURI uri = new MongoClientURI(mongodbURI);
        MongoClient mongoClient = new MongoClient(uri);
        collection = mongoClient.getDatabase(db).getCollection("history");
    }

    public void insertHistory(String id, String upc, String sku, String merchantId) {
        List<Map<String, String>> result;
        if(id == null)
            throw new NullPointerException("id cannot be null");
        if(upc == null && !(sku != null && merchantId != null))
            throw  new NullPointerException("upc or sku and merchantId have to be specified!");
        upc = (upc == null? "" : upc);
        sku = (sku == null? "" : sku);
        merchantId = (merchantId == null? "" : merchantId);

        //create and insert a new doc if id does not exist
        if((result = findHistory(id)) == null) {
            Document doc = new Document("_id",id)
                                .append("history", asList(new Document()
                                        .append("upc", upc)
                                        .append("sku", sku)
                                        .append("merchantId", merchantId)));
           collection.insertOne(doc);
        }
        else if (recordExist(id,upc,sku,merchantId)) {
            return;
        }
        //directly insert into the history array if maximum number of record not reached
        else if (result.size() < maxNumberOfRecord){
            Document newHistory = new Document()
                                        .append("upc", upc)
                                        .append("sku", sku)
                                        .append("merchantId", merchantId);
            collection.updateOne(eq("_id", id), new Document("$push", new Document("history", newHistory)));
        }
        //pop the earliest history first, then push the new history
        else {
            deleteHistory(id, Option.Earliest);
            Document newHistory = new Document()
                    .append("upc", upc)
                    .append("sku", sku)
                    .append("merchantId", merchantId);
            collection.updateOne(eq("_id", id), new Document("$push", new Document("history", newHistory)));
        }

    }

    private boolean recordExist(String id, String upc, String sku, String merchantId) {
        List<Map<String, String>> results = findHistory(id);
        for(Map<String, String> result:results) {
            if(result.get("upc").equals(upc) && result.get("sku").equals(sku) && result.get("merchantId").equals(merchantId)) {
                return true;
            }
        }
        return false;
    }

    public List<Map<String, String>> findHistory(String id) {
        if(id == null)
            throw new NullPointerException("id cannot be null");
         Document result = collection.find(eq("_id", id)).first();

        if(result == null)
            return null;
        else{
            return result.get("history", ArrayList.class);
        }
    }

    public void deleteHistory(String id, Option option) {
        if(option == Option.All) {
            collection.updateOne(eq("_id", id), new Document("$set",new Document("history", new ArrayList<Document>())));
        }
        else if(option == Option.Earliest) {
            collection.updateOne(eq("_id", id), new Document("$pop", new Document("history", -1)));
        }
        else{
            collection.updateOne(eq("_id", id), new Document("$pop", new Document("history", 1)));
        }
    }

    public void deleteId(String id) {
        collection.deleteOne(eq("_id", id));
    }
}

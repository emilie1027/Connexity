
package application.gateway;

import com.mongodb.MongoClient;
import com.sun.istack.internal.Nullable;
import groovy.lang.Singleton;

import org.springframework.beans.factory.annotation.Value;


/**
 * Created by lijiayu on 5/17/16.
 */
@Singleton
public enum DBManager {
    INSTANCE;
    private MongoClient mongoClient = null;
    @Value("${mongo.address}")
    private String host = "localhost";
    @Value("${mongo.port}")
    private int port = 27017;
    @Value("${mongo.db}")
    private  String db;

    private DBManager() {
        try {
            if (mongoClient == null) {
                mongoClient = new MongoClient(host , port);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    public MongoClient getClient() {
        return mongoClient;
    }

    public void closeClient() {
        mongoClient.close();
    }
}

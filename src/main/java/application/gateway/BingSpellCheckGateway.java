package application.gateway;

import com.mongodb.util.JSON;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

/**
 * Created by lijiayu on 5/27/16.
 */

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = application.Application.class, loader = AnnotationConfigContextLoader.class)

@Component
public class BingSpellCheckGateway {
    @Value("${Bing.subscriptionKey}")
    private String subscriptionKey;
    @Value("${Bing.mode}")
    private String queryMode;

    private static JSONParser jsonParser = new JSONParser();

    public String bingSpellCheck(String input) {
        //String input = "micheal kors wemen handba";
        HttpClient httpclient = HttpClients.createDefault();

        try {
            URIBuilder builder = new URIBuilder("https://bingapis.azure-api.net/api/v5/spellcheck");

            builder.setParameter("mode", queryMode);

            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);
            request.setHeader("Content-Type", "application/x-www-form-urlencoded");
            request.setHeader("Ocp-Apim-Subscription-Key", subscriptionKey);


            // Request body

            StringEntity reqEntity = new StringEntity("text=" + input);
            request.setEntity(reqEntity);

            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                String suggestionResponse = EntityUtils.toString(entity);
                String suggestionString = getSuggestion(input, suggestionResponse);
                return suggestionString;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return input;
    }

    private String getSuggestion(String input, String response) throws ParseException {
        //String input = "micheal kors wemen handba";
        //String response = "{\"_type\": \"SpellCheck\", \"flaggedTokens\": [{\"offset\": 0, \"token\": \"micheal\", \"type\": \"UnknownToken\", \"suggestions\": [{\"suggestion\": \"michael\", \"score\": 1}]}, {\"offset\": 13, \"token\": \"wemen\", \"type\": \"UnknownToken\", \"suggestions\": [{\"suggestion\": \"women\", \"score\": 1}]}, {\"offset\": 19, \"token\": \"handba\", \"type\": \"UnknownToken\", \"suggestions\": [{\"suggestion\": \"handbag\", \"score\": 1}]}]}";

        JSONObject responseJson = (JSONObject) jsonParser.parse(response);
        if (responseJson != null) {
            JSONArray flaggedTokens = (JSONArray) responseJson.get("flaggedTokens");
            if (flaggedTokens.size() != 0){
                StringBuilder sb = new StringBuilder(input);
                int overallOffset = 0;
                for (int i = 0 ; i < flaggedTokens.size(); i++) {
                    JSONObject flaggedToken = (JSONObject) flaggedTokens.get(i);
                    int offset = (int)(long)((Long)(flaggedToken.get("offset")))+ overallOffset;
                    String token = (String) flaggedToken.get("token");
                    JSONArray suggestions = (JSONArray) flaggedToken.get("suggestions");
                    if (suggestions.size() != 0) {
                        String suggestion = (String) ((JSONObject)suggestions.get(0)).get("suggestion");
                        sb.replace(offset, offset + token.length(), suggestion);
                        overallOffset = suggestion.length()-token.length();
                    }
                }
                return sb.toString();
            }
        }
        return input;
    }
}

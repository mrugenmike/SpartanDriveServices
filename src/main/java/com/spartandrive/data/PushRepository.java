package com.spartandrive.data;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;

import static com.spartandrive.data.SharedFile.SHARED_WITH_EMAIL;
import static com.spartandrive.data.UserProfile.*;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

/**
 * Created by mrugen on 11/21/15.
 */
@Repository
public class PushRepository {


    @Autowired
    TransportClient client;

    private static String USER = "user";
    private static String PROFILE = "profile";

    /***
     * @return DocumentId from the index.
     */
    public String saveToken(String uid, String pushToken, String email, String firstName) throws IOException {
        final IndexResponse indexResponse = client.prepareIndex(USER, PROFILE, uid)
                .setSource(
                        jsonBuilder()
                                .startObject()
                                .field(USER_ID.toString(), uid)
                                .field(USER_ANDROID_TOKEN.toString(), pushToken)
                                .field(USER_EMAIL.toString(), email)
                                .field(FIRST_NAME.toString(), firstName)
                                .endObject()
                ).get();
        return indexResponse.getId();
    }

    /**
     * @return String - a Push Notification Token from GCM.
     **/
    public PushProfile findPushToken(String emailId) {
        final SearchResponse searchResponse = client.prepareSearch(USER)
                .setQuery(QueryBuilders.boolQuery().filter(matchQuery(USER_EMAIL.toString(), emailId.trim())))
                .get();
        final SearchHits hits = searchResponse.getHits();
        if (hits != null && hits.getTotalHits() > 0) {
            final SearchHit hit = hits.hits()[0];
            System.out.println(hit.getSource());
            final String token = hit.getSource().get(USER_ANDROID_TOKEN.toString()).toString();
            System.out.println("token="+token);
            final String email = hit.getSource().get(USER_EMAIL.toString()).toString();
            System.out.println("email="+email);
            final String firstName = hit.getSource().get(FIRST_NAME.toString()).toString();
            System.out.println("name="+firstName);
            final String userId = hit.getSource().get(USER_ID.toString()).toString();

            return new PushProfile(token,email,firstName,userId);
        }
        return null;
    }
}

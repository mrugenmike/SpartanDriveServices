package com.spartandrive.data;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;

import static com.spartandrive.data.UserProfile.*;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

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
     *@return DocumentId from the index.
     */
    public String saveToken(String uid, String pushToken, String email) throws IOException {
        final IndexResponse indexResponse = client.prepareIndex(USER, PROFILE, uid)
                .setSource(
                        jsonBuilder()
                                .startObject()
                                .field(USER_ID.toString(), uid)
                                .field(USER_ANDROID_TOKEN.toString(), pushToken)
                                .field(USER_EMAIL.toString(), email)
                                .endObject()
                ).get();
        return indexResponse.getId();
    }
}

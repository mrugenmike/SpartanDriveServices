package com.spartandrive.services;

import com.spartandrive.data.PushRepository;
import com.spartandrive.web.PushDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by mrugen on 11/21/15.
 */
@Service
public class PushService {

    @Autowired
    PushRepository repository;

    /**
     * @return Unique identifier from the storage layer.
     * @throws PushException if fails to save data in storage.
     * */
    public String saveOrUpdateToken(String uid, PushDetail pushToken) throws PushException{
        try {
            return repository.saveToken(uid,pushToken.getAndroidToken(),pushToken.getEmailId());
        } catch (IOException e) {
            throw new PushException("Failed to process the PushToken for userId:"+uid);
        }
    }
}

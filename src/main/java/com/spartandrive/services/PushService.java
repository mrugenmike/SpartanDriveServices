package com.spartandrive.services;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Sender;
import com.spartandrive.data.PushProfile;
import com.spartandrive.data.PushRepository;
import com.spartandrive.web.request.PushDetail;
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

    @Autowired
    Sender sender;

    /**
     * @return Unique identifier from the storage layer.
     * @throws PushException if fails to save data in storage.
     */
    public String saveOrUpdateToken(String uid, PushDetail pushToken) throws PushException {
        try {
            return repository.saveToken(uid, pushToken.getAndroidToken(), pushToken.getEmailId(), pushToken.getFirstName());
        } catch (IOException e) {
            throw new PushException("Failed to process the PushToken for userId:" + uid);
        }
    }

    public void sendFileSharedPush(String filename, String ownerEmail, String sharedWithEmail) throws IOException {
        final PushProfile pushToken = repository.findPushToken(sharedWithEmail);
        if (pushToken != null) {
            final Message message = new Message.Builder()
                    .addData(PushMessageFields.TITLE, "File Shared")
                    .addData(PushMessageFields.TEXT, String.format("%s shared %s",ownerEmail,filename))
                    .build();
            sender.send(message, pushToken.getToken(), 3);
        }
    }

    public void sendUnsharedFilePush(String emailId, String filename, String ownerEmail) throws IOException, PushTokenNotFoundException {
        final PushProfile pushToken = repository.findPushToken(emailId);
        if (pushToken != null) {
            final Message message = new Message.Builder()
                    .addData(PushMessageFields.TITLE, "File Unshared")
                    .addData(PushMessageFields.TEXT,String.format("%s unshared %s",ownerEmail,filename))
                    .build();
            sender.send(message, pushToken.getToken(), 3);
        } else {
            throw new PushTokenNotFoundException("No Push Token found for the user with email " + emailId);
        }
    }
}

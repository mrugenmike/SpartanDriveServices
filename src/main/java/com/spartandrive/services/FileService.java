package com.spartandrive.services;

import com.spartandrive.data.FailedToDeleteFileException;
import com.spartandrive.data.FileRepository;
import com.spartandrive.web.request.SharedFileDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Created by mrugen on 11/28/15.
 */
@Service
public class FileService {
    @Autowired
    FileRepository fileRepository;

    @Autowired
    PushService pushService;

    /**
     * @return sharedFileIndexDocumentID
     * @Throws FileException if underlying data store fails to store the fileDetails.
     */
    public String shareFile(SharedFileDetail sharedFileDetail) {
        try {
            final String sharedFileDocId = fileRepository.saveFileDetails(sharedFileDetail);
            if (sharedFileDetail != null) {
                pushService.sendFileSharedPush(sharedFileDetail.getPath(), sharedFileDetail.getOwnerEmail(), sharedFileDetail.getSharedWithEmail());
            }
            return sharedFileDocId;
        } catch (IOException e) {
            throw new FileException(e.getMessage());
        }
    }

    public List<SharedFileDetail> fetchSharedFiles(String email) {
        return fileRepository.findBySharedEmail(email);
    }

    public void unshareFile(String emailId, String filePath, String ownerEmailId) throws UnshareException {
        try {
            final boolean deleted = fileRepository.deleteSharedFileRecord(emailId, filePath);
            if (deleted) {
                pushService.sendUnsharedFilePush(emailId, filePath, ownerEmailId);
            }
        } catch (FailedToDeleteFileException e) {
            throw new UnshareException(e.getMessage());
        } catch (IOException e) {
            throw new PushException("Failed to send Unshare Notification to user with email" + emailId);
        } catch (PushTokenNotFoundException e) {
            e.printStackTrace();
        }
    }
}

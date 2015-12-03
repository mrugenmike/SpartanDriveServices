package com.spartandrive.services;

import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;
import com.spartandrive.data.FailedToDeleteFileException;
import com.spartandrive.data.FileRepository;
import com.spartandrive.web.request.SharedFileDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Autowired
    SendGrid sendGrid;

    @Value("${sendgrid.emailTemplate}")
    String shareEmailTemplate;

    @Value("${sendgrid.emailTemplate.unshared}")
    String unsharedTemplate;

    /**
     * @return sharedFileIndexDocumentID
     * @Throws FileException if underlying data store fails to store the fileDetails.
     */
    public String shareFile(SharedFileDetail sharedFileDetail) throws Exception{
        try{
            final String sharedFileDocId = fileRepository.saveFileDetails(sharedFileDetail);
            try {
                if (sharedFileDocId != null) {
                    final SendGrid.Email email = getEmail(sharedFileDetail);
                    sendGrid.send(email);
                    pushService.sendFileSharedPush(sharedFileDetail.getFileName(), sharedFileDetail.getOwnerEmail(), sharedFileDetail.getSharedWithEmail());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sharedFileDocId;
        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    private SendGrid.Email getEmail(SharedFileDetail sharedFileDetail) {
        final SendGrid.Email email = new SendGrid.Email();
        email.setTo(new String[]{sharedFileDetail.getSharedWithEmail()});
        email.setSubject("File Shared");
        email.setTemplateId(shareEmailTemplate);
        email.setFrom(sharedFileDetail.getOwnerEmail());
        final String html = String.format("%s shared<b> %s</b>, and is now accessible from SpartanDrive app <b>\"Shared with Me\"</b> section", sharedFileDetail.getOwnerEmail(), sharedFileDetail.getFileName());
        email.setHtml(html);
        return email;
    }

    public List<SharedFileDetail> fetchSharedFilesFor(String email) {
        return fileRepository.findBySharedWith(email);
    }

    public void unshareFile(String emailId, String filePath, String ownerEmailId) throws UnshareException {
        try {
            final boolean deleted = fileRepository.deleteSharedFileRecord(emailId, filePath);
            if (deleted) {
                final SendGrid.Email email = new SendGrid.Email();
                email.setTo(new String[]{emailId});
                email.setSubject("File Unshared -"+filePath);
                email.setTemplateId(unsharedTemplate);
                email.setFrom(ownerEmailId);
                final String html = String.format("%s Unshared file at <b> %s </b> with you.<b>",ownerEmailId, filePath);
                email.setHtml(html);
                sendGrid.send(email);
                pushService.sendUnsharedFilePush(emailId, filePath, ownerEmailId);
            }
        } catch (FailedToDeleteFileException e) {
            throw new UnshareException(e.getMessage());
        } catch (IOException e) {
            throw new PushException("Failed to send Unshare Notification to user with email" + emailId);
        } catch (PushTokenNotFoundException e) {
            e.printStackTrace();
        } catch (SendGridException e) {
            e.printStackTrace();
        }
    }

    public List<SharedFileDetail> fetchSharedFilesBy(String uid, String filePath) {
        return fileRepository.findSharedByUser(uid,filePath);
    }
}

package com.spartandrive.web;

import com.spartandrive.services.FileService;
import com.spartandrive.services.UnshareException;
import com.spartandrive.web.request.SharedFileDetail;
import com.spartandrive.web.request.UnshareFileRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by mrugen on 11/28/15.
 */
@RestController
public class FilesResource {

    @Autowired
    FileService fileService;

    @RequestMapping(value = "/files/shared", method = RequestMethod.POST)
    public ResponseEntity<String> shareFile(@RequestBody @Valid SharedFileDetail sharedFileDetail) {

        try {
            final String documentId = fileService.shareFile(sharedFileDetail);
            if(documentId!=null){
                return new ResponseEntity<String>(String.format("{\"docId\":\"%s\"}", documentId), HttpStatus.CREATED);
            } else{
                return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<String>(String.format("{\"error\":\"%s\"}", e.getMessage()),HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @RequestMapping(value = "/files/shared", method = RequestMethod.GET)
    public ResponseEntity<List<SharedFileDetail>> findAllSharedFiles(@RequestParam("emailId") String email) {
        final List<SharedFileDetail> sharedFiles = fileService.fetchSharedFiles(email);
        if (sharedFiles!=null)
            return new ResponseEntity<>(sharedFiles, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/files/shared", method = RequestMethod.DELETE)
    public ResponseEntity<String> unshareFile(@Valid @RequestBody UnshareFileRequest unshareFileRequest) throws UnshareException {
        fileService.unshareFile(unshareFileRequest.getSharedWithEmailId(), unshareFileRequest.getFilePath(), unshareFileRequest.getOwnerEmailId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

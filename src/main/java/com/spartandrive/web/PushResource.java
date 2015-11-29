package com.spartandrive.web;

/**
 * Created by mrugen on 11/20/15.
 */

import com.spartandrive.services.PushService;
import com.spartandrive.web.request.PushDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class PushResource {

    @Autowired
    private PushService pushService;

    @RequestMapping(value = "/push/{uid}", method = RequestMethod.PUT)
    public ResponseEntity<String> fetchToken(@PathVariable String uid, @RequestBody @Valid PushDetail detail) {
        final String docId = pushService.saveOrUpdateToken(uid, detail);
        return new ResponseEntity<>(String.format("{\"docId\":\"%s\"}", docId), HttpStatus.OK);
    }

}

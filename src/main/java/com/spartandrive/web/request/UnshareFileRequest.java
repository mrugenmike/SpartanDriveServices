package com.spartandrive.web.request;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by mrugen on 11/29/15.
 */
public class UnshareFileRequest {
    @NotBlank
    String filePath;

    @NotBlank
    String sharedWithEmailId;

    @NotBlank
    String ownerEmailId;

    public String getSharedWithEmailId() {
        return sharedWithEmailId;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getOwnerEmailId() {
        return ownerEmailId;
    }
}

package com.spartandrive.web.request;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Collections;
import java.util.Map;

import static com.spartandrive.data.SharedFile.*;

/**
 * Created by mrugen on 11/28/15.
 */
public class SharedFileDetail {
    @NotEmpty
    String sharedFileUrl;

    @NotEmpty
    String sharedWithEmail;

    @NotBlank
    String ownerUID;

    @NotEmpty
    String ownerEmail;

    Boolean isDirectory;

    Map<String, Object> directoryContents;

    String mimeType;

    String path;

    String icon;

    String fileName;

    String folderName;

    String size;

    String modified;

    public String getModified() {
        return modified;
    }

    public String getSize() {
        return size;
    }

    public SharedFileDetail() {
    }

    public SharedFileDetail(Map<String, Object> source) {
        this.ownerUID = source.get(OWNER_ID.toString()).toString();
        this.ownerEmail = source.get(OWNER_EMAIL.toString()).toString();
        this.isDirectory = (Boolean) source.get(IS_DIRECTORY.toString());
        this.path = source.get(PATH.toString()).toString();
        this.mimeType = source.get(MIME_TYPE.toString()).toString();
        this.icon = source.get(ICON.toString()).toString();
        this.fileName = source.get(FILE_NAME.toString()).toString();
        this.folderName = source.get(FOLDER_NAME.toString()).toString();
        this.sharedWithEmail = source.get(SHARED_WITH_EMAIL.toString()).toString();
        this.sharedFileUrl = source.get(SHARED_FILE_URL.toString()).toString();
        this.directoryContents = (Map<String, Object>) source.get(CONTENTS.toString());
        this.modified = source.get(MODIFIED.toString()).toString();
        this.size = source.get(SIZE.toString()).toString();
    }

    public String getSharedFileUrl() {
        return sharedFileUrl;
    }

    public String getSharedWithEmail() {
        return sharedWithEmail;
    }

    public String getOwnerUID() {
        return ownerUID;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public Boolean getIsDirectory() {
        return isDirectory;
    }

    public Map<String, Object> getDirectoryContents() {
        if (directoryContents != null) {
            return directoryContents;
        }
        return Collections.emptyMap();
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getPath() {
        return path;
    }

    public String getIcon() {
        return icon;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFolderName() {
        return folderName;
    }

}

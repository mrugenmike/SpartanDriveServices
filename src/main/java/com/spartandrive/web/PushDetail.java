package com.spartandrive.web;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by mrugen on 11/21/15.
 */
public class PushDetail {
    @NotEmpty
    private String androidToken;
    @NotEmpty
    private String emailId;

    public String getAndroidToken() {
        return androidToken;
    }

    public String getEmailId() {
        return emailId;
    }
}

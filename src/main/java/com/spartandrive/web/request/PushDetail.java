package com.spartandrive.web.request;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by mrugen on 11/21/15.
 */
public class PushDetail {
    @NotEmpty
    private String androidToken;
    @NotEmpty
    private String emailId;

    public String getFirstName() {
        return firstName;
    }

    @NotEmpty
    private String firstName;

    public String getAndroidToken() {
        return androidToken;
    }

    public String getEmailId() {
        return emailId;
    }
}

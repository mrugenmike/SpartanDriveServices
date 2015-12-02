package com.spartandrive.data;

/**
 * Created by mrugen on 12/1/15.
 */
public class PushProfile {

    public String getToken() {
        return token;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getUserId() {
        return userId;
    }

    private final String token;
    private final String email;
    private final String firstName;
    private final String userId;

    public PushProfile(String token, String email, String firstName, String userId) {

        this.token = token;
        this.email = email;
        this.firstName = firstName;
        this.userId = userId;
    }
}

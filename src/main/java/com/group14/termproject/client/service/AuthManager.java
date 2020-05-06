package com.group14.termproject.client.service;

public interface AuthManager {
    /**
     * Should be called whenever an HTTP request's response is considered as unauthorized.
     * For example, when HTTP's status code is 403 (Forbidden).
     */
    void onUnauthorizedRequest();

    /**
     * Should be called after login is successful to that {@code token} and {@code username} values can be persisted.
     *
     * @param token    JWT assigned to logged in user.
     * @param username username of the user.
     */
    void onSuccessfulLogin(String token, String username);

    void logout();

    String getUsername();

    String getToken();
}

package com.group14.termproject.client.service.http;

import com.group14.termproject.client.model.UserDTO;
import org.json.JSONObject;

public interface AuthService {
    /**
     * Sends a login request to the server.
     * <br>
     * On response, it invokes {@code handler}'s success callback with a {@link JSONObject} which contains only
     * with only one field named <b>token</b> whose value is a raw string.
     *
     * @param user    holds login information (username and password).
     * @param handler see {@link HttpResponseHandler}
     */
    void login(UserDTO user, HttpResponseHandler<JSONObject> handler);

    /**
     * Sends a register request to the server.
     * <br>
     * On response, {@code handler}'s success callback is invoked with a {@link JSONObject} which consists of one
     * field id whose type is {@link Long}.
     *
     * @param user    holds login information (username and password).
     * @param handler see {@link HttpResponseHandler}
     */
    void register(UserDTO user, HttpResponseHandler<JSONObject> handler);
}

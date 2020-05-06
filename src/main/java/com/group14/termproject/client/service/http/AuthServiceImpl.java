package com.group14.termproject.client.service.http;

import com.group14.termproject.client.model.UserDTO;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
class AuthServiceImpl extends HttpService implements AuthService {

    @Value("${custom.server.auth.api.uri}")
    private String authAPIUri;

    @Override
    public void login(UserDTO user, HttpResponseHandler<JSONObject> handler) {
        makeAuthRequest(user, handler, "login");
    }

    @Override
    public void register(UserDTO user, HttpResponseHandler<JSONObject> handler) {
        makeAuthRequest(user, handler, "register");
    }

    private void makeAuthRequest(UserDTO user, HttpResponseHandler<JSONObject> handler, String apiName) {
        var response = makeRequest(authAPIUri + apiName, HttpMethod.POST, new JSONObject(user), handler);
        tryInvokingSuccessCallback(response, handler);
    }

}

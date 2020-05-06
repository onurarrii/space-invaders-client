package com.group14.termproject.client.service.http;

import com.group14.termproject.client.service.AuthManager;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * <p>
 * Controls all HTTP requests and thus each and every HTTP request to game server must be sent over this class. It
 * intercepts the requests and put HTTP Authorization header if it has token.
 * <br>
 * If HTTP request's response is retrieved successfully, then handling procedure is delegated to implementer service
 * i.e. the class extending {@link HttpService}.
 * <br>
 * However, if an error occurs, then respective {@link HttpResponseHandler} object's error handler is invoked with
 * error message. The only exception is Forbidden error (HTTP 403). It is considered as either token exception or
 * unauthorized request thus, it results in navigation to login page.
 * </p>
 */
public abstract class HttpService {

    private static final String ERROR_MESSAGE_FIELD_NAME = "message";
    @Autowired
    AuthManager authManager;
    @Value("${custom.server.base.api.uri}")
    private String baseAPIUri;

    protected <T> ResponseEntity<String> makeRequest(String apiUri, HttpMethod method, JSONObject body,
                                                     HttpResponseHandler<T> handler) {
        HttpEntity<String> entity = createHttpEntity(body);
        RestTemplate restTemplate = getRestTemplate(handler);
        return restTemplate.exchange(this.baseAPIUri + apiUri, method, entity, String.class);
    }

    private <T> RestTemplate getRestTemplate(HttpResponseHandler<T> handler) {
        RestTemplate restTemplate = new RestTemplate();
        DefaultResponseErrorHandler errorHandler = new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                JSONObject bodyAsJSON = new JSONObject(new JSONTokener(response.getBody()));
                // If error code is 403, that means that the user is not logged in or token expired. Needs extra care.
                if (response.getStatusCode() == HttpStatus.FORBIDDEN)
                    authManager.onUnauthorizedRequest();
                else
                    handler.getOnErrorCallback().accept(bodyAsJSON.getString(ERROR_MESSAGE_FIELD_NAME));

            }
        };
        restTemplate.setErrorHandler(errorHandler);
        return restTemplate;
    }

    private HttpEntity<String> createHttpEntity(JSONObject body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String token = authManager.getToken();
        if (token != null) {
            headers.setBearerAuth(token);
        }
        return new HttpEntity<>(body.toString(), headers);
    }

    protected void tryInvokingSuccessCallback(ResponseEntity<String> response,
                                              HttpResponseHandler<JSONObject> handler) {
        if (response.getBody() != null)
            handler.getOnSuccessCallback().accept(new JSONObject(response.getBody()));
    }
}

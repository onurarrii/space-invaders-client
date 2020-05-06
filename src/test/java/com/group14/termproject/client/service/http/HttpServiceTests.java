package com.group14.termproject.client.service.http;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.function.Consumer;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class HttpServiceTests {

    @Mock
    HttpService httpService;
    @Mock
    ResponseEntity<String> responseEntity;
    @Mock
    Consumer<JSONObject> onSuccess;
    @Mock
    Consumer<String> onError;
    @Mock
    JSONObject mockJSON;
    String mockError;

    // Mock http request and response constants
    final String apiUri = "testAPI";
    final HttpMethod method = HttpMethod.POST;
    final JSONObject body = new JSONObject();

    @Before
    public void setup() {
        mockError = "A random string";
    }

    private HttpResponseHandler<JSONObject> generateHttpResponseHandler() {
        return new HttpResponseHandler.HttpResponseHandlerBuilder<JSONObject>()
                .withOnSuccess(onSuccess)
                .withOnError(onError)
                .build();
    }

    @Test
    public void testTryInvokingSuccessCallbackOnError() {
        httpService.tryInvokingSuccessCallback(responseEntity, generateHttpResponseHandler());
        // Since response is empty, success callback should not be called.
        verify(onSuccess, times(0)).accept(mockJSON);
    }

    @Test
    public void testTryInvokingSuccessCallbackOnSuccess() {
        var handler = generateHttpResponseHandler();
        when(responseEntity.getBody()).thenReturn("{}");
        responseEntity.getBody(); // Invoke method for Mock
        httpService.tryInvokingSuccessCallback(responseEntity, handler);
        // Valid response, error callback should not be invoked.
        verify(handler.getOnErrorCallback(), times(0)).accept(mockError);
    }

    @Test
    public void testMakeRequestSuccess() {
        String bodyAsString = "{\"message\": \"Success\"}";

        Consumer<JSONObject> asserter = json -> Assert.assertEquals(bodyAsString, json.toString());

        var handler = new HttpResponseHandler.HttpResponseHandlerBuilder<JSONObject>()
                .withOnSuccess(asserter)
                .build();

        when(responseEntity.getBody()).thenReturn(bodyAsString);
        when(httpService.makeRequest(apiUri, method, body, handler)).thenReturn(responseEntity);

        httpService.makeRequest(apiUri, method, body, handler);
    }

    @Test
    public void testMakeRequestOnError() {
        String errorMessage = "Error";
        String response = String.format("{\"message\": \"%s\"}", errorMessage);
        HttpStatus statusCode = HttpStatus.FORBIDDEN;

        var handler = new HttpResponseHandler.HttpResponseHandlerBuilder<JSONObject>()
                .withOnError(message -> Assert.assertEquals(errorMessage, message))
                .build();

        when(responseEntity.getStatusCode()).thenReturn(statusCode);
        when(responseEntity.getBody()).thenReturn(response);
        // Check if methods are overridden
        Assert.assertEquals(statusCode, responseEntity.getStatusCode());
        Assert.assertEquals(response, responseEntity.getBody());

        // Invoke error handler
        when(httpService.makeRequest(apiUri, method, body, handler)).thenReturn(responseEntity);
        httpService.makeRequest(apiUri, method, body, handler);
    }

}

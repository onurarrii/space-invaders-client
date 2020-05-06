package com.group14.termproject.client.service;

import com.group14.termproject.client.util.enums.UiPage;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.FieldSetter;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AuthManagerTests {

    private static String token, username;
    @InjectMocks
    AuthManagerImpl authManager;
    @Mock
    StageManager stageManager;

    @BeforeClass
    public static void setup() {
        // Assign random strings
        token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxODYwIiwiaWF0Ij";
        username = "onur";
    }

    private void checkNavigation(UiPage page) {
        verify(stageManager, times(1)).navigate(page);
    }

    private void testFields(String username, String token) {
        Assert.assertEquals(token, authManager.getToken());
        Assert.assertEquals(username, authManager.getUsername());
    }

    private void setFields(String username, String token) throws NoSuchFieldException {
        FieldSetter.setField(authManager, authManager.getClass().getDeclaredField("username"), username);
        FieldSetter.setField(authManager, authManager.getClass().getDeclaredField("token"), token);

    }

    @Test
    public void testOnUnauthorizedHTTPRequestLogout() throws NoSuchFieldException {
        setFields(username, token);
        // check if fields are set
        testFields(username, token);
        authManager.onUnauthorizedRequest();
        testFields(null, null);
    }

    @Test
    public void testSuccessfulLogin() {
        authManager.onSuccessfulLogin(token, username);
        testFields(username, token);
    }

    @Test
    public void testSuccessfulLoginNavigation() {
        authManager.onSuccessfulLogin(token, username);
        checkNavigation(UiPage.MAIN_MENU);
    }


    @Test
    public void testOnUnauthorizedHTTPRequestNavigation() {
        authManager.onUnauthorizedRequest();
        checkNavigation(UiPage.LOGIN);
    }

    @Test
    public void testValidLogout() throws NoSuchFieldException {
        setFields(username, token);
        // check if fields are set
        testFields(username, token);
        authManager.logout();
        testFields(null, null);
        checkNavigation(UiPage.LOGIN);
    }
}

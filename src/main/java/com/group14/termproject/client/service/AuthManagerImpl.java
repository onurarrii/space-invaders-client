package com.group14.termproject.client.service;

import com.group14.termproject.client.util.enums.UiPage;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Getter
@Service
final class AuthManagerImpl implements AuthManager {
    final StageManager stageManager;
    private String username;
    private String token;

    public AuthManagerImpl(StageManager stageManager) {
        this.stageManager = stageManager;
    }

    public void onUnauthorizedRequest() {
        this.username = this.token = null;
        stageManager.navigate(UiPage.LOGIN);
    }

    public void onSuccessfulLogin(String token, String username) {
        this.token = token;
        this.username = username;
        stageManager.navigate(UiPage.MAIN_MENU);
    }

    public void logout() {
        this.username = this.token = null;
        stageManager.navigate(UiPage.LOGIN);
    }
}

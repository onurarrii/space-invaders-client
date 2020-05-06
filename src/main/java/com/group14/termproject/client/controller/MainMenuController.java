package com.group14.termproject.client.controller;

import com.group14.termproject.client.service.AuthManager;
import com.group14.termproject.client.service.StageManager;
import com.group14.termproject.client.service.http.GameService;
import com.group14.termproject.client.service.http.HttpResponseHandler;
import com.group14.termproject.client.util.AlertUtil;
import com.group14.termproject.client.util.enums.UiPage;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class MainMenuController implements Initializable {

    final StageManager stageManager;
    final AuthManager authManager;
    final GameService gameService;

    public Button gameStartButton;
    public Button leaderboardButton;
    public Button logoutButton;

    public MainMenuController(StageManager stageManager, AuthManager authManager, GameService gameService) {
        this.stageManager = stageManager;
        this.authManager = authManager;
        this.gameService = gameService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void onStartGameClick() {
        var handler = new HttpResponseHandler.HttpResponseHandlerBuilder<JSONObject>()
                .withOnSuccess(o -> stageManager.openGamePage())
                .withOnError(AlertUtil::showError)
                .build();
        gameService.createRoom(handler);
    }

    public void onLeaderboardButtonClick() {
        stageManager.navigate(UiPage.LEADERBOARD);
    }

    public void onLogoutClick() {
        authManager.logout();
    }
}

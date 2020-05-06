package com.group14.termproject.client.service;

import com.group14.termproject.client.UiApplication;
import com.group14.termproject.client.controller.game.GameController;
import com.group14.termproject.client.util.enums.UiPage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
class StageManagerImpl implements StageManager, ApplicationListener<UiApplication.StageReadyEvent> {
    private final GameController gameController;
    private final ApplicationContext applicationContext;
    private final Map<UiPage, Resource> navigationMap = new HashMap<>();
    @Value("${custom.window.width}")
    private int windowWidth;
    @Value("${custom.window.height}")
    private int windowHeight;
    @Value("${custom.window.name}")
    private String applicationTitle;
    @Getter
    private Stage stage;

    public StageManagerImpl(ApplicationContext applicationContext,
                            @Lazy GameController gameController,
                            @Value("classpath:/ui-pages/auth-ui.fxml") Resource authUiResource,
                            @Value("classpath:/ui-pages/leaderboard-ui.fxml") Resource leaderboardUiResource,
                            @Value("classpath:/ui-pages/menu-ui.fxml") Resource menuUiResource) {
        this.applicationContext = applicationContext;

        navigationMap.put(UiPage.LOGIN, authUiResource);
        navigationMap.put(UiPage.LEADERBOARD, leaderboardUiResource);
        navigationMap.put(UiPage.MAIN_MENU, menuUiResource);
        this.gameController = gameController;
    }

    public void navigate(UiPage page) {
        Parent parent = loadFxml(page);
        stage.setScene(new Scene(parent, stage.getScene().getWidth(), stage.getScene().getHeight()));
    }

    public void openGamePage() {
        gameController.open();
    }


    @Override
    public void onApplicationEvent(UiApplication.StageReadyEvent stageReadyEvent) {
        stage = stageReadyEvent.getStage();

        Parent parent = loadFxml(UiPage.LOGIN);
        stage.setScene(new Scene(parent, windowWidth, windowHeight));
        stage.setTitle(applicationTitle);
        stage.show();
    }

    private Parent loadFxml(UiPage page) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(navigationMap.get(page).getURL());
            fxmlLoader.setControllerFactory(applicationContext::getBean);
            return fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

}
package com.group14.termproject.client.controller.game;

import com.group14.termproject.client.game.Vector2D;
import com.group14.termproject.client.model.GameStatusDTO;
import com.group14.termproject.client.service.AuthManager;
import com.group14.termproject.client.service.StageManager;
import com.group14.termproject.client.service.http.GameService;
import com.group14.termproject.client.service.http.HttpResponseHandler;
import com.group14.termproject.client.util.AlertUtil;
import com.group14.termproject.client.util.GameObjectDrawer;
import com.group14.termproject.client.util.GameUiUtil;
import com.group14.termproject.client.util.enums.UiPage;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Component
public class GameController {

    private final StageManager stageManager;
    private final GameService gameService;
    private final AuthManager authManager;
    boolean gameCompleted;
    private AnchorPane pane;
    private Canvas canvas;
    private List<ImageView> gameObjectSprites;
    private Vector2D mousePosition;
    @Value("${custom.game.status-request-delay}")
    private Long GAME_STATUS_REQUEST_DELAY;
    @Value("${custom.game.mouse-position-notify-delay}")
    private long MOUSE_POSITION_NOTIFY_DELAY;
    private GameInfoGUI gameInfoGUI;

    public GameController(GameService gameService, StageManager stageManager, AuthManager authManager) {
        this.gameService = gameService;
        this.stageManager = stageManager;
        this.authManager = authManager;
    }

    /**
     * <p>
     * Making a call to this method triggers {@link GameController} after which it initializes game page and shows it.
     * </p>
     *
     * <p>
     * Note that {@link GameController} is a singleton class and calling the method multiple times through different
     * objects is not possible. Yet, invoking the method multiple times in a single lifetime is perfectly fine. It
     * causes page elements to be reinitialized which means it will work just like the first time it called.
     * </p>
     */
    public void open() {
        this.gameObjectSprites = new ArrayList<>();
        this.mousePosition = new Vector2D();
        this.gameCompleted = false;
        createPane();
        createCanvas();
        this.gameInfoGUI = new GameInfoGUI(pane, canvas);
        bindEvents();
        // Programmatically invoke resize events to set GUI elements' position and get the first frame.
        Platform.runLater(this::handleResize);
    }

    private void createCanvas() {
        Stage stage = stageManager.getStage();
        canvas = new Canvas(stage.getWidth(), stage.getHeight());
        pane.getChildren().add(canvas);
    }

    private void createPane() {
        Stage stage = stageManager.getStage();
        Group root = new Group();
        pane = new AnchorPane();
        pane.setBackground(getPaneBackground());
        root.getChildren().add(pane);
        Scene scene = new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight());
        stage.setScene(scene);
    }

    private Background getPaneBackground() {
        String BACKGROUND_IMAGE_PATH = "/static/game-background.jpg";
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image(GameController.class.getResourceAsStream(BACKGROUND_IMAGE_PATH)),
                BackgroundRepeat.REPEAT,
                BackgroundRepeat.REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        return new Background(backgroundImage);
    }

    private void bindEvents() {
        bindReadyClickEvent();
        bindMouseMovementEvent();
        bindResizeEvents();
        bindCheatKeyEvent();
    }

    private void bindReadyClickEvent() {
        var handler = new HttpResponseHandler.HttpResponseHandlerBuilder<JSONObject>()
                .withOnSuccess(o -> {
                    gameInfoGUI.removeReadyButton();
                    onGameStart();
                })
                .withOnError(AlertUtil::showError)
                .build();
        gameInfoGUI.getReadyButton().setOnMouseClicked(mouseEvent -> gameService.startGame(handler));
    }

    private void bindMouseMovementEvent() {
        canvas.setOnMouseMoved(mouseEvent -> {
            Vector2D proportionedPosition = new Vector2D(mouseEvent.getX(), mouseEvent.getY());
            Vector2D actualPosition = GameUiUtil.getActualPosition(proportionedPosition, canvas);
            mousePosition.setX(actualPosition.getX());
            mousePosition.setY(actualPosition.getY());
        });
    }

    private void bindResizeEvents() {
        Stage currentStage = stageManager.getStage();

        InvalidationListener resizeListener = evt -> handleResize();
        currentStage.widthProperty().addListener(resizeListener);
        currentStage.heightProperty().addListener(resizeListener);
    }

    private void handleResize() {
        Stage currentStage = stageManager.getStage();
        canvas.setHeight(currentStage.getHeight());
        canvas.setWidth(currentStage.getWidth());
        renderScene();
        gameInfoGUI.updateElementsPosition();
    }

    private void onGameStart() {
        BiConsumer<Long, Runnable> runGameTask = (delayBetweenCalls, task) ->
                new Thread(() -> {
                    while (!gameCompleted) {
                        try {
                            Thread.sleep(delayBetweenCalls);
                        } catch (InterruptedException e) {
                            break;
                        }
                        Platform.runLater(task);
                    }
                }).start();
        runGameTask.accept(GAME_STATUS_REQUEST_DELAY, this::renderScene);
        runGameTask.accept(MOUSE_POSITION_NOTIFY_DELAY, this::notifyMousePosition);

    }

    private void notifyMousePosition() {
        var handler = new HttpResponseHandler.HttpResponseHandlerBuilder<JSONObject>().build();
        gameService.notifyNewMousePosition(mousePosition, handler);
    }

    private void renderScene() {
        Consumer<GameStatusDTO> onSuccess = gameStatus -> {
            if (gameCompleted) // Prevent duplicate invocation
                return;
            if (gameStatus.isGameCompleted()) {
                onGameCompleted(gameStatus);
            } else {
                gameInfoGUI.showInfo(gameStatus);
                drawGameObjects(gameStatus);
            }
        };

        var handler = new HttpResponseHandler.HttpResponseHandlerBuilder<GameStatusDTO>()
                .withOnSuccess(onSuccess)
                .build();
        gameService.getGameStatus(handler);
    }

    private void onGameCompleted(GameStatusDTO gameStatus) {
        gameCompleted = true;
        stageManager.navigate(UiPage.MAIN_MENU);
        String message = "Game is over, %s. Score: %s";
        AlertUtil.showSuccess(String.format(message, authManager.getUsername(), gameStatus.getScore()));
    }

    private void bindCheatKeyEvent() {
        String CHEAT_KEY_COMBINATION = "CTRL+SHIFT+9";
        Scene currentScene = stageManager.getStage().getScene();
        currentScene.getAccelerators().put(KeyCombination.keyCombination(CHEAT_KEY_COMBINATION), () -> {
            var handler = new HttpResponseHandler.HttpResponseHandlerBuilder<JSONObject>()
                    .withOnError(AlertUtil::showError)
                    .build();
            gameService.applyCheat(handler);
        });
    }

    private void drawGameObjects(GameStatusDTO gameStatus) {
        Consumer<ImageView> drawSprite = sprite -> {
            pane.getChildren().add(sprite);
            gameObjectSprites.add(sprite);
        };

        pane.getChildren().removeAll(gameObjectSprites);
        gameObjectSprites.clear();

        gameStatus
                .getSpaceships()
                .forEach(spaceship -> drawSprite.accept(GameObjectDrawer.getSprite(canvas, spaceship)));
        gameStatus
                .getBullets()
                .forEach(bullet -> drawSprite.accept(GameObjectDrawer.getSprite(canvas, bullet)));
    }
}

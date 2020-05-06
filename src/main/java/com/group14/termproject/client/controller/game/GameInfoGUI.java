package com.group14.termproject.client.controller.game;

import com.group14.termproject.client.game.SpaceshipDTO;
import com.group14.termproject.client.game.Vector2D;
import com.group14.termproject.client.game.enums.Level;
import com.group14.termproject.client.game.enums.SpaceshipType;
import com.group14.termproject.client.model.GameStatusDTO;
import com.group14.termproject.client.util.GameUiUtil;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class GameInfoGUI {

    private static final double INFO_TOP_MARGIN = 20;
    private static final double HEALTH_BAR_HEIGHT = 20;

    private final Canvas canvas;
    private final Pane pane;
    @Getter
    private Button readyButton;
    @Getter
    private Text levelInfo;
    @Getter
    private Text scoreInfo;
    @Getter
    private Rectangle healthBar, maxHealthBar;

    public GameInfoGUI(@NotNull Pane pane, @NotNull Canvas canvas) {
        this.pane = pane;
        this.canvas = canvas;
        this.addElementsToPane();
    }

    /**
     * <p>
     * Initially all GUI elements' positions are set to (0, 0).
     * <br>
     * Calling this method, rearranges elements' positions which are relative to canvas. Thus, the method should be
     * called anytime the canvas's size changes so that elements positions could stay same relatively.
     * </p>
     */
    public void updateElementsPosition() {
        setHealthBarPosition();
        setInfoTextsPosition();
        setReadyButtonPosition();
    }

    /**
     * <p>
     * Sets <b>Level</b>, <b>Score</b> and <b>Health Bar</b> according to {@code gameStatusDTO}.
     * <br>
     * After calling this method, old values, if exists, are overwritten.
     * </p>
     *
     * @param gameStatusDTO Latest status of the game
     */
    public void showInfo(GameStatusDTO gameStatusDTO) {
        levelInfo.setText(GameUiUtil.getLevelName(gameStatusDTO.getLevel()));
        scoreInfo.setText(Integer.toString((int) gameStatusDTO.getScore()));
        showHealthBar(gameStatusDTO);
    }

    public void removeReadyButton() {
        pane.getChildren().remove(readyButton);
    }

    private void addElementsToPane() {
        this.addReadyButton();
        this.addInfoTexts();
        this.addHealthBar();
    }


    private void addReadyButton() {
        readyButton = new Button("Ready");
        pane.getChildren().add(readyButton);

        pane.getStylesheets().add("/ui-pages/style.css");
        readyButton.getStyleClass().add("button-label-base");
        readyButton.getStyleClass().add("button-label");
        readyButton.setStyle("-fx-font-family: \"Impact\";");

    }

    private void addHealthBar() {
        double width = 20;
        Color barColor = Color.RED;
        Color backgroundColor = Color.BLACK;
        healthBar = new Rectangle(0, width, barColor);
        maxHealthBar = new Rectangle(0, width, backgroundColor);

        maxHealthBar.setStroke(barColor);

        Consumer<Rectangle> configBarAndAdd = bar -> {
            bar.setHeight(HEALTH_BAR_HEIGHT);
            pane.getChildren().add(bar);
        };

        configBarAndAdd.accept(maxHealthBar);
        configBarAndAdd.accept(healthBar);
    }

    private void addInfoTexts() {
        levelInfo = new Text(GameUiUtil.getLevelName(Level.LEVEL_1));
        scoreInfo = new Text("0");

        Consumer<Text> setStyleAndAdd = text -> {
            text.getStyleClass().add("text");
            text.setFill(Color.rgb(172, 166, 219));
            text.setStyle("-fx-font-size: 24; -fx-font-family: \"Impact\";");
            pane.getChildren().add(text);
        };

        setStyleAndAdd.accept(levelInfo);
        setStyleAndAdd.accept(scoreInfo);

    }

    private void setHealthBarPosition() {
        double leftMargin = 10;
        double width = canvas.getWidth() / 4;
        Vector2D position = new Vector2D(leftMargin, INFO_TOP_MARGIN);

        Consumer<Rectangle> setBarProperties = bar -> {
            bar.setX(position.getX());
            bar.setY(position.getY());
            bar.setWidth(width);
        };

        setBarProperties.accept(healthBar);
        setBarProperties.accept(maxHealthBar);
    }

    private void setInfoTextsPosition() {

        // Since javafx takes position value with bottom left, it needs extra work to be centralized.
        BiConsumer<Text, Double> setPosition = (text, x) -> {
            double offset = text.getBoundsInLocal().getWidth() / 2;
            text.setX(x - offset);
            text.setY(INFO_TOP_MARGIN + HEALTH_BAR_HEIGHT);
        };

        setPosition.accept(levelInfo, canvas.getWidth() * 3 / 4);
        setPosition.accept(scoreInfo, canvas.getWidth() / 2);
    }

    private void setReadyButtonPosition() {
        Vector2D center = new Vector2D(canvas.getWidth() / 2, canvas.getHeight() / 2);
        double xOffset = readyButton.getWidth() / 2;
        double yOffset = readyButton.getHeight() / 2;
        readyButton.setLayoutX(center.getX() - xOffset);
        readyButton.setLayoutY(center.getY() - yOffset);
    }

    private void showHealthBar(GameStatusDTO gameStatusDTO) {
        SpaceshipDTO player = gameStatusDTO.getSpaceships()
                .stream()
                .filter(spaceshipDTO -> spaceshipDTO.getType() == SpaceshipType.PLAYER)
                .findFirst()
                .orElse(null);
        if (player != null) {
            double healthPercentage = player.getHealth() / player.getMaxHealth();
            healthBar.setWidth(healthPercentage * maxHealthBar.getWidth());
        }
    }


}

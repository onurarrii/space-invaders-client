package com.group14.termproject.client.service;

import com.group14.termproject.client.util.enums.UiPage;
import javafx.stage.Stage;

public interface StageManager {
    /**
     * After calling it, the current page is immediately closed and, a new page is created.
     * If @{code page} is same as the current page, then the page is refreshed.
     * One should use {@link #openGamePage()} to navigate to game GUI.
     *
     * @param page page to navigate.
     */
    void navigate(UiPage page);

    /**
     * Opens the game GUI. Calling it multiple times results in refreshing the page.
     */
    void openGamePage();

    /**
     * @return current stage.
     */
    Stage getStage();
}
package com.group14.termproject.client.controller;

import com.group14.termproject.client.model.LeaderboardEntry;
import com.group14.termproject.client.service.StageManager;
import com.group14.termproject.client.service.http.HttpResponseHandler;
import com.group14.termproject.client.service.http.LeaderboardService;
import com.group14.termproject.client.util.AlertUtil;
import com.group14.termproject.client.util.enums.UiPage;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Component
public class LeaderboardController implements Initializable {
    final LeaderboardService leaderboardService;
    final StageManager stageManager;

    public TableView<LeaderboardEntry> table;
    public TableColumn<LeaderboardEntry, String> userColumn;
    public TableColumn<LeaderboardEntry, Double> scoreColumn;
    public TableColumn<LeaderboardEntry, String> createdOnColumn;
    public Button queryButtonLastWeek;
    public Button queryButtonLastMonth;
    public Button queryButtonAllTime;
    public Button backButton;

    public LeaderboardController(LeaderboardService leaderboardService, StageManager stageManager) {
        this.leaderboardService = leaderboardService;
        this.stageManager = stageManager;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int columnNumber = 3;
        userColumn.prefWidthProperty().bind(table.widthProperty().divide(columnNumber));
        scoreColumn.prefWidthProperty().bind(table.widthProperty().divide(columnNumber));
        createdOnColumn.prefWidthProperty().bind(table.widthProperty().divide(columnNumber));

        userColumn.setCellValueFactory(new PropertyValueFactory<>("user"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        createdOnColumn.setCellValueFactory(new PropertyValueFactory<>("createdOn"));

        // By default, show last 7 days' leaderboard entries.
        onLastWeekClick();
    }

    public void onLastWeekClick() {
        getEntries(LeaderboardService.TimeLimitQuery.ONE_WEEK);
    }

    public void onLastMonthClick() {
        getEntries(LeaderboardService.TimeLimitQuery.ONE_MONTH);

    }

    public void onAllTimeClick() {
        getEntries(LeaderboardService.TimeLimitQuery.ALL_TIME);
    }

    public void onBackButtonClick() {
        stageManager.navigate(UiPage.MAIN_MENU);
    }

    private void getEntries(LeaderboardService.TimeLimitQuery timeLimitQuery) {
        HttpResponseHandler<List<LeaderboardEntry>> handler =
                new HttpResponseHandler.HttpResponseHandlerBuilder<List<LeaderboardEntry>>()
                        .withOnError(AlertUtil::showError)
                        .withOnSuccess(this::fillTable)
                        .build();
        leaderboardService.getEntries(timeLimitQuery, handler);
    }

    private void fillTable(List<LeaderboardEntry> leaderboardEntries) {
        table.getItems().clear();
        table.getItems().addAll(leaderboardEntries);
    }
}

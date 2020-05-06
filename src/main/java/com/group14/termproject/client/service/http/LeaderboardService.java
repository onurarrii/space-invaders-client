package com.group14.termproject.client.service.http;

import com.group14.termproject.client.model.LeaderboardEntry;

import java.util.List;

public interface LeaderboardService {

    /**
     * Used for retrieving Leaderboard entry list.
     *
     * @param timeLimit indicates how many previous days to be included in the query.
     * @param handler   see {@link HttpResponseHandler}
     */
    void getEntries(TimeLimitQuery timeLimit, HttpResponseHandler<List<LeaderboardEntry>> handler);

    enum TimeLimitQuery {
        ONE_WEEK,
        ONE_MONTH,
        ALL_TIME
    }
}

package com.group14.termproject.client.service.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group14.termproject.client.model.LeaderboardEntry;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
class LeaderboardServiceImpl extends HttpService implements LeaderboardService {

    private static final int ENTRY_COUNT_LIMIT = 50;
    private static final String ENTRY_COUNT_FIELD_NAME = "limit";
    private static final String TIME_LIMIT_FIELD_NAME = "latest-days-limit";
    private static final Map<TimeLimitQuery, Integer> timeLimitQueryValues = new HashMap<>() {{
        put(TimeLimitQuery.ONE_WEEK, 7);
        put(TimeLimitQuery.ONE_MONTH, 30);
    }};

    @Value("${custom.server.leaderboard.api.uri}")
    private String authAPIUri;

    public void getEntries(TimeLimitQuery timeLimit, HttpResponseHandler<List<LeaderboardEntry>> handler) {
        var response = makeRequest(createUri(timeLimit), HttpMethod.GET, new JSONObject(), handler);
        if (response.getBody() != null) {
            try {
                LeaderboardEntry[] leaderboardEntries = new ObjectMapper()
                        .readValue(response.getBody(), LeaderboardEntry[].class);

                handler.getOnSuccessCallback().accept(Arrays.asList(leaderboardEntries));
            } catch (JsonProcessingException e) {
                throw new RuntimeException();
            }
        }
    }

    private String createUri(TimeLimitQuery timeLimit) {
        String urlParameters = "?" + ENTRY_COUNT_FIELD_NAME + "=" + ENTRY_COUNT_LIMIT;
        Integer timeLimitValue = timeLimitQueryValues.get(timeLimit);
        if (timeLimitValue != null) {
            urlParameters += "&" + TIME_LIMIT_FIELD_NAME + "=" + timeLimitValue;
        }
        return authAPIUri + urlParameters;
    }
}

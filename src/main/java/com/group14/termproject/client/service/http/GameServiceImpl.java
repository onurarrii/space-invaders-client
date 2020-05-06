package com.group14.termproject.client.service.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group14.termproject.client.game.Vector2D;
import com.group14.termproject.client.model.GameStatusDTO;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

@Service
class GameServiceImpl extends HttpService implements GameService {
    @Value("${custom.server.game.api.uri}")
    private String gameAPIUri;

    @Override
    public void createRoom(HttpResponseHandler<JSONObject> handler) {
        makeRoomRequest("create", handler);
    }

    @Override
    public void startGame(HttpResponseHandler<JSONObject> handler) {
        makeRoomRequest("start", handler);
    }

    @SneakyThrows
    @Override
    public void getGameStatus(HttpResponseHandler<GameStatusDTO> handler) {
        String uri = gameAPIUri + "status";
        var response = makeRequest(uri, HttpMethod.GET, new JSONObject(), handler);
        if (response.getBody() != null) {
            GameStatusDTO gameStatus = new ObjectMapper()
                    .readValue(response.getBody(), GameStatusDTO.class);
            handler.getOnSuccessCallback().accept(gameStatus);
        }
    }

    @Override
    public void notifyNewMousePosition(Vector2D newPosition, HttpResponseHandler<JSONObject> handler) {
        String uri = gameAPIUri + "reposition";
        makeRequest(uri, HttpMethod.POST, new JSONObject(newPosition), handler);
    }

    @Override
    public void applyCheat(HttpResponseHandler<JSONObject> handler) {
        String uri = gameAPIUri + "cheat";
        var response = makeRequest(uri, HttpMethod.POST, new JSONObject(), handler);
        tryInvokingSuccessCallback(response, handler);
    }

    private void makeRoomRequest(String apiName, HttpResponseHandler<JSONObject> handler) {
        String uri = gameAPIUri + apiName;
        var response = makeRequest(uri, HttpMethod.POST, new JSONObject(), handler);
        tryInvokingSuccessCallback(response, handler);
    }
}

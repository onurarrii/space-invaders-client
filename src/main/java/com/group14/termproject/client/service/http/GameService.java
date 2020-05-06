package com.group14.termproject.client.service.http;

import com.group14.termproject.client.game.Vector2D;
import com.group14.termproject.client.model.GameStatusDTO;
import lombok.SneakyThrows;
import org.json.JSONObject;

public interface GameService {

    /**
     * Sends a room creation request to server.
     *
     * @param handler see {@link HttpResponseHandler}
     */
    void createRoom(HttpResponseHandler<JSONObject> handler);

    /**
     * Sends a request to start the game.
     *
     * @param handler see {@link HttpResponseHandler}
     */
    void startGame(HttpResponseHandler<JSONObject> handler);

    /**
     * Sends an HTTP GET request to retrieve game's current status.
     *
     * @param handler see {@link HttpResponseHandler}
     */
    @SneakyThrows
    void getGameStatus(HttpResponseHandler<GameStatusDTO> handler);

    /**
     * Notifies the server about mouse's new position.
     *
     * @param newPosition Last detected position of mouse.
     * @param handler     see {@link HttpResponseHandler}
     */
    void notifyNewMousePosition(Vector2D newPosition, HttpResponseHandler<JSONObject> handler);

    /**
     * Requests to apply a cheat from the server. Note that, {@code handler}'s error callback might be invoked if
     * game's current status is not appropriate for cheating.
     *
     * @param handler see {@link HttpResponseHandler}
     */
    void applyCheat(HttpResponseHandler<JSONObject> handler);
}

package com.group14.termproject.client.controller;

import com.group14.termproject.client.model.UserDTO;
import com.group14.termproject.client.service.AuthManager;
import com.group14.termproject.client.service.http.AuthService;
import com.group14.termproject.client.service.http.HttpResponseHandler;
import com.group14.termproject.client.util.AlertUtil;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class AuthController implements Initializable {
    final AuthManager authManager;
    final AuthService authService;
    @FXML
    public Button loginButton;
    @FXML
    public Button registerButton;
    @FXML
    TextField usernameField;
    @FXML
    PasswordField passwordField;

    public AuthController(AuthService authService, AuthManager authManager) {
        this.authService = authService;
        this.authManager = authManager;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    public void onLoginClick() {

        HttpResponseHandler<JSONObject> responseHandler = new HttpResponseHandler
                .HttpResponseHandlerBuilder<JSONObject>()
                .withOnSuccess(response -> {
                    String token = response.getString("token");
                    String username = usernameField.getText();
                    authManager.onSuccessfulLogin(token, username);
                })
                .withOnError(AlertUtil::showError)
                .build();
        authService.login(getUser(), responseHandler);

    }

    @FXML
    public void onRegisterClick() {

        HttpResponseHandler<JSONObject> responseHandler = new HttpResponseHandler
                .HttpResponseHandlerBuilder<JSONObject>()
                .withOnSuccess(id -> AlertUtil.showSuccess("Successfully registered."))
                .withOnError(AlertUtil::showError)
                .build();

        authService.register(getUser(), responseHandler);
    }

    private UserDTO getUser() {
        return new UserDTO(usernameField.getText(), passwordField.getText());
    }

}

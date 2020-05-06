package com.group14.termproject.client.util;

import javafx.scene.control.Alert;

public final class AlertUtil {

    private AlertUtil() {
    }

    public static void showSuccess(String message) {
        showAlert(Alert.AlertType.INFORMATION, "Info", message);
    }

    public static void showError(String errorMessage) {
        showAlert(Alert.AlertType.ERROR, "Error", errorMessage);
    }

    private static void showAlert(Alert.AlertType alertType, String header, String errorMessage) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(header);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

}

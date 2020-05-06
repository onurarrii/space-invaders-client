package com.group14.termproject.client;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientApp {

    public static void main(String[] args) {
        Application.launch(UiApplication.class, args);
    }

}

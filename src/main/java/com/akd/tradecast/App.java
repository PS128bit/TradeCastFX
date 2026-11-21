package com.akd.tradecast;

import com.akd.tradecast.ui.LoginWindow;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * AKD TradeCast - JavaFX frontend replica (SDA Assignment 2).
 * Starts at the login screen; a successful login opens the main trading window.
 */
public class App extends Application {

    private static final String CSS = App.class.getResource("tradecast.css").toExternalForm();

    /** Applies the shared legacy-desktop stylesheet to a scene. Every window calls this. */
    public static Scene themed(Scene scene) {
        scene.getStylesheets().add(CSS);
        return scene;
    }

    /** Applies the stylesheet to an Alert or other Dialog. */
    public static void themed(javafx.scene.control.Dialog<?> dialog) {
        dialog.getDialogPane().getStylesheets().add(CSS);
    }

    @Override
    public void start(Stage stage) {
        new LoginWindow(stage).show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

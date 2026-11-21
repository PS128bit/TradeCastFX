package com.akd.tradecast.ui.azan;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/** Temporary content shown until Azan's screen is built. Delete this class when all four are done. */
final class Placeholder {
    private Placeholder() { }

    static Node of(String screen, String owner) {
        Label title = new Label(screen);
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");
        Label who = new Label("To be built by " + owner);
        VBox box = new VBox(6, title, who);
        box.setAlignment(Pos.CENTER);
        box.getStyleClass().add("placeholder");
        return box;
    }
}

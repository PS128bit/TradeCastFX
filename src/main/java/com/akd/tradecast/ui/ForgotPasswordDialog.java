package com.akd.tradecast.ui;

import com.akd.tradecast.App;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.util.regex.Pattern;

/** Screen 1 (popup): Forgot Password, opened from the login window. */
public class ForgotPasswordDialog {

    private static final Pattern EMAIL = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");

    private final Stage dialog = new Stage();
    private final TextField userId = new TextField();
    private final TextField email = new TextField();
    private final Label error = new Label();

    public ForgotPasswordDialog(Window owner) {
        dialog.initOwner(owner);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setTitle("Forgot Password");
        dialog.setResizable(false);

        GridPane form = new GridPane();
        form.setHgap(8);
        form.setVgap(9);
        userId.setPrefWidth(160);
        email.setPrefWidth(160);
        Label u = new Label("User ID");
        u.setMinWidth(Region.USE_PREF_SIZE);
        form.add(u, 0, 0);
        form.add(userId, 1, 0);
        Label em = new Label("Email Address");
        em.setMinWidth(Region.USE_PREF_SIZE);
        form.add(em, 0, 1);
        form.add(email, 1, 1);

        Button ok = new Button("OK");
        ok.setDefaultButton(true);
        ok.setPrefWidth(62);
        ok.setOnAction(e -> submit());
        Button cancel = new Button("Cancel");
        cancel.setCancelButton(true);
        cancel.setPrefWidth(62);
        cancel.setOnAction(e -> dialog.close());
        HBox buttons = new HBox(10, ok, cancel);
        buttons.setPadding(new Insets(0, 0, 0, 90));
        form.add(buttons, 1, 2);

        error.getStyleClass().add("error-label");
        form.add(error, 1, 3);

        // Logos: TradeCast top-left, akd trade.com top-right
        HBox logos = new HBox(Brand.tradeCastLogo(1.3), spacer(), Brand.akdTradeLogo(0.85));
        logos.setAlignment(Pos.TOP_LEFT);
        logos.setPadding(new Insets(10, 16, 0, 60));

        GridPane centre = new GridPane();
        centre.add(form, 0, 0);
        centre.setPadding(new Insets(14, 20, 10, 36));

        Label copyright = new Label("Copyright 2010 - 2011 CATALYST IT Solutions (Pvt.) Limited. All Rights Reserved.");
        copyright.getStyleClass().add("copyright");
        HBox footer = new HBox(Brand.preludeLogo(), spacer(), copyright);
        footer.setAlignment(Pos.CENTER_LEFT);
        footer.getStyleClass().add("login-footer");

        BorderPane root = new BorderPane(centre, logos, null, footer, null);
        root.getStyleClass().add("login-pane");
        dialog.setScene(App.themed(new Scene(root, 570, 300)));
    }

    public void showAndWait() {
        dialog.showAndWait();
    }

    private void submit() {
        if (userId.getText().trim().isEmpty() || email.getText().trim().isEmpty()) {
            error.setText("Please enter both User ID and Email Address.");
            return;
        }
        if (!EMAIL.matcher(email.getText().trim()).matches()) {
            error.setText("Please enter a valid Email Address.");
            return;
        }
        error.setText("");
        Alert done = new Alert(Alert.AlertType.INFORMATION);
        done.initOwner(dialog);
        done.setTitle("Forgot Password");
        done.setHeaderText(null);
        done.setContentText("A new password has been sent to " + email.getText().trim()
                + " from connect@akdtrade.com.");
        App.themed(done);
        done.showAndWait();
        dialog.close();
    }

    private static Region spacer() {
        Region r = new Region();
        HBox.setHgrow(r, Priority.ALWAYS);
        return r;
    }
}

package com.akd.tradecast.ui;

import com.akd.tradecast.App;
import com.akd.tradecast.data.MockData;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Screen 1: Trade Cast login.
 * The password is never typed in full: the user fills only 4 randomly chosen
 * positions out of 10 character boxes, as named in the instruction label.
 */
public class LoginWindow {

    public static final int PASSWORD_LENGTH = 10;
    private static final int CHALLENGE_SIZE = 4;

    private final Stage stage;
    private final Random random = new Random();
    private final TextField userIdField = new TextField();
    private final List<PasswordField> boxes = new ArrayList<>();
    private final Label challengeLabel = new Label();
    private final Label errorLabel = new Label("Invalid login credentials!");
    private List<Integer> challenge = new ArrayList<>();

    public LoginWindow(Stage stage) {
        this.stage = stage;
    }

    public void show() {
        BorderPane root = new BorderPane();
        root.getStyleClass().add("login-pane");
        root.setCenter(buildForm());
        root.setBottom(footer());

        newChallenge();

        stage.setTitle("Trade Cast v1.4.0");
        stage.setScene(App.themed(new Scene(root, 800, 420)));
        stage.setResizable(false);
        stage.show();
        userIdField.requestFocus();
    }

    private Node buildForm() {
        GridPane grid = new GridPane();
        grid.setMinWidth(Region.USE_PREF_SIZE);
        grid.setHgap(14);
        grid.setVgap(8);
        grid.setPadding(new Insets(14, 20, 6, 16));

        grid.add(Brand.tradeCastLogo(1.6), 1, 0);

        // User ID row
        userIdField.setPrefWidth(122);
        Button forgot = new Button("Forgot Password?");
        forgot.setOnAction(e -> new ForgotPasswordDialog(stage).showAndWait());
        grid.add(fixedLabel("User ID"), 0, 1);
        grid.add(new HBox(10, userIdField, forgot), 1, 1);

        // Password row: 10 individual character boxes
        HBox boxRow = new HBox(4);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            PasswordField box = charBox();
            final int index = i;
            box.textProperty().addListener((obs, old, val) -> {
                if (val != null && val.length() == 1) focusNext(index);
            });
            box.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.BACK_SPACE && box.getText().isEmpty()) focusPrevious(index);
            });
            boxes.add(box);
            boxRow.getChildren().add(box);
        }
        grid.add(fixedLabel("Password"), 0, 2);
        grid.add(boxRow, 1, 2);

        challengeLabel.getStyleClass().add("hint-label");
        challengeLabel.setMinWidth(Region.USE_PREF_SIZE);
        grid.add(challengeLabel, 1, 3);

        errorLabel.getStyleClass().add("error-label");
        errorLabel.setVisible(false);
        grid.add(errorLabel, 1, 4);

        Button login = new Button("Login");
        login.setDefaultButton(true);
        login.setPrefWidth(64);
        login.setOnAction(e -> attemptLogin());
        Button cancel = new Button("Cancel");
        cancel.setCancelButton(true);
        cancel.setPrefWidth(64);
        cancel.setOnAction(e -> Platform.exit());
        HBox buttons = new HBox(10, login, cancel);
        buttons.setPadding(new Insets(0, 0, 0, 112));
        grid.add(buttons, 1, 5);

        grid.add(exampleSection(), 1, 6);

        // akd trade.com logo on the right, level with the User ID / Password rows
        Node akd = Brand.akdTradeLogo(1.0);
        VBox right = new VBox(akd);
        right.setPadding(new Insets(48, 24, 0, 0));
        right.setAlignment(Pos.TOP_RIGHT);

        HBox content = new HBox(grid, spacer(), right);
        return content;
    }

    /** Static worked example underneath the buttons, as on the original screen. */
    private Node exampleSection() {
        Text t1 = new Text("Example: If your ");
        Text t2 = new Text("Password is q a Z s 1 2 q G");
        t2.setStyle("-fx-font-weight: bold;");
        Text t3 = new Text(", you would be required to enter the missing characters in white boxes.");
        TextFlow text = new TextFlow(t1, t2, t3);
        text.setPrefWidth(400);
        text.setMinWidth(400);

        String[] shown = {"", "a", "", "s", "", "2", "q", "", "", ""};
        HBox exampleBoxes = new HBox(4);
        for (String s : shown) {
            TextField b = new TextField(s);
            b.getStyleClass().addAll("char-box", "example");
            b.setEditable(false);
            b.setFocusTraversable(false);
            if (s.isEmpty()) b.setDisable(true);
            exampleBoxes.getChildren().add(b);
        }

        String[] letters = {"q", "a", "Z", "s", "1", "2", "q", "G"};
        HBox letterRow = new HBox(0);
        for (int i = 0; i < letters.length; i++) {
            Label l = new Label(letters[i]);
            l.setMinWidth(20);
            l.setAlignment(Pos.CENTER);
            if (!shown[i].isEmpty()) l.setStyle("-fx-font-weight: bold;");
            letterRow.getChildren().add(l);
        }
        VBox box = new VBox(5, text, exampleBoxes, letterRow);
        box.setPadding(new Insets(8, 0, 0, 0));
        return box;
    }

    private Node footer() {
        Label copyright = new Label("Copyright 2010 - 2011 CATALYST IT Solutions (Pvt.) Limited. All Rights Reserved.");
        copyright.getStyleClass().add("copyright");
        HBox footer = new HBox(Brand.preludeLogo(), spacer(), copyright);
        footer.setAlignment(Pos.CENTER_LEFT);
        footer.getStyleClass().add("login-footer");
        return footer;
    }

    /** Picks 4 random positions, enables only those boxes and rewrites the instruction label. */
    private void newChallenge() {
        List<Integer> all = new ArrayList<>();
        for (int i = 0; i < PASSWORD_LENGTH; i++) all.add(i);
        Collections.shuffle(all, random);
        challenge = all.subList(0, CHALLENGE_SIZE).stream().sorted().collect(Collectors.toList());

        for (int i = 0; i < boxes.size(); i++) {
            boxes.get(i).clear();
            boxes.get(i).setDisable(!challenge.contains(i));
        }
        challengeLabel.setText(challengeText(challenge));
    }

    /** e.g. [2, 4, 6, 7] -> "Please enter 3rd, 5th, 7th & 8th character of your password." */
    static String challengeText(List<Integer> positions) {
        List<String> names = positions.stream().map(p -> ordinal(p + 1)).collect(Collectors.toList());
        String joined = String.join(", ", names.subList(0, names.size() - 1)) + " & " + names.get(names.size() - 1);
        return "Please enter " + joined + " character of your password.";
    }

    static String ordinal(int n) {
        if (n % 100 >= 11 && n % 100 <= 13) return n + "th";
        switch (n % 10) {
            case 1: return n + "st";
            case 2: return n + "nd";
            case 3: return n + "rd";
            default: return n + "th";
        }
    }

    private void attemptLogin() {
        boolean ok = MockData.DEMO_USER_ID.equalsIgnoreCase(userIdField.getText().trim());
        for (int pos : challenge) {
            String typed = boxes.get(pos).getText();
            if (typed == null || typed.length() != 1 || typed.charAt(0) != MockData.DEMO_PASSWORD.charAt(pos)) {
                ok = false;
            }
        }
        if (!ok) {
            errorLabel.setVisible(true);
            newChallenge();
            boxes.get(challenge.get(0)).requestFocus();
            return;
        }
        errorLabel.setVisible(false);
        stage.hide();
        new MainWindow(new Stage(), userIdField.getText().trim().toUpperCase()).show();
    }

    private PasswordField charBox() {
        PasswordField f = new PasswordField();
        f.getStyleClass().add("char-box");
        f.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= 1 ? change : null));
        return f;
    }

    private void focusNext(int from) {
        for (int i = from + 1; i < boxes.size(); i++) {
            if (!boxes.get(i).isDisabled()) {
                boxes.get(i).requestFocus();
                return;
            }
        }
    }

    private void focusPrevious(int from) {
        for (int i = from - 1; i >= 0; i--) {
            if (!boxes.get(i).isDisabled()) {
                boxes.get(i).requestFocus();
                return;
            }
        }
    }

    private static Label fixedLabel(String text) {
        Label l = new Label(text);
        l.setMinWidth(Region.USE_PREF_SIZE);
        return l;
    }

    private static Region spacer() {
        Region r = new Region();
        HBox.setHgrow(r, Priority.ALWAYS);
        return r;
    }
}

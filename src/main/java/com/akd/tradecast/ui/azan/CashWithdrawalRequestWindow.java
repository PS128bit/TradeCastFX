package com.akd.tradecast.ui.azan;

import com.akd.tradecast.App;
import com.akd.tradecast.data.MockData;
import com.akd.tradecast.ui.Fmt;
import com.akd.tradecast.ui.Icons;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;

/** Screen 6: Cash Withdrawal Request (Services menu, Alt+R). */
public final class CashWithdrawalRequestWindow {

    private static final double AVAILABLE_BALANCE = 316.00;

    private final Stage stage = new Stage();
    private final ComboBox<String> account = new ComboBox<>(FXCollections.observableArrayList(MockData.ACCOUNTS.keySet()));
    private final PasswordField pinField = new PasswordField();
    private final TextField availableField = readOnly(Fmt.money(AVAILABLE_BALANCE));
    private final TextField withdrawalField = new TextField();
    private final TextField remainingField = readOnly(Fmt.money(AVAILABLE_BALANCE));

    private final ToggleGroup modeGroup = new ToggleGroup();
    private final RadioButton chequeDelivery = new RadioButton();
    private final RadioButton chequePickup = new RadioButton();
    private final RadioButton onlineTransfer = new RadioButton();
    private final ComboBox<String> branchCombo = new ComboBox<>(FXCollections.observableArrayList(
            "Karachi - Clifton Head Office", "Lahore - Gulberg Branch", "Islamabad - Blue Area Branch"));
    private final TextField bankAccountField = new TextField();
    private final TextArea commentsArea = new TextArea();
    private final Label errorLabel = new Label();

    private CashWithdrawalRequestWindow(Window owner) {
        stage.initOwner(owner);
        stage.setTitle("Cash Withdrawal Request");
        stage.setResizable(false);

        BorderPane root = new BorderPane();
        root.setTop(header());
        root.setCenter(body());
        root.setBottom(footer());

        stage.setScene(App.themed(new Scene(root, 500, 580)));
    }

    public static void show(Window owner) {
        new CashWithdrawalRequestWindow(owner).stage.show();
    }

    private Node header() {
        Label title = new Label("Cash Withdrawal Request");
        title.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        HBox box = new HBox(title);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(14, 16, 14, 16));
        box.setStyle("-fx-background-color: linear-gradient(to right, #FFFFFF, #ECE9D8);"
                + "-fx-border-color: transparent transparent #C7C5B2 transparent;");
        return box;
    }

    private Node body() {
        VBox content = new VBox(10);
        content.setPadding(new Insets(12, 16, 8, 16));

        Label prompt = new Label("Please select the account for which you wish to make Cash Withdrawal");

        account.getSelectionModel().selectFirst();
        account.setPrefWidth(160);
        pinField.setPrefWidth(110);
        pinField.setText("1234");

        HBox accountRow = new HBox(8, label("Account No"), account, label("PIN"), pinField);
        accountRow.setAlignment(Pos.CENTER_LEFT);

        Label statementLink = new Label("Cash Withdrawal Statement", Icons.browse());
        statementLink.setStyle("-fx-font-weight: bold; -fx-cursor: hand;");
        statementLink.setOnMouseClicked(e -> CashWithdrawalStatementWindow.show(stage));

        Label forgotPin = new Label("Forgot PIN?");
        forgotPin.getStyleClass().add("error-label");
        forgotPin.setStyle("-fx-cursor: hand; -fx-underline: true;");
        forgotPin.setOnMouseClicked(e -> alert(Alert.AlertType.INFORMATION,
                "A PIN reset link has been sent to " + MockData.DEMO_EMAIL + "."));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox linksRow = new HBox(statementLink, spacer, forgotPin);
        linksRow.setAlignment(Pos.CENTER_LEFT);

        // Live calculation row: Available - Withdrawal = Remaining
        withdrawalField.setPrefWidth(110);
        withdrawalField.setAlignment(Pos.CENTER_RIGHT);
        withdrawalField.textProperty().addListener((obs, oldVal, newVal) -> updateRemaining());

        GridPane calcGrid = new GridPane();
        calcGrid.setHgap(8);
        calcGrid.setVgap(4);
        calcGrid.add(new Label("Available Amount"), 1, 0);
        calcGrid.add(new Label("Withdrawal Amount"), 3, 0);
        calcGrid.add(new Label("Remaining Amount"), 5, 0);

        calcGrid.add(new Label("Rs."), 0, 1);
        calcGrid.add(availableField, 1, 1);
        calcGrid.add(new Label("-"), 2, 1);
        calcGrid.add(withdrawalField, 3, 1);
        calcGrid.add(new Label("="), 4, 1);
        calcGrid.add(remainingField, 5, 1);

        // Withdrawal modes
        chequeDelivery.setToggleGroup(modeGroup);
        chequePickup.setToggleGroup(modeGroup);
        onlineTransfer.setToggleGroup(modeGroup);
        chequeDelivery.setSelected(true);

        branchCombo.setPromptText("Please select");
        branchCombo.setPrefWidth(170);
        branchCombo.setDisable(true);
        bankAccountField.setPrefWidth(150);
        bankAccountField.setDisable(true);

        modeGroup.selectedToggleProperty().addListener((obs, a, b) -> {
            branchCombo.setDisable(b != chequePickup);
            bankAccountField.setDisable(b != onlineTransfer);
        });

        VBox modes = new VBox(8,
                modeRow(chequeDelivery, "Cheque Delivery",
                        "Please select this option if you would like us to send cheque at your registered Address.", null),
                new Separator(),
                modeRow(chequePickup, "Cheque Pickup",
                        "Please select this option if you would like to pickup cheque from our branches.", branchCombo),
                new Separator(),
                modeRow(onlineTransfer, "Online Fund Transfer",
                        "Please select this option if you would like us to transfer your withdrawal amount in your Standard Chartered Bank account.",
                        new HBox(6, label("Account No"), bankAccountField))
        );

        commentsArea.setPromptText("Please enter your comments (if required)");
        commentsArea.setPrefRowCount(4);
        commentsArea.setWrapText(true);

        errorLabel.getStyleClass().add("error-label");

        content.getChildren().addAll(
                prompt, accountRow, linksRow, new Separator(),
                calcGrid, new Separator(), modes, commentsArea, errorLabel);
        return content;
    }

    private Node modeRow(RadioButton radio, String title, String desc, Node extra) {
        Label t = new Label(title);
        t.setStyle("-fx-font-weight: bold;");
        HBox topLine = new HBox(10, t);
        topLine.setAlignment(Pos.CENTER_LEFT);
        if (extra != null) {
            Region sp = new Region();
            HBox.setHgrow(sp, Priority.ALWAYS);
            topLine.getChildren().addAll(sp, extra);
        }
        Label d = new Label(desc);
        d.setWrapText(true);
        VBox textCol = new VBox(3, topLine, d);
        HBox.setHgrow(textCol, Priority.ALWAYS);

        HBox row = new HBox(10, radio, textCol);
        row.setAlignment(Pos.CENTER_LEFT);
        return row;
    }

    private Node footer() {
        Button submit = new Button("Submit");
        submit.setDefaultButton(true);
        submit.setPrefWidth(80);
        submit.setOnAction(e -> submit());

        HBox bar = new HBox(submit);
        bar.setAlignment(Pos.CENTER_RIGHT);
        bar.setPadding(new Insets(6, 16, 12, 16));
        return bar;
    }

    private void updateRemaining() {
        String raw = withdrawalField.getText().replace(",", "").trim();
        if (raw.isEmpty()) {
            remainingField.setText(Fmt.money(AVAILABLE_BALANCE));
            remainingField.getStyleClass().remove("loss");
            errorLabel.setText("");
            return;
        }
        try {
            double amt = Double.parseDouble(raw);
            double rem = AVAILABLE_BALANCE - amt;
            remainingField.setText(Fmt.money(rem));
            remainingField.getStyleClass().remove("loss");
            if (rem < 0) remainingField.getStyleClass().add("loss");
            errorLabel.setText("");
        } catch (NumberFormatException ex) {
            remainingField.setText("Invalid");
            remainingField.getStyleClass().add("loss");
        }
    }

    private void submit() {
        if (pinField.getText().trim().isEmpty()) {
            errorLabel.setText("Please enter your PIN.");
            return;
        }
        double amt = Fmt.parse(withdrawalField.getText());
        if (amt <= 0) {
            errorLabel.setText("Withdrawal Amount must be greater than zero.");
            return;
        }
        if (amt > AVAILABLE_BALANCE) {
            errorLabel.setText("Withdrawal Amount cannot exceed Available Amount (Rs. " + Fmt.money(AVAILABLE_BALANCE) + ").");
            return;
        }
        if (chequePickup.isSelected() && branchCombo.getValue() == null) {
            errorLabel.setText("Please select a branch for Cheque Pickup.");
            return;
        }
        if (onlineTransfer.isSelected() && bankAccountField.getText().trim().isEmpty()) {
            errorLabel.setText("Please enter your bank Account No for Online Fund Transfer.");
            return;
        }
        errorLabel.setText("");
        String mode = chequeDelivery.isSelected() ? "Cheque Delivery"
                : chequePickup.isSelected() ? "Cheque Pickup (" + branchCombo.getValue() + ")"
                : "Online Fund Transfer (" + bankAccountField.getText().trim() + ")";
        alert(Alert.AlertType.INFORMATION,
                "Cash withdrawal request submitted.\n\nAccount: " + account.getValue()
                        + "\nAmount: Rs. " + Fmt.money(amt)
                        + "\nMode: " + mode);
        stage.close();
    }

    private void alert(Alert.AlertType type, String text) {
        Alert a = new Alert(type, text);
        a.initOwner(stage);
        a.setTitle("Cash Withdrawal Request");
        a.setHeaderText(null);
        App.themed(a);
        a.showAndWait();
    }

    private static TextField readOnly(String value) {
        TextField f = new TextField(value);
        f.setEditable(false);
        f.setFocusTraversable(false);
        f.setPrefWidth(110);
        f.getStyleClass().add("readonly");
        return f;
    }

    private static Label label(String text) {
        Label l = new Label(text);
        l.setMinWidth(Region.USE_PREF_SIZE);
        return l;
    }
}
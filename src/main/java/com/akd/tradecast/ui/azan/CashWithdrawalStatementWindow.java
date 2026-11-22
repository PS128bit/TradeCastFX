package com.akd.tradecast.ui.azan;

import com.akd.tradecast.App;
import com.akd.tradecast.data.MockData;
import com.akd.tradecast.ui.Cells;
import com.akd.tradecast.ui.Fmt;
import com.akd.tradecast.ui.Icons;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/** Screen 5: Cash Withdrawal Statement (Reports menu, Alt+W). */
public final class CashWithdrawalStatementWindow {

    private static final class WithdrawalRecord {
        final String accountNo;
        final LocalDate requestedDate;
        final double amountRequested;
        final double amountApproved;
        final String mode;
        final String comments;
        final String status;

        WithdrawalRecord(String accountNo, LocalDate requestedDate, double amountRequested,
                         double amountApproved, String mode, String comments, String status) {
            this.accountNo = accountNo;
            this.requestedDate = requestedDate;
            this.amountRequested = amountRequested;
            this.amountApproved = amountApproved;
            this.mode = mode;
            this.comments = comments;
            this.status = status;
        }
    }

    private static final List<WithdrawalRecord> SAMPLE_DATA = List.of(
            new WithdrawalRecord("CKHI000", LocalDate.of(2026, 7, 7), 680000.00, 680000.00, "SCBTransfer", "", "Approved"),
            new WithdrawalRecord("CKHI000", LocalDate.of(2026, 8, 14), 25000.00, 25000.00, "Cheque Delivery", "Urgent", "Approved"),
            new WithdrawalRecord("CKHI000", LocalDate.of(2026, 9, 22), 50000.00, 0.00, "Cheque Pickup", "Clifton Branch", "Declined"),
            new WithdrawalRecord("CKHI000", LocalDate.of(2026, 11, 18), 15000.00, 0.00, "SCBTransfer", "Monthly transfer", "In-Process"),
            new WithdrawalRecord("CKHI001", LocalDate.of(2026, 10, 5), 120000.00, 120000.00, "SCBTransfer", "", "Approved")
    );

    private final Stage stage = new Stage();
    private final ObservableList<WithdrawalRecord> rows = FXCollections.observableArrayList();

    private final ComboBox<String> account = new ComboBox<>(FXCollections.observableArrayList(MockData.ACCOUNTS.keySet()));
    private final ComboBox<String> name = new ComboBox<>(FXCollections.observableArrayList(MockData.ACCOUNTS.values()));
    private final PasswordField pinField = new PasswordField();
    private final DatePicker startDate = datePicker(LocalDate.of(2026, 1, 1));
    private final DatePicker endDate = datePicker(LocalDate.now());
    private final Label recordsLabel = new Label();

    private CashWithdrawalStatementWindow(Window owner) {
        stage.initOwner(owner);
        stage.setTitle("Cash Withdrawal Statement");

        BorderPane root = new BorderPane();
        root.setTop(filters());
        root.setCenter(grid());
        root.setBottom(statusBar());
        BorderPane.setMargin(root.getCenter(), new Insets(4, 10, 10, 10));

        stage.setScene(App.themed(new Scene(root, 780, 460)));
        applyFilters();
    }

    public static void show(Window owner) {
        new CashWithdrawalStatementWindow(owner).stage.show();
    }

    private Node filters() {
        account.getSelectionModel().selectFirst();
        name.getSelectionModel().selectFirst();
        account.setPrefWidth(130);
        name.setPrefWidth(180);
        account.setOnAction(e -> name.getSelectionModel().select(MockData.ACCOUNTS.get(account.getValue())));
        name.setOnAction(e -> {
            for (var entry : MockData.ACCOUNTS.entrySet()) {
                if (entry.getValue().equals(name.getValue())) account.getSelectionModel().select(entry.getKey());
            }
        });

        pinField.setPrefWidth(80);
        pinField.setText("1234");

        Button save = Icons.button(Icons.save(), "Save");
        Button pdf = Icons.button(Icons.pdf(), "Export to PDF");
        Button print = Icons.button(Icons.print(), "Print");
        Button cols = Icons.button(Icons.grid(), "Columns");
        for (Button b : new Button[]{save, pdf, print, cols}) {
            b.setOnAction(e -> info(((Tooltip) b.getTooltip()).getText() + " is not available in this demo (no backend)."));
        }
        HBox icons = new HBox(2, save, pdf, print, cols);

        Button get = new Button("Get");
        get.setPrefWidth(56);
        get.setDefaultButton(true);
        get.setOnAction(e -> applyFilters());

        Label forgotPin = new Label("Forgot PIN?");
        forgotPin.getStyleClass().add("error-label");
        forgotPin.setStyle("-fx-cursor: hand; -fx-underline: true;");
        forgotPin.setOnMouseClicked(e -> info("A PIN reset link has been sent to " + MockData.DEMO_EMAIL + "."));

        GridPane g = new GridPane();
        g.setHgap(8);
        g.setVgap(10);
        g.setPadding(new Insets(14, 10, 8, 16));

        g.add(label("Account"), 0, 0);
        g.add(account, 1, 0);
        g.add(label("Name"), 2, 0);
        g.add(name, 3, 0, 2, 1);
        g.add(label("PIN"), 5, 0);
        g.add(pinField, 6, 0);
        g.add(icons, 7, 0);

        g.add(label("Start Date"), 0, 1);
        g.add(startDate, 1, 1);
        g.add(label("End Date"), 2, 1);
        g.add(endDate, 3, 1);
        g.add(get, 4, 1);
        g.add(forgotPin, 6, 1, 2, 1);
        return g;
    }

    private TableView<WithdrawalRecord> grid() {
        TableView<WithdrawalRecord> t = new TableView<>(rows);
        t.getStyleClass().add("report-grid");
        t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        t.setPlaceholder(new Label("No withdrawal records found for the selected date range."));
        t.getColumns().add(Cells.text("Requested Date", 100, r -> Fmt.date(r.requestedDate)));
        t.getColumns().add(Cells.num("Amount Requested", 115, r -> Fmt.money(r.amountRequested)));
        t.getColumns().add(Cells.num("Amount Approved", 115, r -> Fmt.money(r.amountApproved)));
        t.getColumns().add(Cells.text("Withdrawal Mode", 115, r -> r.mode));
        t.getColumns().add(Cells.text("Comments", 130, r -> r.comments));
        t.getColumns().add(Cells.col("Status", 85, r -> r.status, r -> {
            if ("Approved".equals(r.status)) return List.of("cell-up");
            if ("Declined".equals(r.status)) return List.of("cell-down");
            return List.of("cell-buy");
        }, false));
        return t;
    }

    private Node statusBar() {
        Label today = new Label("Today is " + Fmt.LONG_DATE.format(LocalDate.now()));
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox bar = new HBox(recordsLabel, spacer, today);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.getStyleClass().add("status-bar");
        return bar;
    }

    private void applyFilters() {
        LocalDate from = startDate.getValue();
        LocalDate to = endDate.getValue();
        if (from != null && to != null && from.isAfter(to)) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Start Date must be on or before End Date.");
            a.initOwner(stage);
            a.setHeaderText(null);
            App.themed(a);
            a.showAndWait();
            return;
        }
        rows.setAll(SAMPLE_DATA.stream()
                .filter(r -> r.accountNo.equals(account.getValue()))
                .filter(r -> from == null || !r.requestedDate.isBefore(from))
                .filter(r -> to == null || !r.requestedDate.isAfter(to))
                .collect(Collectors.toList()));
        recordsLabel.setText(rows.size() + (rows.size() == 1 ? " Record found." : " Records found."));
    }

    private void info(String text) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, text);
        a.initOwner(stage);
        a.setHeaderText(null);
        App.themed(a);
        a.showAndWait();
    }

    private static Label label(String text) {
        Label l = new Label(text);
        l.setMinWidth(Region.USE_PREF_SIZE);
        return l;
    }

    private static DatePicker datePicker(LocalDate value) {
        DatePicker d = new DatePicker(value);
        d.setPrefWidth(115);
        d.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) { return Fmt.date(date); }

            @Override
            public LocalDate fromString(String s) {
                try {
                    return s == null || s.isBlank() ? null : LocalDate.parse(s.trim(), Fmt.DATE);
                } catch (Exception e) {
                    return null;
                }
            }
        });
        return d;
    }
}
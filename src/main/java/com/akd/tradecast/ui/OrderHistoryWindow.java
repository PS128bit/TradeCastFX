package com.akd.tradecast.ui;

import com.akd.tradecast.App;
import com.akd.tradecast.data.MockData;
import com.akd.tradecast.model.OrderRecord;
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
import javafx.scene.control.TableView;
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
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Screen 4: Order History (Reports menu, Alt+O).
 * Filters: Account No, Name, Type, Scrip, Start Date, End Date and a "Get" button.
 * Grid: Scrip, Quantity, Gross Rate, Net Amount, Type, Trade Date, Clearing Date.
 * Status bar: number of records found and today's date.
 */
public class OrderHistoryWindow {

    private final Stage stage = new Stage();
    private final List<OrderRecord> all = MockData.orderHistory();
    private final ObservableList<OrderRecord> rows = FXCollections.observableArrayList();

    private final ComboBox<String> account = new ComboBox<>(FXCollections.observableArrayList(MockData.ACCOUNTS.keySet()));
    private final ComboBox<String> name = new ComboBox<>(FXCollections.observableArrayList(MockData.ACCOUNTS.values()));
    private final ComboBox<String> type = new ComboBox<>(FXCollections.observableArrayList("ALL", "BUY", "SELL"));
    private final ComboBox<String> scrip = new ComboBox<>();
    private final DatePicker startDate = datePicker(LocalDate.of(2026, 8, 1));
    private final DatePicker endDate = datePicker(LocalDate.now());
    private final Label recordsLabel = new Label();

    public OrderHistoryWindow(Window owner) {
        stage.initOwner(owner);
        stage.setTitle("Order History");

        BorderPane root = new BorderPane();
        root.setTop(filters());
        root.setCenter(grid());
        root.setBottom(statusBar());
        BorderPane.setMargin(root.getCenter(), new Insets(4, 10, 10, 10));

        stage.setScene(App.themed(new Scene(root, 800, 470)));
        applyFilters();
    }

    public void show() {
        stage.show();
    }

    private Node filters() {
        account.getSelectionModel().selectFirst();
        name.getSelectionModel().selectFirst();
        account.setPrefWidth(160);
        name.setPrefWidth(250);
        // Account and Name always point at the same client.
        account.setOnAction(e -> name.getSelectionModel().select(MockData.ACCOUNTS.get(account.getValue())));
        name.setOnAction(e -> {
            for (var entry : MockData.ACCOUNTS.entrySet()) {
                if (entry.getValue().equals(name.getValue())) account.getSelectionModel().select(entry.getKey());
            }
        });

        type.getSelectionModel().selectFirst();
        type.setPrefWidth(72);
        TreeSet<String> scrips = all.stream().map(OrderRecord::getScrip).collect(Collectors.toCollection(TreeSet::new));
        scrip.getItems().add("ALL");
        scrip.getItems().addAll(scrips);
        scrip.getSelectionModel().selectFirst();
        scrip.setPrefWidth(80);

        Button get = new Button("Get");
        get.setPrefWidth(56);
        get.setDefaultButton(true);
        get.setOnAction(e -> applyFilters());

        Button save = Icons.button(Icons.save(), "Save");
        Button pdf = Icons.button(Icons.pdf(), "Export to PDF");
        Button print = Icons.button(Icons.print(), "Print");
        Button cols = Icons.button(Icons.grid(), "Columns");
        for (Button b : new Button[]{save, pdf, print, cols}) {
            b.setOnAction(e -> notAvailable(((javafx.scene.control.Tooltip) b.getTooltip()).getText()));
        }
        HBox icons = new HBox(2, save, pdf, print, cols);

        GridPane g = new GridPane();
        g.setHgap(8);
        g.setVgap(10);
        g.setPadding(new Insets(14, 10, 8, 16));
        g.add(label("Account No"), 0, 0);
        g.add(account, 1, 0, 3, 1);
        g.add(label("Name"), 4, 0);
        g.add(name, 5, 0, 3, 1);
        g.add(icons, 8, 0);

        g.add(label("Type"), 0, 1);
        g.add(type, 1, 1);
        g.add(label("Scrip"), 2, 1);
        g.add(scrip, 3, 1);
        g.add(label("Start Date"), 4, 1);
        g.add(startDate, 5, 1);
        g.add(label("End Date"), 6, 1);
        g.add(endDate, 7, 1);
        g.add(get, 8, 1);
        return g;
    }

    private TableView<OrderRecord> grid() {
        TableView<OrderRecord> t = new TableView<>(rows);
        t.getStyleClass().add("report-grid");
        t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        t.setPlaceholder(new Label("No records found for the selected filters."));
        t.getColumns().add(Cells.text("Scrip", 80, OrderRecord::getScrip));
        t.getColumns().add(Cells.num("Quantity", 70, r -> Fmt.qtyAlways(r.getQuantity())));
        t.getColumns().add(Cells.num("Gross Rate", 80, r -> Fmt.money(r.getGrossRate())));
        t.getColumns().add(Cells.num("Net Amount", 95, r -> Fmt.money(r.getNetAmount())));
        t.getColumns().add(Cells.col("Type", 50, OrderRecord::getType,
                r -> List.of(r.getType().equals("BUY") ? "cell-buy" : "cell-sell"), false));
        t.getColumns().add(Cells.text("Trade Date", 95, r -> Fmt.date(r.getTradeDate())));
        t.getColumns().add(Cells.text("Clearing Date", 95, r -> Fmt.date(r.getClearingDate())));
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

    /** "Get": keeps the records that match every filter. */
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
        rows.setAll(all.stream()
                .filter(r -> r.getAccountNo().equals(account.getValue()))
                .filter(r -> "ALL".equals(type.getValue()) || r.getType().equals(type.getValue()))
                .filter(r -> "ALL".equals(scrip.getValue()) || r.getScrip().equals(scrip.getValue()))
                .filter(r -> from == null || !r.getTradeDate().isBefore(from))
                .filter(r -> to == null || !r.getTradeDate().isAfter(to))
                .collect(Collectors.toList()));
        recordsLabel.setText(rows.size() + (rows.size() == 1 ? " Record found." : " Records found."));
    }

    private static Label label(String text) {
        Label l = new Label(text);
        l.setMinWidth(Region.USE_PREF_SIZE);
        return l;
    }

    private void notAvailable(String what) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, what + " is not available in this demo (no backend).");
        a.initOwner(stage);
        a.setHeaderText(null);
        App.themed(a);
        a.showAndWait();
    }

    private static DatePicker datePicker(LocalDate value) {
        DatePicker d = new DatePicker(value);
        d.setPrefWidth(115);
        d.setConverter(new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return Fmt.date(date);
            }

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

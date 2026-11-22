package com.akd.tradecast.ui.azan;

import com.akd.tradecast.App;
import com.akd.tradecast.data.MockData;
import com.akd.tradecast.model.Quote;
import com.akd.tradecast.ui.Fmt;
import com.akd.tradecast.ui.Icons;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/** Screen 7 (part): Market Depth By Order / By Price panels docked on the right side of MainWindow. */
public final class MarketDepthPane {
    private MarketDepthPane() { }

    public enum Kind { BY_ORDER, BY_PRICE }

    private static final class DepthRow {
        final String bidFlag;
        final long bidVolume;
        final double bidPrice;
        final double askPrice;
        final long askVolume;
        final String askFlag;

        DepthRow(String bidFlag, long bidVolume, double bidPrice,
                 double askPrice, long askVolume, String askFlag) {
            this.bidFlag = bidFlag;
            this.bidVolume = bidVolume;
            this.bidPrice = bidPrice;
            this.askPrice = askPrice;
            this.askVolume = askVolume;
            this.askFlag = askFlag;
        }
    }

    public static Node create(Kind kind) {
        Map<String, Quote> universe = MockData.universe();
        ObservableList<DepthRow> rows = FXCollections.observableArrayList();

        ComboBox<String> market = new ComboBox<>(FXCollections.observableArrayList("REG", "FUT", "ODL"));
        market.getSelectionModel().selectFirst();
        market.setPrefWidth(56);

        TextField scripField = new TextField("ANL");
        scripField.setPrefWidth(58);

        TextField lTradeField = new TextField("4.49");
        lTradeField.setEditable(false);
        lTradeField.setFocusTraversable(false);
        lTradeField.setPrefWidth(56);
        lTradeField.getStyleClass().add("readonly");

        Label footer = new Label("Azgard Nine Limited. - Personal Goods");
        footer.setStyle("-fx-font-weight: bold; -fx-font-size: 10px;");
        footer.setPadding(new Insets(3, 6, 3, 6));

        Runnable loadScrip = () -> {
            String sym = scripField.getText().trim().toUpperCase();
            Quote q = universe.get(sym);
            double base = q != null ? q.getLastPrice() : 4.49;
            lTradeField.setText(Fmt.money(base));
            footer.setText(q != null ? q.getCompanyName() : "Azgard Nine Limited. - Personal Goods");
            rows.setAll(kind == Kind.BY_ORDER ? sampleByOrder(base) : sampleByPrice(base));
        };

        scripField.setOnAction(e -> loadScrip.run());

        Button details = new Button("Details", Icons.star());
        details.getStyleClass().add("icon");
        details.setOnAction(e -> {
            String sym = scripField.getText().trim().toUpperCase();
            Quote q = universe.get(sym);
            String msg = q == null ? "No quote data for " + sym
                    : q.getCompanyName() + "\nLast Trade: Rs. " + Fmt.money(q.getLastPrice())
                    + "\nHigh: " + Fmt.money(q.getHigh()) + "   Low: " + Fmt.money(q.getLow())
                    + "\nTotal Volume: " + Fmt.qtyAlways(q.getTotalVolume());
            Alert a = new Alert(Alert.AlertType.INFORMATION, msg);
            a.setTitle("Details - " + sym);
            a.setHeaderText(null);
            App.themed(a);
            a.showAndWait();
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox topBar = new HBox(4, new Label("Market"), market, new Label("Scrip"), scripField,
                new Label("L.Trade"), lTradeField, spacer, details);
        topBar.getStyleClass().add("toolbar-strip");
        topBar.setAlignment(Pos.CENTER_LEFT);

        TableView<DepthRow> table = new TableView<>(rows);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().add(depthCol("Flag", 36, r -> r.bidFlag, true, false));
        table.getColumns().add(depthCol("Volume", 62, r -> Fmt.qtyAlways(r.bidVolume), true, true));
        table.getColumns().add(depthCol("Price", 54, r -> Fmt.money(r.bidPrice), true, true));
        table.getColumns().add(depthCol("Price", 54, r -> Fmt.money(r.askPrice), false, true));
        table.getColumns().add(depthCol("Volume", 62, r -> Fmt.qtyAlways(r.askVolume), false, true));
        table.getColumns().add(depthCol("Flag", 36, r -> r.askFlag, false, false));

        loadScrip.run();

        BorderPane pane = new BorderPane(table, topBar, null, footer, null);
        return pane;
    }

    private static TableColumn<DepthRow, DepthRow> depthCol(String title, double width,
                                                            Function<DepthRow, String> text,
                                                            boolean bidSide, boolean rightAlign) {
        TableColumn<DepthRow, DepthRow> col = new TableColumn<>(title);
        col.setPrefWidth(width);
        col.setSortable(false);
        col.setCellValueFactory(cd -> new ReadOnlyObjectWrapper<>(cd.getValue()));
        col.setCellFactory(c -> new TableCell<DepthRow, DepthRow>() {
            @Override
            protected void updateItem(DepthRow row, boolean empty) {
                super.updateItem(row, empty);
                if (empty || row == null) {
                    setText(null);
                    setStyle("");
                    return;
                }
                setText(text.apply(row));
                setAlignment(rightAlign ? Pos.CENTER_RIGHT : Pos.CENTER);
                String bg = bidSide ? "#B8EBF5" : "#F3D3D8";
                String fg = bidSide ? "#003366" : "#660022";
                setStyle("-fx-background-color: " + bg + "; -fx-text-fill: " + fg
                        + "; -fx-border-color: #FFFFFF; -fx-border-width: 0 1 1 0;");
            }
        });
        return col;
    }

    private static List<DepthRow> sampleByOrder(double base) {
        return List.of(
                new DepthRow("dc", 100, base - 0.03, base, 925, "dc"),
                new DepthRow("dc", 10000, base - 0.03, base, 2000, "dc"),
                new DepthRow("dc", 1530, base - 0.04, base, 5000, "dc"),
                new DepthRow("dc", 1000, base - 0.04, base, 300, "dc"),
                new DepthRow("dc", 10, base - 0.04, base + 0.01, 3582, "dc"),
                new DepthRow("dc", 1400, base - 0.04, base + 0.03, 2000, "dc"),
                new DepthRow("dc", 50, base - 0.06, base + 0.03, 7500, "dc"),
                new DepthRow("dc", 1000, base - 0.07, base + 0.05, 5000, "dc"),
                new DepthRow("dc", 500, base - 0.07, base + 0.06, 25000, "dc"),
                new DepthRow("dc", 200, base - 0.07, base + 0.08, 15000, "dc")
        );
    }

    private static List<DepthRow> sampleByPrice(double base) {
        return List.of(
                new DepthRow("2", 10100, base - 0.03, base, 8225, "4"),
                new DepthRow("4", 3940, base - 0.04, base + 0.01, 3582, "1"),
                new DepthRow("1", 50, base - 0.06, base + 0.03, 9500, "2"),
                new DepthRow("4", 6700, base - 0.07, base + 0.05, 5000, "1"),
                new DepthRow("1", 300, base - 0.08, base + 0.06, 25000, "1"),
                new DepthRow("13", 19200, base - 0.09, base + 0.08, 15000, "1"),
                new DepthRow("1", 50, base - 0.11, base + 0.10, 5071, "2"),
                new DepthRow("2", 2550, base - 0.12, base + 0.11, 9150, "5"),
                new DepthRow("3", 8500, base - 0.13, base + 0.14, 5000, "1"),
                new DepthRow("10", 55802, base - 0.14, base + 0.15, 5000, "1")
        );
    }
}
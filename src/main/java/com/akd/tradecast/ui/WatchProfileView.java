package com.akd.tradecast.ui;

import com.akd.tradecast.App;
import com.akd.tradecast.model.Quote;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
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

/**
 * Screen 2: one watch-list tab of My Watches (1st / 2nd / 3rd Profile, Sector Watch ...).
 * Scrip search bar on top, dense market grid below with a Trade button on every row.
 */
public class WatchProfileView extends BorderPane {

    private final String profileName;
    private final Map<String, Quote> universe;
    private final ObservableList<Quote> rows = FXCollections.observableArrayList();
    private final TableView<Quote> table = new TableView<>(rows);
    private final TextField scripField = new TextField();
    private final Label companyLabel = new Label();

    public WatchProfileView(String profileName, List<String> scrips, Map<String, Quote> universe) {
        this.profileName = profileName;
        this.universe = universe;
        for (String s : scrips) {
            Quote q = universe.get(s);
            if (q != null) rows.add(q);
        }

        setTop(searchBar());
        setCenter(buildTable());
        if (!rows.isEmpty()) showCompany(rows.get(rows.size() > 8 ? 8 : 0));
    }

    private HBox searchBar() {
        Button browse = new Button("Scrip Browse", Icons.browse());
        browse.getStyleClass().addAll("icon");
        browse.setStyle("-fx-font-weight: bold;");
        browse.setOnAction(e -> scripField.requestFocus());

        Label scripLabel = new Label("Scrip", Icons.search());
        scripLabel.setStyle("-fx-font-weight: bold;");
        scripLabel.setContentDisplay(javafx.scene.control.ContentDisplay.RIGHT);
        scripField.setPrefWidth(70);
        scripField.setPromptText("e.g. HUBC");
        scripField.setOnAction(e -> addScrip(scripField.getText()));

        companyLabel.getStyleClass().add("company-label");

        Button save = new Button("Save Profile", Icons.save());
        save.getStyleClass().add("icon");
        save.setOnAction(e -> info("Save Profile", profileName + " saved with " + rows.size() + " scrips."));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox bar = new HBox(6, browse, scripLabel, scripField, companyLabel, spacer, save);
        bar.getStyleClass().add("toolbar-strip");
        return bar;
    }

    private TableView<Quote> buildTable() {
        table.getStyleClass().add("market-grid");
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        table.setPlaceholder(new Label("Type a scrip in the Scrip box and press Enter to add it."));

        table.getColumns().add(Cells.text("Mkt", 36, Quote::getMarket));
        table.getColumns().add(Cells.text("Scrip", 74, Quote::getScrip));
        table.getColumns().add(Cells.num("LastPrice", 66, q -> Fmt.price(q.getLastPrice())));
        table.getColumns().add(Cells.col("Change", 60, q -> Fmt.money(q.getChange()),
                q -> Cells.upDown(q.getChange()), true));
        table.getColumns().add(Cells.col("Buy", 64, q -> Fmt.price(q.getBuy()),
                q -> List.of("cell-buy"), true));
        table.getColumns().add(Cells.col("BVol", 62, q -> Fmt.qty(q.getBuyVolume()),
                q -> q.isBuyFlash() ? List.of("cell-flash-buy") : List.of(), true));
        table.getColumns().add(Cells.col("Sell", 64, q -> Fmt.price(q.getSell()),
                q -> List.of("cell-sell"), true));
        table.getColumns().add(Cells.col("SVol", 66, q -> Fmt.qty(q.getSellVolume()),
                q -> q.isSellFlash() ? List.of("cell-flash-sell", "cell-sell") : List.of("cell-sell"), true));
        table.getColumns().add(Cells.num("High", 60, q -> Fmt.price(q.getHigh())));
        table.getColumns().add(Cells.num("Low", 60, q -> Fmt.price(q.getLow())));
        table.getColumns().add(Cells.num("TotalVolume", 84, q -> Fmt.qtyAlways(q.getTotalVolume())));
        table.getColumns().add(Cells.num("Average", 64, q -> Fmt.price(q.getAverage())));
        table.getColumns().add(Cells.num("ClosePrice", 72, q -> Fmt.price(q.getClosePrice())));
        table.getColumns().add(tradeColumn());

        table.getSelectionModel().selectedItemProperty().addListener((o, old, q) -> { if (q != null) showCompany(q); });

        MenuItem remove = new MenuItem("Remove Scrip");
        remove.setOnAction(e -> {
            Quote q = table.getSelectionModel().getSelectedItem();
            if (q != null) rows.remove(q);
        });
        table.setContextMenu(new ContextMenu(remove));
        return table;
    }

    /** Last column: a "Trade" button on every row that opens the order dialog for that scrip. */
    private TableColumn<Quote, Quote> tradeColumn() {
        TableColumn<Quote, Quote> c = new TableColumn<>("Trade");
        c.setPrefWidth(66);
        c.setSortable(false);
        c.setCellValueFactory(cd -> new javafx.beans.property.ReadOnlyObjectWrapper<>(cd.getValue()));
        c.setCellFactory(col -> new TableCell<Quote, Quote>() {
            private final Button button = new Button("Trade");
            {
                button.getStyleClass().add("trade-button");
                button.setFocusTraversable(false);
                button.setOnAction(e -> new TradeDialog(getScene().getWindow(), getItem()).showAndWait());
            }

            @Override
            protected void updateItem(Quote q, boolean empty) {
                super.updateItem(q, empty);
                setGraphic(empty || q == null ? null : button);
                setStyle("-fx-alignment: center;");
            }
        });
        return c;
    }

    private void addScrip(String text) {
        String symbol = text == null ? "" : text.trim().toUpperCase();
        if (symbol.isEmpty()) return;
        Quote q = universe.get(symbol);
        if (q == null) {
            info("Scrip Browse", "Scrip \"" + symbol + "\" was not found.");
            return;
        }
        if (!rows.contains(q)) rows.add(q);
        table.getSelectionModel().select(q);
        table.scrollTo(q);
        scripField.clear();
    }

    private void showCompany(Quote q) {
        companyLabel.setText(q.getCompanyName() + " Sector");
    }

    /** Called by MyWatchesView on every market tick. */
    public void refresh() {
        table.refresh();
    }

    public List<Quote> getQuotes() {
        return rows;
    }

    private void info(String title, String text) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, text);
        a.initOwner(getScene().getWindow());
        a.setTitle(title);
        a.setHeaderText(null);
        App.themed(a);
        a.showAndWait();
    }
}

package com.akd.tradecast.ui;

import com.akd.tradecast.App;
import com.akd.tradecast.data.MockData;
import com.akd.tradecast.model.Holding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Screen 3: Portfolio Watch.
 * Top: Scrip Worth (selected holding), Security Holdings totals with "Liquidate All Security Holdings",
 * Portfolio Worth summary (Portfolio Worth, Ledger Balance, Sold Collaterals, Net Worth).
 * Bottom: the Security Holdings grid.
 */
public class PortfolioWatchView extends BorderPane {

    private final List<Holding> allHoldings = MockData.holdings();
    private final ObservableList<Holding> rows = FXCollections.observableArrayList(allHoldings);
    private final TableView<Holding> table = new TableView<>(rows);

    // Scrip Worth (selected holding)
    private final TextField netQty = readOnly();
    private final TextField avgPrice = readOnly();
    private final TextField investment = readOnly();
    private final TextField currentPrice = readOnly();
    private final TextField currentWorth = readOnly();
    private final TextField scripPL = readOnly();
    private final PieChart scripPie = pie();
    private final Label scripLegend = legend("ENGRO", "#F2B705");
    private final Label otherLegend = legend("Other Security Holdings", "#5DBB63");
    private final Button liquidateScrip = new Button("Liquidate");
    private final RadioButton byPrice = new RadioButton("By Price");

    // Security Holdings totals
    private final TextField holdingsInvestment = readOnly();
    private final TextField holdingsWorth = readOnly();
    private final TextField holdingsPL = readOnly();

    // Portfolio Worth summary
    private final TextField portfolioWorth = readOnly();
    private final TextField ledgerBalance = readOnly();
    private final TextField portfolioPL = readOnly();
    private final TextField soldCollaterals = readOnly();
    private final TextField netWorth = readOnly();
    private final PieChart portfolioPie = pie();

    private final TextField scripField = new TextField();
    private final Label companyLabel = new Label();

    public PortfolioWatchView() {
        setTop(new VBox(topBar(), summary()));
        setCenter(buildTable());
        recalculateTotals();
        Holding engro = allHoldings.stream().filter(h -> h.getScrip().equals("ENGRO")).findFirst().orElse(allHoldings.get(0));
        table.getSelectionModel().select(engro);
    }

    // ------------------------------------------------------------ top bar

    private Node topBar() {
        ComboBox<String> account = new ComboBox<>(FXCollections.observableArrayList(MockData.ACCOUNTS.keySet()));
        account.getSelectionModel().selectFirst();
        account.setPrefWidth(90);

        scripField.setPrefWidth(60);
        scripField.setOnAction(e -> selectScrip(scripField.getText()));
        companyLabel.getStyleClass().add("company-label");

        ToggleGroup mode = new ToggleGroup();
        RadioButton security = new RadioButton("Security Holdings");
        RadioButton session = new RadioButton("Session Holdings");
        security.setToggleGroup(mode);
        session.setToggleGroup(mode);
        security.setSelected(true);
        mode.selectedToggleProperty().addListener((o, a, b) -> {
            // Session Holdings = only scrips traded in today's session.
            if (b == session) {
                rows.setAll(allHoldings.stream().filter(h -> !h.getLastTradeTime().equals("--")).collect(Collectors.toList()));
            } else {
                rows.setAll(allHoldings);
            }
            recalculateTotals();
        });

        Button refresh = Icons.button(Icons.refresh(), "Refresh");
        refresh.setOnAction(e -> recalculateTotals());
        Button details = new Button("Details", Icons.star());
        details.getStyleClass().add("icon");
        details.setOnAction(e -> showDetails());

        Label accountLabel = new Label("Account");
        accountLabel.setStyle("-fx-font-weight: bold;");
        Label scripLabel = new Label("Scrip", Icons.search());
        scripLabel.setStyle("-fx-font-weight: bold;");
        scripLabel.setContentDisplay(javafx.scene.control.ContentDisplay.RIGHT);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox bar = new HBox(6, accountLabel, account, scripLabel, scripField, companyLabel, spacer,
                security, session, refresh, details);
        bar.getStyleClass().add("toolbar-strip");
        return bar;
    }

    // ------------------------------------------------------------ summary panel

    private Node summary() {
        // Scrip Worth
        GridPane scripGrid = form(
                "Net Quantity", netQty, "Avg. Price   Rs.", avgPrice, "Investment   Rs.", investment,
                "Current Price Rs.", currentPrice, "Current Worth Rs.", currentWorth, "Profit / Loss  Rs.", scripPL);
        ToggleGroup pieMode = new ToggleGroup();
        RadioButton byVolume = new RadioButton("By Volume");
        byPrice.setToggleGroup(pieMode);
        byVolume.setToggleGroup(pieMode);
        byPrice.setSelected(true);
        pieMode.selectedToggleProperty().addListener((o, a, b) -> updateScripWorth(table.getSelectionModel().getSelectedItem()));
        liquidateScrip.setOnAction(e -> {
            Holding h = table.getSelectionModel().getSelectedItem();
            if (h != null) confirmLiquidate(List.of(h));
        });
        VBox scripChart = new VBox(2, new HBox(6, byPrice, byVolume), scripLegend, scripPie, otherLegend, liquidateScrip);
        scripChart.setAlignment(Pos.TOP_CENTER);
        Node scripBox = group("Scrip Worth", new HBox(10, scripGrid, scripChart));

        // Security Holdings
        GridPane holdingsGrid = form("Investment   Rs.", holdingsInvestment, "Current Worth Rs.", holdingsWorth,
                "Profit / Loss  Rs.", holdingsPL);
        holdingsGrid.setVgap(14);
        Button liquidateAll = new Button("Liquidate All Security Holdings");
        liquidateAll.setOnAction(e -> confirmLiquidate(rows));
        VBox holdingsCol = new VBox(16, holdingsGrid, liquidateAll);
        holdingsCol.setAlignment(Pos.TOP_CENTER);
        Node holdingsBox = group("Security Holdings", holdingsCol);

        // Portfolio Worth
        GridPane worthGrid = form("Portfolio Worth Rs.", portfolioWorth, "Ledger Balance Rs.", ledgerBalance,
                "Profit / Loss  Rs.", portfolioPL, "Sold Collaterals Rs.", soldCollaterals);
        GridPane netGrid = form("Net Worth       Rs.", netWorth);
        netWorth.setStyle("-fx-font-weight: bold;");
        VBox worthCol = new VBox(14, worthGrid, netGrid);
        VBox portfolioChart = new VBox(2, legend("Cash", "#F2B705"), portfolioPie, legend("Security Holdings", "#5DBB63"));
        Node worthBox = group("Portfolio Worth", new HBox(10, worthCol, portfolioChart));

        HBox panel = new HBox(0, scripBox, holdingsBox, worthBox);
        panel.setPadding(new Insets(4, 4, 6, 4));
        panel.setStyle("-fx-background-color: #F7F6F1; -fx-border-color: #C7C5B2; -fx-border-width: 0 0 1 0;");
        return panel;
    }

    private Node group(String title, Node content) {
        Label t = new Label(title);
        t.getStyleClass().add("group-box-title");
        t.setMaxWidth(Double.MAX_VALUE);
        VBox box = new VBox(6, t, content);
        box.getStyleClass().add("group-box");
        return box;
    }

    /** Two-column label/field grid built from alternating (label, field) arguments. */
    private GridPane form(Object... pairs) {
        GridPane g = new GridPane();
        g.setHgap(6);
        g.setVgap(5);
        for (int i = 0; i < pairs.length; i += 2) {
            g.add(new Label((String) pairs[i]), 0, i / 2);
            g.add((Node) pairs[i + 1], 1, i / 2);
        }
        return g;
    }

    // ------------------------------------------------------------ holdings grid

    private TableView<Holding> buildTable() {
        table.getStyleClass().add("market-grid");
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        table.getColumns().add(Cells.text("Mkt", 36, Holding::getMarket));
        table.getColumns().add(Cells.text("Scrip", 64, Holding::getScrip));
        table.getColumns().add(Cells.num("Net Qty", 60, h -> Fmt.qtyAlways(h.getNetQuantity())));
        table.getColumns().add(Cells.num("Avg Price", 64, h -> Fmt.money(h.getAvgPrice())));
        table.getColumns().add(Cells.num("Current Price", 78, h -> Fmt.money(h.getCurrentPrice())));
        table.getColumns().add(Cells.num("Current Worth", 86, h -> Fmt.money(h.getCurrentWorth())));
        table.getColumns().add(Cells.col("Profit/Loss", 80, h -> Fmt.money(h.getProfitLoss()),
                h -> Cells.upDown(h.getProfitLoss()), true));
        table.getColumns().add(Cells.col("Change", 54, h -> Fmt.money(h.getChange()),
                h -> Cells.upDown(h.getChange()), true));
        table.getColumns().add(Cells.col("BVol", 58, h -> Fmt.qty(h.getBuyVolume()), h -> List.of("cell-buy"), true));
        table.getColumns().add(Cells.col("Buy", 56, h -> Fmt.price(h.getBuy()), h -> List.of("cell-buy"), true));
        table.getColumns().add(Cells.col("Sell", 56, h -> Fmt.price(h.getSell()), h -> List.of("cell-sell"), true));
        table.getColumns().add(Cells.col("SVol", 58, h -> Fmt.qty(h.getSellVolume()), h -> List.of("cell-sell"), true));
        table.getColumns().add(Cells.col("Dir", 30, Holding::getDirection, h -> Cells.upDown(h.getChange()), false));
        table.getColumns().add(Cells.text("LTrdTime", 64, Holding::getLastTradeTime));
        table.getColumns().add(Cells.num("TotalVolume", 80, h -> Fmt.qty(h.getTotalVolume())));
        table.getColumns().add(Cells.num("High", 54, h -> Fmt.price(h.getHigh())));
        table.getColumns().add(Cells.num("Low", 54, h -> Fmt.price(h.getLow())));
        table.getColumns().add(Cells.num("ClosePrice", 68, h -> Fmt.price(h.getClosePrice())));
        table.getColumns().add(Cells.text("State", 40, Holding::getState));
        table.getSelectionModel().selectedItemProperty().addListener((o, a, h) -> updateScripWorth(h));
        return table;
    }

    // ------------------------------------------------------------ calculations

    private void updateScripWorth(Holding h) {
        if (h == null) return;
        companyLabel.setText(h.getCompanyName() + " Sector");
        scripField.setText(h.getScrip());
        netQty.setText(Fmt.qtyAlways(h.getNetQuantity()));
        avgPrice.setText(Fmt.money(h.getAvgPrice()));
        investment.setText(Fmt.money(h.getInvestment()));
        currentPrice.setText(Fmt.money(h.getCurrentPrice()));
        currentWorth.setText(Fmt.money(h.getCurrentWorth()));
        setProfitLoss(scripPL, h.getProfitLoss());
        liquidateScrip.setText("Liquidate " + h.getScrip());
        scripLegend.setText(h.getScrip());

        double mine = byPrice.isSelected() ? h.getCurrentWorth() : h.getNetQuantity();
        double total = rows.stream().mapToDouble(x -> byPrice.isSelected() ? x.getCurrentWorth() : x.getNetQuantity()).sum();
        scripPie.setData(FXCollections.observableArrayList(
                new PieChart.Data(h.getScrip(), mine),
                new PieChart.Data("Other Security Holdings", Math.max(0, total - mine))));
    }

    /** Totals for Security Holdings and Portfolio Worth (all read-only, calculated from the grid). */
    private void recalculateTotals() {
        double inv = rows.stream().mapToDouble(Holding::getInvestment).sum();
        double worth = rows.stream().mapToDouble(Holding::getCurrentWorth).sum();
        double pl = worth - inv;
        double ledger = MockData.LEDGER_BALANCE;
        double sold = MockData.SOLD_COLLATERALS;

        holdingsInvestment.setText(Fmt.money(inv));
        holdingsWorth.setText(Fmt.money(worth));
        setProfitLoss(holdingsPL, pl);

        portfolioWorth.setText(Fmt.money(worth));
        ledgerBalance.setText(Fmt.money(ledger));
        setProfitLoss(portfolioPL, pl);
        soldCollaterals.setText(Fmt.money(sold));
        netWorth.setText(Fmt.money(worth + ledger - sold));

        portfolioPie.setData(FXCollections.observableArrayList(
                new PieChart.Data("Cash", Math.max(ledger, worth * 0.02)),
                new PieChart.Data("Security Holdings", worth)));
        updateScripWorth(table.getSelectionModel().getSelectedItem());
    }

    private void setProfitLoss(TextField f, double v) {
        f.setText(Fmt.money(v));
        f.getStyleClass().removeAll("profit", "loss");
        f.getStyleClass().add(v >= 0 ? "profit" : "loss");
    }

    // ------------------------------------------------------------ actions

    private void selectScrip(String text) {
        String s = text == null ? "" : text.trim().toUpperCase();
        for (Holding h : rows) {
            if (h.getScrip().equals(s)) {
                table.getSelectionModel().select(h);
                table.scrollTo(h);
                return;
            }
        }
        alert(Alert.AlertType.INFORMATION, "Portfolio Watch", "You do not hold any " + s + " shares.");
    }

    private void confirmLiquidate(List<Holding> holdings) {
        if (holdings.isEmpty()) return;
        double value = holdings.stream().mapToDouble(Holding::getCurrentWorth).sum();
        String what = holdings.size() == 1 ? "all " + Fmt.qtyAlways(holdings.get(0).getNetQuantity()) + " "
                + holdings.get(0).getScrip() + " shares" : "all " + holdings.size() + " security holdings";
        Alert c = new Alert(Alert.AlertType.CONFIRMATION,
                "Place market sell orders for " + what + " (current worth Rs. " + Fmt.money(value) + ")?",
                ButtonType.YES, ButtonType.NO);
        c.initOwner(getScene().getWindow());
        c.setTitle("Liquidate");
        c.setHeaderText(null);
        App.themed(c);
        Optional<ButtonType> answer = c.showAndWait();
        if (answer.isPresent() && answer.get() == ButtonType.YES) {
            alert(Alert.AlertType.INFORMATION, "Liquidate",
                    holdings.size() + " sell order(s) sent to the exchange.");
        }
    }

    private void showDetails() {
        Holding h = table.getSelectionModel().getSelectedItem();
        if (h == null) return;
        alert(Alert.AlertType.INFORMATION, "Details - " + h.getScrip(),
                h.getCompanyName() + "\n\nNet quantity: " + Fmt.qtyAlways(h.getNetQuantity())
                        + "\nAverage price: Rs. " + Fmt.money(h.getAvgPrice())
                        + "\nCurrent price: Rs. " + Fmt.money(h.getCurrentPrice())
                        + "\nProfit / Loss: Rs. " + Fmt.money(h.getProfitLoss()));
    }

    private void alert(Alert.AlertType type, String title, String text) {
        Alert a = new Alert(type, text);
        a.initOwner(getScene().getWindow());
        a.setTitle(title);
        a.setHeaderText(null);
        App.themed(a);
        a.showAndWait();
    }

    private static TextField readOnly() {
        TextField f = new TextField();
        f.setEditable(false);
        f.setFocusTraversable(false);
        f.setPrefWidth(88);
        f.getStyleClass().add("readonly");
        return f;
    }

    private static PieChart pie() {
        PieChart p = new PieChart();
        p.setLabelsVisible(false);
        p.setLegendVisible(false);
        p.setAnimated(false);
        p.setStartAngle(90);
        p.getStyleClass().add("worth-pie");
        p.setPrefSize(130, 110);
        p.setMinSize(130, 110);
        p.setMaxSize(130, 110);
        return p;
    }

    /** Small coloured square + text, used as a legend above/below each pie. */
    private static Label legend(String text, String colour) {
        javafx.scene.shape.Rectangle sq = new javafx.scene.shape.Rectangle(8, 8, javafx.scene.paint.Color.web(colour));
        Label l = new Label(text, sq);
        l.setStyle("-fx-font-size: 10px;");
        return l;
    }
}

package com.akd.tradecast.ui;

import com.akd.tradecast.App;
import com.akd.tradecast.ui.azan.CashWithdrawalRequestWindow;
import com.akd.tradecast.ui.azan.CashWithdrawalStatementWindow;
import com.akd.tradecast.ui.azan.MarketDepthPane;
import com.akd.tradecast.ui.azan.MessageWindowPane;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Main trading window. Docked internal panels:
 * <pre>
 *  +-----------------------------+------------------------+
 *  | My Watches | Trade Log ...  | Market Depth By Order  |
 *  |                             +------------------------+
 *  +-----------------------------+ Market Depth By Price  |
 *  | Message Window              |                        |
 *  +-----------------------------+------------------------+
 * </pre>
 * Screens 2 and 3 live in the My Watches panel. Screens 4 to 6 open as separate
 * windows from the menus. Screen 7 is this whole layout with Market Depth and
 * the Message Window, plus Restore Default Layout (Ctrl+R).
 */
public class MainWindow {

    /** Default divider positions used by Restore Default Layout. */
    private static final double MAIN_DIVIDER = 0.67;
    private static final double LEFT_DIVIDER = 0.72;
    private static final double RIGHT_DIVIDER = 0.5;

    private final Stage stage;
    private final String userId;
    private final BorderPane root = new BorderPane();

    private final MyWatchesView watches = new MyWatchesView();
    private final InternalPanel watchesPanel = new InternalPanel();
    private final InternalPanel messagePanel = new InternalPanel();
    private final InternalPanel depthByOrderPanel = new InternalPanel();
    private final InternalPanel depthByPricePanel = new InternalPanel();

    private final SplitPane mainSplit = new SplitPane();
    private final SplitPane leftSplit = new SplitPane();
    private final SplitPane rightSplit = new SplitPane();
    private InternalPanel maximised;
    private final Label clock = new Label();

    public MainWindow(Stage stage, String userId) {
        this.stage = stage;
        this.userId = userId;
    }

    public void show() {
        watchesPanel.addWindow("My Watches", watches)
                .addWindow("Trade Log", SimpleLogs.tradeLog())
                .addWindow("Outstanding Log", SimpleLogs.outstandingLog())
                .addWindow("Activity Log", SimpleLogs.activityLog());
        messagePanel.addWindow("Message Window", MessageWindowPane.create());
        depthByOrderPanel.addWindow("Market Depth By Order", MarketDepthPane.create(MarketDepthPane.Kind.BY_ORDER));
        depthByPricePanel.addWindow("Market Depth By Price", MarketDepthPane.create(MarketDepthPane.Kind.BY_PRICE));

        for (InternalPanel p : new InternalPanel[]{watchesPanel, messagePanel, depthByOrderPanel, depthByPricePanel}) {
            p.setOnMaximise(this::toggleMaximise);
            p.setOnClose(panel -> restoreDefaultLayout());
        }

        leftSplit.setOrientation(Orientation.VERTICAL);
        rightSplit.setOrientation(Orientation.VERTICAL);
        restoreDefaultLayout();

        root.setTop(new VBox(menuBar(), toolbar()));
        root.setBottom(statusBar());

        Scene scene = App.themed(new Scene(root, 1280, 760));
        // Ctrl+R also works when focus is inside a table (menu accelerators can be swallowed there).
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.R, KeyCombination.SHORTCUT_DOWN),
                this::restoreDefaultLayout);

        stage.setTitle("AKD TradeCast  -  " + userId);
        stage.setScene(scene);
        stage.setOnCloseRequest(e -> Platform.exit());
        stage.show();
        startClock();
    }

    // ------------------------------------------------------------------ menus

    private MenuBar menuBar() {
        MenuItem logout = new MenuItem("Logout");
        logout.setOnAction(e -> logout());
        MenuItem exit = new MenuItem("Exit");
        exit.setOnAction(e -> Platform.exit());
        Menu file = new Menu("File", null, logout, new SeparatorMenuItem(), exit);

        MenuItem myWatches = item("My Watches", KeyCode.F12, null, this::showMyWatches);
        MenuItem portfolio = new MenuItem("Portfolio Watch");
        portfolio.setOnAction(e -> {
            showMyWatches();
            watches.selectTab(MyWatchesView.PORTFOLIO_WATCH);
        });
        Menu market = new Menu("Market Monitor", null, myWatches, portfolio);

        MenuItem orderHistory = item("Order History", KeyCode.O, KeyCombination.ALT_DOWN,
                () -> new OrderHistoryWindow(stage).show());
        MenuItem statement = item("Cash Withdrawal Statement", KeyCode.W, KeyCombination.ALT_DOWN,
                () -> CashWithdrawalStatementWindow.show(stage));
        Menu reports = new Menu("Reports", null, orderHistory, statement);

        MenuItem request = item("Cash Withdrawal Request", KeyCode.R, KeyCombination.ALT_DOWN,
                () -> CashWithdrawalRequestWindow.show(stage));
        Menu services = new Menu("Services", null, request);

        MenuItem restore = item("Restore Default Layout", KeyCode.R, KeyCombination.SHORTCUT_DOWN,
                this::restoreDefaultLayout);
        Menu tools = new Menu("Tools", null, restore);

        MenuItem about = new MenuItem("About TradeCast");
        about.setOnAction(e -> info("About", "AKD TradeCast v1.4.0\nJavaFX frontend replica - SDA Assignment 2"));
        Menu help = new Menu("Help", null, about);

        return new MenuBar(file, market, reports, services, tools, help);
    }

    private MenuItem item(String text, KeyCode key, KeyCombination.Modifier modifier, Runnable action) {
        MenuItem mi = new MenuItem(text);
        mi.setAccelerator(modifier == null ? new KeyCodeCombination(key) : new KeyCodeCombination(key, modifier));
        mi.setOnAction(e -> action.run());
        return mi;
    }

    private Node toolbar() {
        Button orderHistory = new Button("Order History");
        orderHistory.getStyleClass().add("small");
        orderHistory.setOnAction(e -> new OrderHistoryWindow(stage).show());
        Button restore = new Button("Restore Default", Icons.refresh());
        restore.getStyleClass().add("small");
        restore.setOnAction(e -> restoreDefaultLayout());
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox bar = new HBox(6, new Label("Welcome, " + userId), spacer, orderHistory, restore);
        bar.getStyleClass().add("toolbar-strip");
        return bar;
    }

    private Node statusBar() {
        Label connected = new Label("● Connected to KSE feed");
        connected.setStyle("-fx-text-fill: #2E7D32;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox bar = new HBox(16, connected, new Label("User: " + userId), spacer, clock);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.getStyleClass().add("status-bar");
        return bar;
    }

    private void startClock() {
        DateTimeFormatter f = DateTimeFormatter.ofPattern("EEE dd MMM yyyy  HH:mm:ss", Locale.ENGLISH);
        Timeline t = new Timeline(new KeyFrame(Duration.seconds(1), e -> clock.setText(LocalDateTime.now().format(f))));
        t.setCycleCount(Timeline.INDEFINITE);
        t.play();
        clock.setText(LocalDateTime.now().format(f));
    }

    // ------------------------------------------------------------------ layout

    /** Puts every panel back in its default position and size (Tools > Restore Default Layout, Ctrl+R). */
    public void restoreDefaultLayout() {
        maximised = null;
        leftSplit.getItems().setAll(watchesPanel, messagePanel);
        rightSplit.getItems().setAll(depthByOrderPanel, depthByPricePanel);
        mainSplit.getItems().setAll(leftSplit, rightSplit);
        leftSplit.setDividerPositions(LEFT_DIVIDER);
        rightSplit.setDividerPositions(RIGHT_DIVIDER);
        mainSplit.setDividerPositions(MAIN_DIVIDER);
        root.setCenter(mainSplit);
    }

    /** Maximise glyph on a panel: fill the window with it, or restore if it is already maximised. */
    private void toggleMaximise(InternalPanel panel) {
        if (maximised == panel) {
            restoreDefaultLayout();
            return;
        }
        maximised = panel;
        // Detach the panel from its split pane before re-parenting it.
        leftSplit.getItems().remove(panel);
        rightSplit.getItems().remove(panel);
        root.setCenter(panel);
    }

    /** F12: My Watches filling the window, as in the tutorial screenshot. */
    private void showMyWatches() {
        watchesPanel.select(0);
        if (maximised != watchesPanel) toggleMaximise(watchesPanel);
        watches.selectTab("1st Profile");
    }

    private void logout() {
        stage.hide();
        new LoginWindow(new Stage()).show();
    }

    private void info(String title, String text) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, text);
        a.initOwner(stage);
        a.setTitle(title);
        a.setHeaderText(null);
        App.themed(a);
        a.showAndWait();
    }
}

package com.akd.tradecast.ui.azan;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.util.Duration;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/** Screen 7 (part): Message Window docked at the bottom-left of MainWindow. */
public final class MessageWindowPane {
    private MessageWindowPane() { }

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private static final String[] LIVE_FEED = {
            "CEPB at Lower Lock now, Last Trade Price is 14.80",
            "Lower Lock removed from CEPB, Last Trade Price is 14.85",
            "DINT at Upper CAP, Last Trade Price is 31.85",
            "PAKD at Lower Lock now, Last Trade Price is 24.42",
            "Lower Lock removed from PAKD, Last Trade Price is 24.50"
    };

    public static Node create() {
        ObservableList<String> activity = FXCollections.observableArrayList(
                "PKT 09:31:12 - Trade Server: Connection established with online.akdtrade.biz",
                "PKT 09:31:12 - Feed Server: Connection established with online.akdtrade.biz",
                "PKT 09:31:15 - Welcome to AKD Securities Limited Trading Platform",
                "PKT 10:35:09 - Lower Lock removed from PAKD, Last Trade Price is 24.42",
                "PKT 10:40:41 - CEPB at Lower Lock now, Last Trade Price is 14.80",
                "PKT 10:40:46 - Lower Lock removed from CEPB, Last Trade Price is 14.80",
                "PKT 10:41:47 - CEPB at Lower Lock now, Last Trade Price is 14.80",
                "PKT 10:41:54 - Lower Lock removed from CEPB, Last Trade Price is 14.80",
                "PKT 10:42:03 - Lower Lock removed from CEPB, Last Trade Price is 14.80"
        );

        ListView<String> activityList = logList(activity);
        startLiveTicker(activity, activityList);

        TabPane tabs = new TabPane(
                new Tab("Activity", activityList),
                new Tab("Orders", logList(FXCollections.observableArrayList(
                        "PKT 09:38:20 - TICKET 23028554 Sold Order 030621023 100 NCL REG Market filled at 22.90 - Remaining 00 for Account # CKHI000",
                        "PKT 10:02:15 - TICKET 23029110 Bought Order 0301C11 500 HUBC REG Market filled at 39.85 - Remaining 00 for Account # CKHI000",
                        "PKT 11:47:02 - TICKET 23029130 Sold Order 0301C12 100 LUCK REG Market filled at 71.20 - Remaining 00 for Account # CKHI000"
                ))),
                new Tab("Announcements", logList(FXCollections.observableArrayList(
                        "ENGRO (Engro Corporation) - Financial Result: EPS = 5.22 Basic, Cash Dividend 20%",
                        "OGDC (Oil & Gas Dev Co.) - Annual General Meeting will be held on 28/11/2026",
                        "LUCK (Lucky Cement Ltd.) - Book Closure from 14/12/2026 to 20/12/2026"
                ))),
                new Tab("News", logList(FXCollections.observableArrayList(
                        "PKT 09:30:22 - GoP planning to revise well head gas price cap for new exploration blocks",
                        "PKT 09:29:58 - SECP taking steps to boost retail trading activities in deliverable futures",
                        "PKT 09:25:18 - Remittances cross record mark in the current fiscal quarter"
                ))),
                new Tab("AKD", logList(FXCollections.observableArrayList(
                        "Morning Note - KSE-100 expected to test resistance near 12,500; preferred stocks: PSO, PPL, NBP, ENGRO"
                ))),
                new Tab("Board Meetings", logList(FXCollections.observableArrayList(
                        "FABL (Faysal Bank Limited) - Board meeting scheduled to consider quarterly accounts"
                ))),
                new Tab("Alerts", logList(FXCollections.observableArrayList(
                        "NBP (Triggered) - NBP trading at Rs. 41.30/-"
                ))),
                new Tab("Margin Calls", logList(FXCollections.observableArrayList(
                        "No active margin calls for account CKHI000."
                ))),
                new Tab("RSS", logList(FXCollections.observableArrayList(
                        "Connected to PSX Market Feed RSS"
                )))
        );
        tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        return tabs;
    }

    private static ListView<String> logList(ObservableList<String> items) {
        ListView<String> list = new ListView<>(items);
        list.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                    return;
                }
                setText(item);
                if (item.contains("Lower Lock")) {
                    setStyle("-fx-text-fill: #C00000; -fx-background-color: #F7ECEC; -fx-font-size: 10px;");
                } else if (item.contains("Upper CAP")) {
                    setStyle("-fx-text-fill: #006600; -fx-background-color: #ECF7EC; -fx-font-size: 10px;");
                } else if (item.contains("Welcome to AKD")) {
                    setStyle("-fx-text-fill: #FFFFFF; -fx-background-color: #D98200; -fx-font-weight: bold; -fx-font-size: 10px;");
                } else if (item.contains("Connection established")) {
                    setStyle("-fx-text-fill: #FFFFFF; -fx-background-color: #0B4F8A; -fx-font-size: 10px;");
                } else {
                    setStyle("-fx-text-fill: #222222; -fx-font-size: 10px;");
                }
            }
        });
        return list;
    }

    private static void startLiveTicker(ObservableList<String> items, ListView<String> list) {
        final int[] idx = {0};
        Timeline t = new Timeline(new KeyFrame(Duration.seconds(6), e -> {
            String msg = "PKT " + LocalTime.now().format(TIME_FMT) + " - " + LIVE_FEED[idx[0] % LIVE_FEED.length];
            idx[0]++;
            items.add(msg);
            list.scrollTo(items.size() - 1);
        }));
        t.setCycleCount(Timeline.INDEFINITE);
        t.play();
    }
}
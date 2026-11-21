package com.akd.tradecast.ui;

import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.ReadOnlyStringWrapper;

/**
 * Trade Log, Outstanding Log and Activity Log tabs of the My Watches window.
 * They are not among the 7 required screens, so they show a light table only.
 */
final class SimpleLogs {
    private SimpleLogs() { }

    static Node tradeLog() {
        return table(new String[]{"Time", "Order No", "Side", "Scrip", "Volume", "Price", "Status"},
                new String[][]{
                        {"10:02:15", "100231", "BUY", "HUBC", "500", "39.85", "Executed"},
                        {"11:47:02", "100244", "SELL", "LUCK", "100", "71.20", "Executed"},
                });
    }

    static Node outstandingLog() {
        return table(new String[]{"Time", "Order No", "Side", "Scrip", "Volume", "Price", "Remaining"},
                new String[][]{{"12:15:40", "100258", "BUY", "MCB", "50", "181.10", "50"}});
    }

    static Node activityLog() {
        return table(new String[]{"Time", "Activity"},
                new String[][]{
                        {"09:30:01", "Logged in from 192.168.1.20"},
                        {"10:02:15", "Order 100231 executed"},
                        {"11:47:02", "Order 100244 executed"},
                });
    }

    private static Node table(String[] headers, String[][] data) {
        TableView<String[]> t = new TableView<>(FXCollections.observableArrayList(data));
        t.getStyleClass().add("report-grid");
        for (int i = 0; i < headers.length; i++) {
            final int col = i;
            TableColumn<String[], String> c = new TableColumn<>(headers[i]);
            c.setPrefWidth(i == headers.length - 1 && headers.length == 2 ? 400 : 90);
            c.setCellValueFactory(cd -> new ReadOnlyStringWrapper(cd.getValue()[col]));
            t.getColumns().add(c);
        }
        t.setPlaceholder(new Label("No records."));
        return t;
    }
}

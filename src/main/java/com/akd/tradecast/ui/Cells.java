package com.akd.tradecast.ui;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;

import java.util.List;
import java.util.function.Function;

/**
 * Builds table columns whose cells take their text and colour from the whole row object.
 * Calling table.refresh() repaints them after the row data changes (live prices).
 */
public final class Cells {
    private Cells() { }

    /**
     * @param title   header text
     * @param width   preferred width in px
     * @param text    row -> cell text
     * @param styles  row -> extra CSS classes (e.g. "cell-down"), may be null
     * @param right   right-align (numbers) or left-align (text)
     */
    public static <T> TableColumn<T, T> col(String title, double width, Function<T, String> text,
                                            Function<T, List<String>> styles, boolean right) {
        TableColumn<T, T> c = new TableColumn<>(title);
        c.setPrefWidth(width);
        c.setSortable(false);
        c.setCellValueFactory(cd -> new ReadOnlyObjectWrapper<>(cd.getValue()));
        c.setCellFactory(col -> new TableCell<T, T>() {
            @Override
            protected void updateItem(T row, boolean empty) {
                super.updateItem(row, empty);
                getStyleClass().removeAll("cell-up", "cell-down", "cell-buy", "cell-sell",
                        "cell-flash-buy", "cell-flash-sell");
                if (empty || row == null) {
                    setText(null);
                    return;
                }
                setText(text.apply(row));
                setAlignment(right ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
                if (styles != null) getStyleClass().addAll(styles.apply(row));
            }
        });
        return c;
    }

    public static <T> TableColumn<T, T> text(String title, double width, Function<T, String> text) {
        return col(title, width, text, null, false);
    }

    public static <T> TableColumn<T, T> num(String title, double width, Function<T, String> text) {
        return col(title, width, text, null, true);
    }

    /** Green for a rise, red for a fall. */
    public static List<String> upDown(double value) {
        return value > 0 ? List.of("cell-up") : value < 0 ? List.of("cell-down") : List.of();
    }
}

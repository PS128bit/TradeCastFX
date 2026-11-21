package com.akd.tradecast.ui;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * A docked "internal window" like the ones inside TradeCast's main screen
 * (My Watches, Market Depth, Message Window). It has a title strip with one or
 * more window tabs on the left and pin / minimise / maximise / close glyphs on
 * the right. Clicking a tab swaps the content below the strip.
 */
public class InternalPanel extends BorderPane {

    private final HBox tabStrip = new HBox(3);
    private final List<Label> tabs = new ArrayList<>();
    private final List<Node> contents = new ArrayList<>();
    private Consumer<InternalPanel> onMaximise;
    private Consumer<InternalPanel> onClose;

    public InternalPanel() {
        getStyleClass().add("panel-frame");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label pin = control("⇱", "Undock");
        Label min = control("–", "Minimise");
        Label max = control("□", "Maximise / restore");
        Label close = control("✕", "Close");
        max.setOnMouseClicked(e -> { if (onMaximise != null) onMaximise.accept(this); });
        close.setOnMouseClicked(e -> { if (onClose != null) onClose.accept(this); });
        min.setOnMouseClicked(e -> { if (onClose != null) onClose.accept(this); });

        HBox bar = new HBox(3, tabStrip, spacer, pin, min, max, close);
        bar.getStyleClass().add("panel-titlebar");
        bar.setAlignment(Pos.CENTER_LEFT);
        setTop(bar);
    }

    /** Adds a window tab (e.g. "My Watches") whose content shows when the tab is clicked. */
    public InternalPanel addWindow(String title, Node content) {
        Rectangle dot = new Rectangle(7, 7);
        dot.getStyleClass().add("panel-tab-dot");
        Label tab = new Label(title, new StackPane(dot));
        tab.getStyleClass().add("panel-tab");
        int index = tabs.size();
        tab.setOnMouseClicked(e -> select(index));
        tabs.add(tab);
        contents.add(content);
        tabStrip.getChildren().add(tab);
        if (index == 0) select(0);
        return this;
    }

    public void select(int index) {
        for (int i = 0; i < tabs.size(); i++) {
            tabs.get(i).getStyleClass().remove("active");
        }
        tabs.get(index).getStyleClass().add("active");
        setCenter(contents.get(index));
    }

    public void setOnMaximise(Consumer<InternalPanel> handler) { this.onMaximise = handler; }

    public void setOnClose(Consumer<InternalPanel> handler) { this.onClose = handler; }

    private static Label control(String glyph, String tip) {
        Label l = new Label(glyph);
        l.getStyleClass().add("panel-control");
        l.setTooltip(new javafx.scene.control.Tooltip(tip));
        return l;
    }
}

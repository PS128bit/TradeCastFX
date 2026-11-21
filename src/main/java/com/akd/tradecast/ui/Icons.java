package com.akd.tradecast.ui;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/** Small 16px toolbar icons drawn with shapes, so the project needs no image files. */
public final class Icons {
    private Icons() { }

    public static Node save() {
        Rectangle body = rect(1, 1, 14, 14, "#3A5FA8", "#1E3A70");
        Rectangle label = rect(4, 1, 8, 5, "#FFFFFF", "#1E3A70");
        Rectangle shutter = rect(4, 9, 8, 6, "#C9D6EE", "#1E3A70");
        return new Group(body, label, shutter);
    }

    public static Node pdf() {
        Rectangle page = rect(2, 1, 12, 14, "#FFFFFF", "#8A8A8A");
        Rectangle band = rect(2, 5, 12, 6, "#D42A1E", "#D42A1E");
        Text t = new Text(3, 10, "PDF");
        t.setFont(Font.font("Arial", FontWeight.BOLD, 5));
        t.setFill(Color.WHITE);
        return new Group(page, band, t);
    }

    public static Node print() {
        Rectangle paper = rect(4, 1, 8, 6, "#FFFFFF", "#6B6B6B");
        Rectangle body = rect(1, 6, 14, 6, "#9FB7A0", "#4D6B4E");
        Rectangle tray = rect(4, 11, 8, 4, "#FFFFFF", "#6B6B6B");
        return new Group(paper, body, tray);
    }

    public static Node grid() {
        Rectangle frame = rect(1, 2, 14, 12, "#FFFFFF", "#4D6185");
        Rectangle head = rect(1, 2, 14, 3, "#9DB9EB", "#4D6185");
        Line v = new Line(6, 5, 6, 14);
        Line h = new Line(1, 9, 15, 9);
        v.setStroke(Color.web("#4D6185"));
        h.setStroke(Color.web("#4D6185"));
        return new Group(frame, head, v, h);
    }

    public static Node search() {
        Circle lens = new Circle(6, 6, 4);
        lens.setFill(Color.web("#DDEEFF"));
        lens.setStroke(Color.web("#3A5FA8"));
        lens.setStrokeWidth(1.5);
        Line handle = new Line(9, 9, 13, 13);
        handle.setStroke(Color.web("#3A5FA8"));
        handle.setStrokeWidth(2);
        return new Group(lens, handle);
    }

    public static Node refresh() {
        Circle c = new Circle(8, 8, 5);
        c.setFill(Color.TRANSPARENT);
        c.setStroke(Color.web("#2E7D32"));
        c.setStrokeWidth(2);
        Rectangle gap = rect(9, 1, 5, 5, "#ECE9D8", "#ECE9D8");
        return new Group(c, gap);
    }

    public static Node star() {
        Text t = new Text(1, 13, "★");
        t.setFont(Font.font(13));
        t.setFill(Color.web("#E8A400"));
        return new Group(t);
    }

    public static Node browse() {
        Rectangle back = rect(1, 3, 10, 10, "#F3E3A2", "#9B7E2A");
        Rectangle front = rect(5, 5, 10, 10, "#FFFFFF", "#6B6B6B");
        return new Group(back, front);
    }

    /** Flat icon-only button with a tooltip. */
    public static Button button(Node icon, String tooltip) {
        Button b = new Button();
        b.setGraphic(icon);
        b.getStyleClass().add("icon");
        b.setTooltip(new Tooltip(tooltip));
        b.setFocusTraversable(false);
        return b;
    }

    private static Rectangle rect(double x, double y, double w, double h, String fill, String stroke) {
        Rectangle r = new Rectangle(x, y, w, h);
        r.setFill(Color.web(fill));
        r.setStroke(Color.web(stroke));
        r.setStrokeWidth(0.8);
        return r;
    }
}

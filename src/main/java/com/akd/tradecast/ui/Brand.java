package com.akd.tradecast.ui;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Logos drawn with JavaFX shapes (no image files needed):
 * the "TRADE CAST" mark and the "akd trade.com / ONLINE INVESTMENTS" mark.
 */
public final class Brand {
    private Brand() { }

    private static final Color AKD_ORANGE = Color.web("#F26B1D");
    private static final Color AKD_NAVY = Color.web("#1F2F5C");

    /** Small "TRADE CAST" logo with a red arrow-bird above the words. */
    public static Node tradeCastLogo(double scale) {
        Polygon wing1 = new Polygon(10, 0, 22, 14, 14, 16);
        wing1.setFill(Color.web("#D9261C"));
        Polygon wing2 = new Polygon(4, 8, 16, 18, 8, 22);
        wing2.setFill(Color.web("#F05A28"));
        Polygon tail = new Polygon(14, 16, 24, 30, 18, 22);
        tail.setFill(Color.web("#9E1B13"));
        Group bird = new Group(wing1, wing2, tail);

        Text trade = new Text("TRADE ");
        trade.setFont(Font.font("Arial", FontWeight.BOLD, 9 * scale));
        trade.setFill(Color.web("#C0281C"));
        Text cast = new Text("CAST");
        cast.setFont(Font.font("Arial", FontWeight.BOLD, 9 * scale));
        cast.setFill(Color.web("#9A9A9A"));
        HBox words = new HBox(trade, cast);

        // Scale the bird inside an outer Group so the scaled size is used for layout.
        bird.setScaleX(scale);
        bird.setScaleY(scale);
        VBox box = new VBox(2, new Group(bird), words);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    /** Orange speech-bubble mark used on its own (window corners, dialog header). */
    public static Node akdBubble(double w, double h) {
        Rectangle bubble = new Rectangle(w, h);
        bubble.setArcWidth(h * 0.6);
        bubble.setArcHeight(h * 0.6);
        bubble.setRotate(-12);
        bubble.setFill(new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#FF9A4D")), new Stop(1, AKD_ORANGE)));
        SVGPath curl = new SVGPath();
        curl.setContent("M 0 0 C 6 -4 10 2 6 6 C 3 9 6 12 10 10");
        curl.setStroke(AKD_NAVY);
        curl.setFill(Color.TRANSPARENT);
        curl.setStrokeWidth(1.4);
        curl.setTranslateX(w * 0.48);
        curl.setTranslateY(h * 0.15);
        return new StackPane(new Group(bubble), curl);
    }

    /** Large "akd trade.com / ONLINE INVESTMENTS" logo. */
    public static Node akdTradeLogo(double scale) {
        Node bubble = akdBubble(58 * scale, 34 * scale);
        HBox bubbleRow = new HBox(bubble);
        bubbleRow.setAlignment(Pos.CENTER_RIGHT);
        bubbleRow.setTranslateX(-40 * scale);

        Text akd = new Text("akd trade");
        akd.setFont(Font.font("Arial", FontWeight.BOLD, 30 * scale));
        akd.setFill(AKD_NAVY);
        Text com = new Text(".com");
        com.setFont(Font.font("Arial", FontWeight.BOLD, 30 * scale));
        com.setFill(AKD_NAVY);
        HBox name = new HBox(akd, com);
        name.setAlignment(Pos.CENTER_RIGHT);

        Label tag = new Label("O N L I N E    I N V E S T M E N T S");
        tag.setFont(Font.font("Arial", 8 * scale));
        tag.setTextFill(Color.web("#555555"));

        VBox box = new VBox(-4 * scale, bubbleRow, name, tag);
        box.setAlignment(Pos.CENTER_RIGHT);
        return box;
    }

    /** "BROKERAGE prelude" mark shown bottom-left on the login windows. */
    public static Node preludeLogo() {
        Rectangle stripe = new Rectangle(22, 12);
        stripe.setFill(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#2D5C9A")), new Stop(1, Color.web("#8FB3E3"))));
        Text brokerage = new Text("BROKERAGE");
        brokerage.setFont(Font.font("Arial", FontWeight.BOLD, 6));
        brokerage.setFill(Color.web("#2D5C9A"));
        Text prelude = new Text("prelude");
        prelude.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        prelude.setFill(Color.web("#2D5C9A"));
        VBox words = new VBox(-3, brokerage, prelude);
        HBox box = new HBox(3, stripe, words);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }
}

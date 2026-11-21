package com.akd.tradecast.ui;

import com.akd.tradecast.App;
import com.akd.tradecast.model.Quote;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/** Small order-entry popup opened by a row's Trade button (no backend: it only confirms the order). */
public class TradeDialog {

    private final Stage dialog = new Stage();

    public TradeDialog(Window owner, Quote quote) {
        dialog.initOwner(owner);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setTitle("Trade - " + quote.getScrip());
        dialog.setResizable(false);

        ToggleGroup side = new ToggleGroup();
        RadioButton buy = new RadioButton("Buy");
        RadioButton sell = new RadioButton("Sell");
        buy.setToggleGroup(side);
        sell.setToggleGroup(side);
        buy.setSelected(true);

        TextField volume = new TextField("500");
        TextField price = new TextField(Fmt.money(quote.getLastPrice()));
        Label error = new Label();
        error.getStyleClass().add("error-label");

        GridPane g = new GridPane();
        g.setHgap(8);
        g.setVgap(8);
        g.setPadding(new Insets(12));
        Label name = new Label(quote.getScrip() + "  -  " + quote.getCompanyName());
        name.setStyle("-fx-font-weight: bold;");
        g.add(name, 0, 0, 2, 1);
        g.add(new Label("Side"), 0, 1);
        g.add(new HBox(12, buy, sell), 1, 1);
        g.add(new Label("Volume"), 0, 2);
        g.add(volume, 1, 2);
        g.add(new Label("Price Rs."), 0, 3);
        g.add(price, 1, 3);
        g.add(error, 0, 4, 2, 1);

        Button place = new Button("Place Order");
        place.setDefaultButton(true);
        place.setOnAction(e -> {
            long vol = (long) Fmt.parse(volume.getText());
            double px = Fmt.parse(price.getText());
            if (vol <= 0 || px <= 0) {
                error.setText("Enter a volume and price greater than zero.");
                return;
            }
            Alert a = new Alert(Alert.AlertType.INFORMATION,
                    (buy.isSelected() ? "BUY " : "SELL ") + Fmt.qtyAlways(vol) + " " + quote.getScrip()
                            + " @ Rs. " + Fmt.money(px) + " sent to the exchange.");
            a.initOwner(dialog);
            a.setTitle("Order Placed");
            a.setHeaderText(null);
            App.themed(a);
            a.showAndWait();
            dialog.close();
        });
        Button cancel = new Button("Cancel");
        cancel.setCancelButton(true);
        cancel.setOnAction(e -> dialog.close());
        HBox buttons = new HBox(8, place, cancel);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        g.add(buttons, 0, 5, 2, 1);

        dialog.setScene(App.themed(new Scene(g)));
    }

    public void showAndWait() {
        dialog.showAndWait();
    }
}

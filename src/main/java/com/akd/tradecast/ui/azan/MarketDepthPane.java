package com.akd.tradecast.ui.azan;

import javafx.scene.Node;

/**
 * SCREEN 7 (part) - Market Depth By Order / By Price panels.  OWNER: Aazan.
 * MainWindow docks two of these on the right-hand side.
 *
 * TODO (reference page 85):
 *  - Top bar: Market combo (REG), Scrip field (ANL), L.Trade read-only price, Details button
 *  - Compact TableView: Flag | Volume | Price  ||  Price | Volume | Flag
 *    (bids on the left in blue tones, offers on the right in pink/red tones)
 *  - BY_ORDER lists individual orders; BY_PRICE groups them per price level
 *  - Footer label with the company name ("Azgard Nine Limited - Personal G...")
 */
public final class MarketDepthPane {
    private MarketDepthPane() { }

    public enum Kind { BY_ORDER, BY_PRICE }

    public static Node create(Kind kind) {
        return Placeholder.of(kind == Kind.BY_ORDER ? "Market Depth By Order" : "Market Depth By Price", "Aazan");
    }
}

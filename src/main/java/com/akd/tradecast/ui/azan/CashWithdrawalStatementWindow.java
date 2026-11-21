package com.akd.tradecast.ui.azan;

import com.akd.tradecast.App;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * SCREEN 5 - Cash Withdrawal Statement (Reports menu, Alt+W).  OWNER: Aazan.
 *
 * TODO (from the assignment sheet and reference page 83):
 *  - Row 1: Account combo (CKHI000), Name combo (ABC), PIN field (masked), save/PDF/print/grid icons (see ui.Icons)
 *  - Row 2: Start Date / End Date DatePickers and a "Get" button, red "Forgot PIN?" link
 *  - Table: Requested Date, Amount Requested, Amount Approved, Withdrawal Mode, Comments, Status
 *    ("Get" filters rows by the date range; Status = In-Process / Approved / Declined)
 *  - Status bar at the bottom
 *  Reuse: ui.Fmt for dates and money, the "report-grid" CSS class on the TableView,
 *  OrderHistoryWindow as a model for the layout.
 */
public final class CashWithdrawalStatementWindow {
    private CashWithdrawalStatementWindow() { }

    public static void show(Window owner) {
        Stage s = new Stage();
        s.initOwner(owner);
        s.setTitle("Cash Withdrawal Statement");
        s.setScene(App.themed(new Scene(new StackPane(Placeholder.of("Screen 5: Cash Withdrawal Statement", "Aazan")), 760, 480)));
        s.show();
    }
}

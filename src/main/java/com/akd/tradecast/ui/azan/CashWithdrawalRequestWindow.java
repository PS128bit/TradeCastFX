package com.akd.tradecast.ui.azan;

import com.akd.tradecast.App;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * SCREEN 6 - Cash Withdrawal Request (Services menu, Alt+R).  OWNER: Aazan.
 *
 * TODO (from the assignment sheet and reference page 84):
 *  - Header "Cash Withdrawal Request"; Account No combo + PIN (PasswordField); red "Forgot PIN?"
 *  - Available Amount (read-only, e.g. 316.00)  -  Withdrawal Amount  =  Remaining Amount (read-only)
 *    Remaining must update LIVE while the user types (listener on withdrawalField.textProperty()).
 *  - Three mutually exclusive RadioButtons in one ToggleGroup: Cheque Delivery, Cheque Pickup
 *    (with branch combo, enabled only when selected), Online Fund Transfer (Account No field,
 *    enabled only when selected), each with its description text
 *  - Comments TextArea and a Submit button (validate amount > 0 and <= available, PIN entered)
 */
public final class CashWithdrawalRequestWindow {
    private CashWithdrawalRequestWindow() { }

    public static void show(Window owner) {
        Stage s = new Stage();
        s.initOwner(owner);
        s.setTitle("Cash Withdrawal Request");
        s.setScene(App.themed(new Scene(new StackPane(Placeholder.of("Screen 6: Cash Withdrawal Request", "Aazan")), 520, 640)));
        s.show();
    }
}

package com.akd.tradecast.ui.azan;

import javafx.scene.Node;

/**
 * SCREEN 7 (part) - Message Window docked at the bottom-left of MainWindow.  OWNER: Aazan.
 *
 * TODO (reference page 85):
 *  - TabPane: Activity, Orders, Announcements, News (optionally AKD, Board Meetings, Alerts, Margin Calls, RSS)
 *  - Each tab: a scrollable ListView of timestamped messages,
 *    e.g. "PKT 10:41:47 - CEPB at Lower Lock now, Last Trade Price is 14.80" (red text)
 *  - Optional: a Timeline that appends a new message every few seconds
 *
 * Restore Default Layout (Ctrl+R) is already done in MainWindow.restoreDefaultLayout().
 */
public final class MessageWindowPane {
    private MessageWindowPane() { }

    public static Node create() {
        return Placeholder.of("Message Window", "Aazan");
    }
}

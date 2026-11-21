# TradeCastFX: AKD TradeCast JavaFX Frontend Replica

Software Design and Analysis, Assignment 2 (group).
A JavaFX replica of the 7 AKD TradeCast screens from the reference PDF. There is no backend: all data is sample data in `data/MockData.java`.

| Member | Roll No | Screens |
| --- | --- | --- |
| Muhammad Anas | 23i-0853 | 1 Login + Forgot Password, 2 My Watches, 3 Portfolio Watch, 4 Order History, app shell |
| Aazan Muhammad Mustafa | 25i-0789 | 5 Cash Withdrawal Statement, 6 Cash Withdrawal Request, 7 Market Depth + Message Window |

## Run it

Needs JDK 17 or newer. Maven downloads JavaFX 21 the first time.

**Eclipse:** File → Import → Maven → Existing Maven Projects → pick this folder. Then right-click the project → Run As → Maven build… → Goals: `javafx:run` → Run.

**Terminal:** `mvn javafx:run`

**IntelliJ:** Open the folder, then in the Maven panel run Plugins → javafx → `javafx:run`.

### Demo login

| Field | Value |
| --- | --- |
| User ID | `CKHI000` |
| Password | `qaZs12qGt5` (10 characters) |

The login screen asks for only 4 random characters of the password, for example "Please enter 3rd, 5th, 7th & 8th character". Type those characters from the demo password into the white boxes. A wrong attempt shows "Invalid login credentials!" and picks 4 new positions.

## Where each screen is

| # | Screen | How to open it | Class |
| --- | --- | --- | --- |
| 1 | Login, Forgot Password popup | App start; "Forgot Password?" button | `ui/LoginWindow`, `ui/ForgotPasswordDialog` |
| 2 | My Watches | After login; Market Monitor → My Watches (F12) | `ui/MyWatchesView`, `ui/WatchProfileView`, `ui/TradeDialog` |
| 3 | Portfolio Watch | "Portfolio Watch" tab in My Watches | `ui/PortfolioWatchView` |
| 4 | Order History | Reports → Order History (Alt+O) | `ui/OrderHistoryWindow` |
| 5 | Cash Withdrawal Statement | Reports → Cash Withdrawal Statement (Alt+W) | `ui/azan/CashWithdrawalStatementWindow` |
| 6 | Cash Withdrawal Request | Services → Cash Withdrawal Request (Alt+R) | `ui/azan/CashWithdrawalRequestWindow` |
| 7 | Market Depth + Message Window, Restore Default Layout | Main window layout; Tools → Restore Default Layout (Ctrl+R) | `ui/azan/MarketDepthPane`, `ui/azan/MessageWindowPane`, `ui/MainWindow` |

## Features

- **Login:** 10 single-character password boxes. Only the 4 challenged boxes are enabled, and focus moves to the next box automatically. The instruction label is generated from the random positions.
- **My Watches:** 1st/2nd/3rd Profile, Sector, Index, Spot, Future and Cap Watch tabs. Type a scrip in the Scrip box and press Enter to add it, or right-click a row to remove it. A simulated market feed updates prices every 1.5 s, and changed bid/offer volumes flash green or yellow. Each row has a Trade button that opens an order dialog.
- **Portfolio Watch:**
  - Selecting a holding updates Scrip Worth and its pie chart.
  - Security Holdings and Portfolio Worth (Portfolio Worth, Ledger Balance, Profit/Loss, Sold Collaterals, Net Worth) are read-only values calculated from the holdings grid.
  - Liquidate buttons ask for confirmation first.
  - Session Holdings shows only the scrips traded today.
- **Order History:** filters by Account No, Name, Type, Scrip, Start Date and End Date, applied when you click "Get". The status bar shows the record count and today's date.
- **Main window:**
  - Docked panels with tab strips, as in the original.
  - The □ glyph maximises a panel.
  - Restore Default Layout (Ctrl+R or the toolbar button) puts every panel back in its default size and position.

## Project layout

```
src/main/java/com/akd/tradecast/
  App.java                 entry point and shared stylesheet
  model/                   Quote, Holding, OrderRecord
  data/MockData.java       all sample data and the demo login
  ui/                      screens 1-4, main window, shared helpers (Cells, Fmt, Icons, Brand, InternalPanel)
  ui/azan/                 screens 5-7 (Aazan)
src/main/resources/com/akd/tradecast/tradecast.css   legacy Windows XP look
```

Logos and icons are drawn with JavaFX shapes, so the project has no image files.

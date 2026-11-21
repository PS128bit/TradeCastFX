package com.akd.tradecast.data;

import com.akd.tradecast.model.Holding;
import com.akd.tradecast.model.OrderRecord;
import com.akd.tradecast.model.Quote;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Hard-coded sample data. The assignment needs no backend, so every screen reads from here.
 */
public final class MockData {

    private MockData() { }

    public static final String DEMO_USER_ID = "CKHI000";
    /** Demo password for the 10-box login challenge (10 characters). */
    public static final String DEMO_PASSWORD = "qaZs12qGt5";
    public static final String DEMO_EMAIL = "client@akdtrade.com";

    /** Account number -> account holder name. */
    public static final Map<String, String> ACCOUNTS = new LinkedHashMap<>();
    static {
        ACCOUNTS.put("CKHI000", "ABC");
        ACCOUNTS.put("CKHI001", "XYZ Traders");
    }

    /** All scrips the Scrip search box can add, keyed by symbol. */
    public static Map<String, Quote> universe() {
        Map<String, Quote> m = new LinkedHashMap<>();
        for (Quote q : allQuotes()) m.put(q.getScrip(), q);
        return m;
    }

    private static List<Quote> allQuotes() {
        List<Quote> l = new ArrayList<>();
        l.add(q("NBP", "National Bank of Pakistan. - Commercial Banks", 41.30, -2.17, 0, 0, 41.30, 770918, 41.31, 41.30, 1244519, 41.30, 43.47));
        l.add(q("ENGRO", "Engro Corporation Limited. - Chemicals", 112.50, -5.22, 112.45, 1, 112.50, 6611, 114.41, 111.86, 662694, 112.60, 117.72));
        l.add(q("OGDC", "Oil & Gas Development Co. - Oil & Gas Exploration", 125.25, -3.72, 125.01, 232, 125.25, 110, 126.00, 124.15, 124480, 125.05, 128.97));
        l.add(q("LOTPTA", "Lotte Pakistan PTA Limited. - Chemicals", 10.70, -0.43, 10.69, 390, 10.70, 98775, 10.85, 10.57, 1233750, 10.73, 11.13));
        l.add(q("DGKC", "D.G. Khan Cement Co. - Cement", 20.00, -0.63, 20.05, 5000, 20.10, 2500, 20.39, 20.00, 201761, 20.07, 20.63));
        l.add(q("LUCK", "Lucky Cement Limited. - Cement", 71.00, -0.93, 71.01, 2000, 71.10, 4172, 71.25, 70.60, 68389, 71.01, 71.93));
        l.add(q("NCL", "Nishat (Chunian) Limited. - Textile Composite", 16.55, -0.72, 16.50, 7595, 16.55, 62, 16.99, 16.45, 41958, 16.53, 17.27));
        l.add(q("NML", "Nishat Mills Limited. - Textile Composite", 40.35, -1.66, 40.30, 270, 40.35, 1035, 41.05, 40.05, 136098, 40.31, 42.01));
        l.add(q("EFOODS", "Engro Foods Limited. - Food & Personal Care", 22.90, -0.20, 22.90, 107, 22.95, 2000, 23.00, 22.25, 11105, 22.87, 23.10));
        l.add(q("AKBL", "Askari Bank Limited. - Commercial Banks", 9.25, -0.32, 9.25, 145, 9.40, 1000, 9.52, 9.25, 19100, 9.32, 9.57));
        l.add(q("BAFL", "Bank Alfalah Limited. - Commercial Banks", 9.80, -0.16, 9.80, 613, 9.84, 50, 9.81, 9.70, 168537, 9.73, 9.96));
        l.add(q("GGL", "Ghani Glass Limited. - Glass & Ceramics", 10.70, -0.25, 10.70, 172, 10.95, 5000, 10.70, 10.70, 10, 10.70, 10.95));
        l.add(q("ANL", "Azgard Nine Limited. - Personal Goods", 4.50, -0.19, 4.48, 200, 4.50, 54, 4.59, 4.33, 601627, 4.48, 4.69));
        l.add(q("ATRL", "Attock Refinery Limited. - Refinery", 113.65, -3.71, 113.65, 2273, 113.74, 2000, 114.55, 111.50, 361347, 113.21, 117.36));
        l.add(q("PRL", "Pakistan Refinery Limited. - Refinery", 58.48, -3.07, 0, 0, 58.48, 9929, 59.88, 58.48, 15577, 58.64, 61.55));
        l.add(q("NRL", "National Refinery Limited. - Refinery", 354.49, -7.20, 354.50, 32, 354.68, 200, 355.49, 348.00, 77493, 352.37, 361.69));
        // Extra scrips used by the other profiles and the Scrip search box.
        l.add(q("HUBC", "Hub Power Company. - Power Generation", 40.30, 0.32, 40.30, 5865, 40.33, 1000, 40.40, 40.06, 3252180, 40.25, 39.98));
        l.add(q("MCB", "MCB Bank Limited. - Commercial Banks", 182.40, 1.15, 182.30, 1200, 182.45, 800, 183.00, 180.90, 412300, 181.95, 181.25));
        l.add(q("PSO", "Pakistan State Oil. - Oil & Gas Marketing", 254.10, 2.35, 254.00, 600, 254.20, 450, 255.00, 251.10, 288400, 253.40, 251.75));
        l.add(q("FFC", "Fauji Fertilizer Company. - Fertilizer", 118.20, 0.85, 118.15, 3100, 118.25, 2400, 118.90, 117.10, 902100, 118.05, 117.35));
        l.add(q("HBL", "Habib Bank Limited. - Commercial Banks", 117.50, 0.40, 117.50, 1795, 117.59, 100, 117.70, 116.50, 28250, 117.21, 117.10));
        l.add(q("PPL", "Pakistan Petroleum Limited. - Oil & Gas Exploration", 196.70, -1.10, 196.60, 900, 196.80, 700, 198.40, 195.90, 377800, 197.02, 197.80));
        l.add(q("POL", "Pakistan Oilfields Limited. - Oil & Gas Exploration", 352.30, 3.05, 352.20, 150, 352.40, 120, 353.00, 348.50, 91500, 351.10, 349.25));
        l.add(q("FCCL", "Fauji Cement Company. - Cement", 7.85, 0.05, 7.84, 15000, 7.86, 12000, 7.90, 7.78, 2104500, 7.84, 7.80));
        return l;
    }

    private static Quote q(String scrip, String name, double last, double chg, double buy, long bvol, double sell,
                           long svol, double high, double low, long tv, double avg, double close) {
        return new Quote("REG", scrip, name, last, chg, buy, bvol, sell, svol, high, low, tv, avg, close);
    }

    /** Scrips in each watch tab of My Watches. */
    public static List<String> watchScrips(String tab) {
        switch (tab) {
            case "1st Profile":
                return List.of("NBP", "ENGRO", "OGDC", "LOTPTA", "DGKC", "LUCK", "NCL", "NML", "EFOODS",
                        "AKBL", "BAFL", "GGL", "ANL", "ATRL", "PRL", "NRL");
            case "2nd Profile":
                return List.of("HUBC", "MCB", "PSO", "FFC", "HBL", "PPL", "POL", "FCCL");
            case "3rd Profile":
                return List.of("ENGRO", "EFOODS", "FFC", "LOTPTA");
            case "Sector Watch":
                return List.of("NBP", "MCB", "HBL", "AKBL", "BAFL");
            case "Index Watch":
                return List.of("OGDC", "PPL", "POL", "HUBC", "ENGRO", "LUCK", "MCB", "FFC");
            case "Cap Watch":
                return List.of("OGDC", "PPL", "MCB", "ENGRO", "HUBC");
            default:
                return List.of("DGKC", "LUCK", "FCCL");
        }
    }

    /** Securities held in the demo account (Portfolio Watch). */
    public static List<Holding> holdings() {
        List<Holding> l = new ArrayList<>();
        l.add(h("BAHL", "Bank AL Habib Limited. - Commercial Banks", 915, 26.24, 29.29, 0.20, 29.26, 2500, 29.29, 99, "15:29:45", 191380, 29.30, 28.92, 29.28));
        l.add(h("EFUL", "EFU Life Assurance Ltd. - Life Insurance", 566, 78.00, 68.39, 3.16, 67.10, 34, 68.37, 100, "13:03:17", 356, 68.39, 67.10, 65.23));
        l.add(h("ENGRO", "Engro Corporation Limited. - Chemicals", 220, 145.75, 135.21, 2.17, 135.22, 500, 135.30, 8, "15:29:58", 2722861, 137.30, 133.30, 135.35));
        l.add(h("GAIL", "Gharibwal Cement Limited. - Cement", 3500, 4.40, 3.21, 0.00, 3.11, 726, 3.45, 1500, "--", 0, 3.21, 3.21, 3.21));
        l.add(h("HBL", "Habib Bank Limited. - Commercial Banks", 319, 89.39, 117.50, 0.40, 117.50, 1795, 117.59, 100, "15:27:51", 28250, 117.70, 116.50, 117.10));
        l.add(h("HMB", "Habib Metropolitan Bank. - Commercial Banks", 870, 19.67, 17.85, 0.07, 17.86, 5000, 17.97, 8500, "14:48:09", 10818, 18.00, 17.84, 17.78));
        l.add(h("HUBC", "Hub Power Company. - Power Generation", 1000, 32.03, 40.30, 0.32, 40.30, 5865, 40.33, 1000, "15:29:57", 3252180, 40.40, 40.06, 39.98));
        l.add(h("JSCL", "Jahangir Siddiqui & Co. - Investment Companies", 1031, 12.50, 5.70, 0.08, 5.71, 2000, 5.72, 3950, "15:29:34", 1104465, 5.89, 5.69, 5.70));
        l.add(h("KESC", "K-Electric Limited. - Power Generation", 3000, 2.27, 1.89, -0.01, 1.89, 2848, 1.92, 2, "15:29:25", 148511, 1.94, 1.83, 1.91));
        l.add(h("LOTPTA", "Lotte Pakistan PTA Limited. - Chemicals", 1000, 8.05, 12.79, 0.01, 12.78, 78314, 12.79, 10000, "15:29:59", 3502276, 13.01, 12.76, 12.80));
        l.add(h("NIB", "NIB Bank Limited. - Commercial Banks", 4000, 3.06, 1.40, 0.00, 1.39, 1, 1.40, 9593, "15:14:27", 420032, 1.44, 1.35, 1.36));
        l.add(h("NJLIC", "New Jubilee Life Insurance. - Life Insurance", 3500, 39.00, 54.95, 1.45, 54.95, 16, 55.00, 1000, "15:10:57", 9068, 55.45, 53.50, 54.95));
        l.add(h("PAKRI", "Pakistan Reinsurance Co. - Insurance", 1000, 17.09, 13.51, -0.10, 13.51, 2600, 13.63, 500, "15:29:41", 155048, 13.80, 13.50, 13.53));
        return l;
    }

    private static Holding h(String scrip, String name, long qty, double avg, double cur, double chg, double buy,
                             long bvol, double sell, long svol, String time, long tv, double high, double low, double close) {
        return new Holding("REG", scrip, name, qty, avg, cur, chg, buy, bvol, sell, svol, time, tv, high, low, close, "OPN");
    }

    public static final double LEDGER_BALANCE = 27.00;
    public static final double SOLD_COLLATERALS = 0.00;

    /** Executed trades for Order History. */
    public static List<OrderRecord> orderHistory() {
        List<OrderRecord> l = new ArrayList<>();
        l.add(o("CKHI000", "BUY", "OGDC", 2, 128.24, 256.72, 2026, 8, 12, 2026, 8, 15));
        l.add(o("CKHI000", "BUY", "OGDC", 2, 130.00, 259.73, 2026, 8, 15, 2026, 8, 18));
        l.add(o("CKHI000", "BUY", "ANL", 50, 4.79, 241.49, 2026, 8, 19, 2026, 8, 22));
        l.add(o("CKHI000", "SELL", "ANL", 25, 4.80, 118.99, 2026, 8, 27, 2026, 8, 30));
        l.add(o("CKHI000", "SELL", "ANL", 25, 5.07, 125.16, 2026, 9, 5, 2026, 9, 8));
        l.add(o("CKHI000", "BUY", "ENGRO", 2, 134.90, 270.04, 2026, 9, 9, 2026, 9, 12));
        l.add(o("CKHI000", "BUY", "HUBC", 500, 39.85, 19947.10, 2026, 10, 6, 2026, 10, 8));
        l.add(o("CKHI000", "SELL", "LUCK", 100, 71.20, 7105.80, 2026, 10, 21, 2026, 10, 23));
        l.add(o("CKHI000", "BUY", "MCB", 50, 181.10, 9069.53, 2026, 11, 3, 2026, 11, 5));
        l.add(o("CKHI001", "BUY", "PSO", 40, 250.75, 10044.13, 2026, 9, 16, 2026, 9, 18));
        l.add(o("CKHI001", "SELL", "FFC", 300, 117.90, 35303.66, 2026, 10, 14, 2026, 10, 16));
        return l;
    }

    private static OrderRecord o(String acc, String type, String scrip, long qty, double rate, double net,
                                 int ty, int tm, int td, int cy, int cm, int cd) {
        return new OrderRecord(acc, type, scrip, qty, rate, net, LocalDate.of(ty, tm, td), LocalDate.of(cy, cm, cd));
    }
}

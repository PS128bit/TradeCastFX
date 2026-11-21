package com.akd.tradecast.ui;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/** Number and date formats used across all screens. */
public final class Fmt {
    private Fmt() { }

    private static final DecimalFormat MONEY = new DecimalFormat("#,##0.00");
    private static final DecimalFormat QTY = new DecimalFormat("#,##0");
    public static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH);
    public static final DateTimeFormatter LONG_DATE = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ENGLISH);

    public static String money(double v) { return MONEY.format(v); }

    /** Price cell: blank when there is no bid/offer (0). */
    public static String price(double v) { return v == 0 ? "" : MONEY.format(v); }

    public static String qty(long v) { return v == 0 ? "" : QTY.format(v); }

    public static String qtyAlways(long v) { return QTY.format(v); }

    public static String date(LocalDate d) { return d == null ? "" : DATE.format(d); }

    /** Parses "1,234.50" style text; returns 0 for blank or invalid input. */
    public static double parse(String s) {
        if (s == null) return 0;
        String t = s.replace(",", "").trim();
        if (t.isEmpty()) return 0;
        try {
            return Double.parseDouble(t);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}

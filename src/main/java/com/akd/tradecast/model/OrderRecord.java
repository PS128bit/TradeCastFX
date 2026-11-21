package com.akd.tradecast.model;

import java.time.LocalDate;

/** One executed trade shown in the Order History window. */
public class OrderRecord {
    private final String accountNo;
    private final String type;          // BUY or SELL
    private final String scrip;
    private final long quantity;
    private final double grossRate;
    private final double netAmount;
    private final LocalDate tradeDate;
    private final LocalDate clearingDate;

    public OrderRecord(String accountNo, String type, String scrip, long quantity, double grossRate,
                       double netAmount, LocalDate tradeDate, LocalDate clearingDate) {
        this.accountNo = accountNo;
        this.type = type;
        this.scrip = scrip;
        this.quantity = quantity;
        this.grossRate = grossRate;
        this.netAmount = netAmount;
        this.tradeDate = tradeDate;
        this.clearingDate = clearingDate;
    }

    public String getAccountNo() { return accountNo; }
    public String getType() { return type; }
    public String getScrip() { return scrip; }
    public long getQuantity() { return quantity; }
    public double getGrossRate() { return grossRate; }
    public double getNetAmount() { return netAmount; }
    public LocalDate getTradeDate() { return tradeDate; }
    public LocalDate getClearingDate() { return clearingDate; }
}

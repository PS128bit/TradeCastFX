package com.akd.tradecast.model;

/** One row of the My Watches market data grid. */
public class Quote {
    private final String market;
    private final String scrip;
    private final String companyName;
    private double lastPrice;
    private double change;
    private double buy;
    private long buyVolume;
    private double sell;
    private long sellVolume;
    private double high;
    private double low;
    private long totalVolume;
    private double average;
    private final double closePrice;
    /** True for one tick after the bid/offer volume changed, so the grid can flash that cell. */
    private boolean buyFlash;
    private boolean sellFlash;

    public boolean isBuyFlash() { return buyFlash; }
    public boolean isSellFlash() { return sellFlash; }
    public void setFlash(boolean buyFlash, boolean sellFlash) {
        this.buyFlash = buyFlash;
        this.sellFlash = sellFlash;
    }

    public void setVolumes(long buyVolume, long sellVolume) {
        this.buyVolume = buyVolume;
        this.sellVolume = sellVolume;
    }

    public Quote(String market, String scrip, String companyName, double lastPrice, double change,
                 double buy, long buyVolume, double sell, long sellVolume, double high, double low,
                 long totalVolume, double average, double closePrice) {
        this.market = market;
        this.scrip = scrip;
        this.companyName = companyName;
        this.lastPrice = lastPrice;
        this.change = change;
        this.buy = buy;
        this.buyVolume = buyVolume;
        this.sell = sell;
        this.sellVolume = sellVolume;
        this.high = high;
        this.low = low;
        this.totalVolume = totalVolume;
        this.average = average;
        this.closePrice = closePrice;
    }

    /** Simulates a market tick: moves the price by a small amount and updates the derived fields. */
    public void tick(double delta, long extraVolume) {
        lastPrice = Math.max(0.5, round(lastPrice + delta));
        change = round(lastPrice - closePrice);
        if (buy > 0) buy = round(lastPrice - 0.05);
        if (sell > 0) sell = round(lastPrice + 0.05);
        high = Math.max(high, lastPrice);
        low = Math.min(low, lastPrice);
        totalVolume += extraVolume;
        average = round((average * 9 + lastPrice) / 10);
    }

    private static double round(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    public String getMarket() { return market; }
    public String getScrip() { return scrip; }
    public String getCompanyName() { return companyName; }
    public double getLastPrice() { return lastPrice; }
    public double getChange() { return change; }
    public double getBuy() { return buy; }
    public long getBuyVolume() { return buyVolume; }
    public double getSell() { return sell; }
    public long getSellVolume() { return sellVolume; }
    public double getHigh() { return high; }
    public double getLow() { return low; }
    public long getTotalVolume() { return totalVolume; }
    public double getAverage() { return average; }
    public double getClosePrice() { return closePrice; }
}

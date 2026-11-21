package com.akd.tradecast.model;

/** One security held in the client's portfolio (Portfolio Watch grid). */
public class Holding {
    private final String market;
    private final String scrip;
    private final String companyName;
    private final long netQuantity;
    private final double avgPrice;
    private final double currentPrice;
    private final double change;
    private final double buy;
    private final long buyVolume;
    private final double sell;
    private final long sellVolume;
    private final String lastTradeTime;
    private final long totalVolume;
    private final double high;
    private final double low;
    private final double closePrice;
    private final String state;

    public Holding(String market, String scrip, String companyName, long netQuantity, double avgPrice,
                   double currentPrice, double change, double buy, long buyVolume, double sell, long sellVolume,
                   String lastTradeTime, long totalVolume, double high, double low, double closePrice, String state) {
        this.market = market;
        this.scrip = scrip;
        this.companyName = companyName;
        this.netQuantity = netQuantity;
        this.avgPrice = avgPrice;
        this.currentPrice = currentPrice;
        this.change = change;
        this.buy = buy;
        this.buyVolume = buyVolume;
        this.sell = sell;
        this.sellVolume = sellVolume;
        this.lastTradeTime = lastTradeTime;
        this.totalVolume = totalVolume;
        this.high = high;
        this.low = low;
        this.closePrice = closePrice;
        this.state = state;
    }

    /** Amount paid for the holding: quantity x average buying price. */
    public double getInvestment() { return netQuantity * avgPrice; }

    /** Market value today: quantity x current price. */
    public double getCurrentWorth() { return netQuantity * currentPrice; }

    public double getProfitLoss() { return getCurrentWorth() - getInvestment(); }

    /** Up or down arrow shown in the Dir column. */
    public String getDirection() { return change > 0 ? "▲" : change < 0 ? "▼" : "•"; }

    public String getMarket() { return market; }
    public String getScrip() { return scrip; }
    public String getCompanyName() { return companyName; }
    public long getNetQuantity() { return netQuantity; }
    public double getAvgPrice() { return avgPrice; }
    public double getCurrentPrice() { return currentPrice; }
    public double getChange() { return change; }
    public double getBuy() { return buy; }
    public long getBuyVolume() { return buyVolume; }
    public double getSell() { return sell; }
    public long getSellVolume() { return sellVolume; }
    public String getLastTradeTime() { return lastTradeTime; }
    public long getTotalVolume() { return totalVolume; }
    public double getHigh() { return high; }
    public double getLow() { return low; }
    public double getClosePrice() { return closePrice; }
    public String getState() { return state; }
}

package com.ibm.websphere.samples.daytrader.beans;

import java.io.Serializable;

public class RunStatsDataBean implements Serializable {

    public RunStatsDataBean() {
    }

    private static final long serialVersionUID = 4017778674103242167L;

    private int tradeUserCount;

    private int tradeStockCount;

    private int newUserCount;

    private int sumLoginCount;

    private int sumLogoutCount;

    private int holdingCount;

    private int orderCount;

    private int buyOrderCount;

    private int sellOrderCount;

    private int cancelledOrderCount;

    private int openOrderCount;

    private int deletedOrderCount;

    @Override
    public String toString() {
        return (((((((((((((((((((((("\n\tRunStatsData for reset at " + new java.util.Date()) + "\n\t\t      tradeUserCount: ") + getTradeUserCount()) + "\n\t\t        newUserCount: ") + getNewUserCount()) + "\n\t\t       sumLoginCount: ") + getSumLoginCount()) + "\n\t\t      sumLogoutCount: ") + getSumLogoutCount()) + "\n\t\t        holdingCount: ") + getHoldingCount()) + "\n\t\t          orderCount: ") + getOrderCount()) + "\n\t\t       buyOrderCount: ") + getBuyOrderCount()) + "\n\t\t      sellOrderCount: ") + getSellOrderCount()) + "\n\t\t cancelledOrderCount: ") + getCancelledOrderCount()) + "\n\t\t      openOrderCount: ") + getOpenOrderCount()) + "\n\t\t   deletedOrderCount: ") + getDeletedOrderCount();
    }

    public int getTradeUserCount() {
        return tradeUserCount;
    }

    public void setTradeUserCount(int tradeUserCount) {
        this.tradeUserCount = tradeUserCount;
    }

    public int getNewUserCount() {
        return newUserCount;
    }

    public void setNewUserCount(int newUserCount) {
        this.newUserCount = newUserCount;
    }

    public int getSumLoginCount() {
        return sumLoginCount;
    }

    public void setSumLoginCount(int sumLoginCount) {
        this.sumLoginCount = sumLoginCount;
    }

    public int getSumLogoutCount() {
        return sumLogoutCount;
    }

    public void setSumLogoutCount(int sumLogoutCount) {
        this.sumLogoutCount = sumLogoutCount;
    }

    public int getHoldingCount() {
        return holdingCount;
    }

    public void setHoldingCount(int holdingCount) {
        this.holdingCount = holdingCount;
    }

    public int getBuyOrderCount() {
        return buyOrderCount;
    }

    public void setBuyOrderCount(int buyOrderCount) {
        this.buyOrderCount = buyOrderCount;
    }

    public int getSellOrderCount() {
        return sellOrderCount;
    }

    public void setSellOrderCount(int sellOrderCount) {
        this.sellOrderCount = sellOrderCount;
    }

    public int getCancelledOrderCount() {
        return cancelledOrderCount;
    }

    public void setCancelledOrderCount(int cancelledOrderCount) {
        this.cancelledOrderCount = cancelledOrderCount;
    }

    public int getOpenOrderCount() {
        return openOrderCount;
    }

    public void setOpenOrderCount(int openOrderCount) {
        this.openOrderCount = openOrderCount;
    }

    public int getDeletedOrderCount() {
        return deletedOrderCount;
    }

    public void setDeletedOrderCount(int deletedOrderCount) {
        this.deletedOrderCount = deletedOrderCount;
    }

    public int getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(int orderCount) {
        this.orderCount = orderCount;
    }

    public int getTradeStockCount() {
        return tradeStockCount;
    }

    public void setTradeStockCount(int tradeStockCount) {
        this.tradeStockCount = tradeStockCount;
    }
}

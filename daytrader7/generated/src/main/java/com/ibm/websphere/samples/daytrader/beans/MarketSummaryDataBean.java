package com.ibm.websphere.samples.daytrader.beans;

import com.ibm.websphere.samples.daytrader.entities.QuoteDataBean;
import com.ibm.websphere.samples.daytrader.util.FinancialUtils;
import com.ibm.websphere.samples.daytrader.util.Log;
import com.ibm.websphere.samples.daytrader.util.TradeConfig;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

public class MarketSummaryDataBean implements Serializable {

    public MarketSummaryDataBean() {
    }

    public MarketSummaryDataBean(BigDecimal TSIA, BigDecimal openTSIA, double volume, Collection<QuoteDataBean> topGainers, Collection<QuoteDataBean> topLosers) {
        setTSIA(TSIA);
        setOpenTSIA(openTSIA);
        setVolume(volume);
        setTopGainers(topGainers);
        setTopLosers(topLosers);
        setSummaryDate(new java.sql.Date(System.currentTimeMillis()));
        gainPercent = FinancialUtils.computeGainPercent(getTSIA(), getOpenTSIA());
    }

    private static final long serialVersionUID = 650652242288745600L;

    private BigDecimal TSIA;

    private BigDecimal openTSIA;

    private double volume;

    private Collection<QuoteDataBean> topGainers;

    private Collection<QuoteDataBean> topLosers;

    private Date summaryDate;

    private BigDecimal gainPercent = null;

    public static MarketSummaryDataBean getRandomInstance() {
        Collection<QuoteDataBean> gain = new ArrayList<QuoteDataBean>();
        Collection<QuoteDataBean> lose = new ArrayList<QuoteDataBean>();

        for (int ii = 0; ii < 5; ii++) {
            QuoteDataBean quote1 = QuoteDataBean.getRandomInstance();
            QuoteDataBean quote2 = QuoteDataBean.getRandomInstance();

            gain.add(quote1);
            lose.add(quote2);
        }

        return new MarketSummaryDataBean(TradeConfig.rndBigDecimal(1000000.0f), TradeConfig.rndBigDecimal(1000000.0f), TradeConfig.rndQuantity(), gain, lose);
    }

    @Override
    public String toString() {
        String ret = "\n\tMarket Summary at: " + getSummaryDate() + "\n\t\t        TSIA:" + getTSIA() + "\n\t\t    openTSIA:" + getOpenTSIA()
                + "\n\t\t        gain:" + getGainPercent() + "\n\t\t      volume:" + getVolume();

        if ((getTopGainers() == null) || (getTopLosers() == null)) {
            return ret;
        }
        ret += "\n\t\t   Current Top Gainers:";
        Iterator<QuoteDataBean> it = getTopGainers().iterator();
        while (it.hasNext()) {
            QuoteDataBean quoteData = it.next();
            ret += ("\n\t\t\t" + quoteData.toString());
        }
        ret += "\n\t\t   Current Top Losers:";
        it = getTopLosers().iterator();
        while (it.hasNext()) {
            QuoteDataBean quoteData = it.next();
            ret += ("\n\t\t\t" + quoteData.toString());
        }
        return ret;
    }

    public String toHTML() {
        String ret = "<BR>Market Summary at: " + getSummaryDate() + "<LI>        TSIA:" + getTSIA() + "</LI>" + "<LI>    openTSIA:" + getOpenTSIA() + "</LI>"
                + "<LI>      volume:" + getVolume() + "</LI>";
        if ((getTopGainers() == null) || (getTopLosers() == null)) {
            return ret;
        }
        ret += "<BR> Current Top Gainers:";
        Iterator<QuoteDataBean> it = getTopGainers().iterator();

        while (it.hasNext()) {
            QuoteDataBean quoteData = it.next();
            ret += ("<LI>" + quoteData.toString() + "</LI>");
        }
        ret += "<BR>   Current Top Losers:";
        it = getTopLosers().iterator();
        while (it.hasNext()) {
            QuoteDataBean quoteData = it.next();
            ret += ("<LI>" + quoteData.toString() + "</LI>");
        }
        return ret;
    }

    public JsonObject toJSON() {
        JsonObjectBuilder jObjectBuilder = Json.createObjectBuilder();
        
        int i = 1;
        for (Iterator<QuoteDataBean> iterator = topGainers.iterator(); iterator.hasNext();) {
            QuoteDataBean quote = iterator.next();

            jObjectBuilder.add("gainer" + i + "_stock",quote.getSymbol());
            jObjectBuilder.add("gainer" + i + "_price","$" + quote.getPrice());
            jObjectBuilder.add("gainer" + i + "_change",quote.getChange());
            i++;
        }

        i = 1;
        for (Iterator<QuoteDataBean> iterator = topLosers.iterator(); iterator.hasNext();) {
            QuoteDataBean quote = iterator.next();

            jObjectBuilder.add("loser" + i + "_stock",quote.getSymbol());
            jObjectBuilder.add("loser" + i + "_price","$" + quote.getPrice());
            jObjectBuilder.add("loser" + i + "_change",quote.getChange());
            i++;
        }

        jObjectBuilder.add("tsia", TSIA);
        jObjectBuilder.add("volume",volume);
        jObjectBuilder.add("date", summaryDate.toString());

        return jObjectBuilder.build();
    }

    public void print() {
        Log.log(this.toString());
    }

    public BigDecimal getGainPercent() {
        if (gainPercent == null) {
            gainPercent = FinancialUtils.computeGainPercent(getTSIA(), getOpenTSIA());
        }
        return gainPercent;
    }

    public BigDecimal getTSIA() {
        return TSIA;
    }

    public void setTSIA(BigDecimal tSIA) {
        TSIA = tSIA;
    }

    public BigDecimal getOpenTSIA() {
        return openTSIA;
    }

    public void setOpenTSIA(BigDecimal openTSIA) {
        this.openTSIA = openTSIA;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public Collection<QuoteDataBean> getTopGainers() {
        return topGainers;
    }

    public void setTopGainers(Collection<QuoteDataBean> topGainers) {
        this.topGainers = topGainers;
    }

    public Collection<QuoteDataBean> getTopLosers() {
        return topLosers;
    }

    public void setTopLosers(Collection<QuoteDataBean> topLosers) {
        this.topLosers = topLosers;
    }

    public Date getSummaryDate() {
        return summaryDate;
    }

    public void setSummaryDate(Date summaryDate) {
        this.summaryDate = summaryDate;
    }
}

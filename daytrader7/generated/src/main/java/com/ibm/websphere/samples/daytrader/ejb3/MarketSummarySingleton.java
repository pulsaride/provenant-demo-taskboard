package com.ibm.websphere.samples.daytrader.ejb3;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.PersistenceContext;

import com.ibm.websphere.samples.daytrader.beans.MarketSummaryDataBean;
import com.ibm.websphere.samples.daytrader.entities.QuoteDataBean;
import com.ibm.websphere.samples.daytrader.util.FinancialUtils;
import com.ibm.websphere.samples.daytrader.util.Log;
import com.ibm.websphere.samples.daytrader.util.TradeConfig;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@Transactional(propagation = Propagation.REQUIRED)
public class MarketSummarySingleton {

    private MarketSummaryDataBean marketSummaryDataBean;

    @PersistenceContext
    private EntityManager entityManager;

    @PostConstruct
    private void setup() {
        updateMarketSummary();
    }

    private void updateMarketSummary() {
        if (Log.doTrace()) {
            Log.trace("MarketSummarySingleton:updateMarketSummary -- updating market summary");
        }
                        
        if (TradeConfig.getRunTimeMode() != TradeConfig.EJB3)
        {
            if (Log.doTrace()) {
                Log.trace("MarketSummarySingleton:updateMarketSummary -- Not EJB3 Mode, so not updating");
            }
            return; // Only do the actual work if in EJB3 Mode
        }
        
        List<QuoteDataBean> quotes;
        
        try {        
        	// Find Trade Stock Index Quotes (Top 100 quotes) ordered by their change in value
        	CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        	CriteriaQuery<QuoteDataBean> criteriaQuery = criteriaBuilder.createQuery(QuoteDataBean.class);
        	Root<QuoteDataBean> quoteRoot = criteriaQuery.from(QuoteDataBean.class);
        	criteriaQuery.orderBy(criteriaBuilder.desc(quoteRoot.get("change1")));
        	criteriaQuery.select(quoteRoot);
        	TypedQuery<QuoteDataBean> q = entityManager.createQuery(criteriaQuery);
        	quotes = q.getResultList();
        } catch (Exception e) {
        	Log.debug("Warning: The database has not been configured. If this is the first time the application has been started, please create and populate the database tables. Then restart the server.");
        	return;
        }	
                
        /* TODO: Make this cleaner? */
        QuoteDataBean[] quoteArray = quotes.toArray(new QuoteDataBean[quotes.size()]);
        ArrayList<QuoteDataBean> topGainers = new ArrayList<QuoteDataBean>(5);
        ArrayList<QuoteDataBean> topLosers = new ArrayList<QuoteDataBean>(5);
        BigDecimal TSIA = FinancialUtils.ZERO;
        BigDecimal openTSIA = FinancialUtils.ZERO;
        double totalVolume = 0.0;

        if (quoteArray.length > 5) {
            for (int i = 0; i < 5; i++) {
                topGainers.add(quoteArray[i]);
            }
            for (int i = quoteArray.length - 1; i >= quoteArray.length - 5; i--) {
                topLosers.add(quoteArray[i]);
            }

            for (QuoteDataBean quote : quoteArray) {
                BigDecimal price = quote.getPrice();
                BigDecimal open = quote.getOpen();
                double volume = quote.getVolume();
                TSIA = TSIA.add(price);
                openTSIA = openTSIA.add(open);
                totalVolume += volume;
            }
            TSIA = TSIA.divide(new BigDecimal(quoteArray.length), FinancialUtils.ROUND);
            openTSIA = openTSIA.divide(new BigDecimal(quoteArray.length), FinancialUtils.ROUND);
        }
        
        setMarketSummaryDataBean(new MarketSummaryDataBean(TSIA, openTSIA, totalVolume, topGainers, topLosers));
    }

    public MarketSummaryDataBean getMarketSummaryDataBean() {
        return marketSummaryDataBean;
    }

    public void setMarketSummaryDataBean(MarketSummaryDataBean marketSummaryDataBean) {
        this.marketSummaryDataBean = marketSummaryDataBean;
    }
}

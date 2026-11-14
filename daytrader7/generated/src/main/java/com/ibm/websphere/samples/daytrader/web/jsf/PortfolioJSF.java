package com.ibm.websphere.samples.daytrader.web.jsf;

import org.springframework.web.context.annotation.RequestScope;

import com.ibm.websphere.samples.daytrader.TradeAction;
import com.ibm.websphere.samples.daytrader.entities.HoldingDataBean;
import com.ibm.websphere.samples.daytrader.entities.OrderDataBean;
import com.ibm.websphere.samples.daytrader.entities.QuoteDataBean;
import com.ibm.websphere.samples.daytrader.util.FinancialUtils;
import com.ibm.websphere.samples.daytrader.util.TradeConfig;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

// TODO(field: injection_point_jsf_api_type_not_emitted: facesExternalContext: ExternalContext)
// TODO(field: jsf_api_type_not_emitted: dataTable: HtmlDataTable)
@Named(value = "portfolio")
@RequestScope
public class PortfolioJSF {

    private final TradeAction tradeAction;

    private BigDecimal balance;

    private BigDecimal openBalance;

    private Integer numberHoldings;

    private BigDecimal holdingsTotal;

    private BigDecimal sumOfCashHoldings;

    private BigDecimal totalGain = new BigDecimal(0.0);

    private BigDecimal totalValue = new BigDecimal(0.0);

    private BigDecimal totalBasis = new BigDecimal(0.0);

    private BigDecimal totalGainPercent = new BigDecimal(0.0);

    private ArrayList<HoldingData> holdingDatas;

    public PortfolioJSF(TradeAction tradeAction) {
        this.tradeAction = tradeAction;
    }
    // TODO(methods_dropped_legacy_signature: setDataTable, getDataTable)

    // TODO(body: retention_calls_stubbed_member:facesExternalContext)
    // TODO(annotation: PostConstruct, body_not_extracted)
    public void getPortfolio() {
        throw new provenant.engine.EngineStubException("body not extracted for getPortfolio — see lower::lir's module docs");
    }

    // TODO(body: retention_calls_stubbed_member:dataTable)
    public String sell() {
        throw new provenant.engine.EngineStubException("body not extracted for sell — see lower::lir's module docs");
    }

    // setDataTable() not reproduced: its real signature needs javax.faces.component.html.HtmlDataTable — no equivalent in the target stack.

    // getDataTable() not reproduced: its real signature needs javax.faces.component.html.HtmlDataTable — no equivalent in the target stack.

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setOpenBalance(BigDecimal openBalance) {
        this.openBalance = openBalance;
    }

    public BigDecimal getOpenBalance() {
        return openBalance;
    }

    public void setHoldingsTotal(BigDecimal holdingsTotal) {
        this.holdingsTotal = holdingsTotal;
    }

    public BigDecimal getHoldingsTotal() {
        return holdingsTotal;
    }

    public void setSumOfCashHoldings(BigDecimal sumOfCashHoldings) {
        this.sumOfCashHoldings = sumOfCashHoldings;
    }

    public BigDecimal getSumOfCashHoldings() {
        return sumOfCashHoldings;
    }

    public void setNumberHoldings(Integer numberHoldings) {
        this.numberHoldings = numberHoldings;
    }

    public Integer getNumberHoldings() {
        return numberHoldings;
    }

    public void setTotalGain(BigDecimal totalGain) {
        this.totalGain = totalGain;
    }

    public BigDecimal getTotalGain() {
        return totalGain;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalBasis(BigDecimal totalBasis) {
        this.totalBasis = totalBasis;
    }

    public BigDecimal getTotalBasis() {
        return totalBasis;
    }

    public void setHoldingDatas(ArrayList<HoldingData> holdingDatas) {
        this.holdingDatas = holdingDatas;
    }

    public ArrayList<HoldingData> getHoldingDatas() {
        return holdingDatas;
    }

    public void setTotalGainPercent(BigDecimal totalGainPercent) {
        this.totalGainPercent = totalGainPercent;
    }

    public BigDecimal getTotalGainPercent() {
        return totalGainPercent;
    }

    public String getTotalGainPercentHTML() {
        return FinancialUtils.printGainPercentHTML(totalGainPercent);
    }
}

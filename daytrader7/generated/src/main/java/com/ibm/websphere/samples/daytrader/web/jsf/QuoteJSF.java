package com.ibm.websphere.samples.daytrader.web.jsf;

import org.springframework.web.context.annotation.RequestScope;

import com.ibm.websphere.samples.daytrader.TradeAction;
import com.ibm.websphere.samples.daytrader.entities.OrderDataBean;
import com.ibm.websphere.samples.daytrader.entities.QuoteDataBean;
import com.ibm.websphere.samples.daytrader.util.Log;
import com.ibm.websphere.samples.daytrader.util.TradeConfig;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;

// TODO(field: injection_point_jsf_api_type_not_emitted: facesExternalContext: ExternalContext)
// TODO(field: jsf_api_type_not_emitted: dataTable: HtmlDataTable)
@Named(value = "quotedata")
@RequestScope
public class QuoteJSF {

    private final TradeAction tradeAction;

    private QuoteData[] quotes;

    private String symbols = null;

    private Integer quantity = 100;

    public QuoteJSF(TradeAction tradeAction) {
        this.tradeAction = tradeAction;
    }
    // TODO(methods_dropped_legacy_signature: setDataTable, getDataTable)

    // TODO(body: retention_calls_stubbed_member:getQuotesBySymbols)
    // TODO(annotation: PostConstruct, body_not_extracted)
    public void getAllQuotes() {
        throw new provenant.engine.EngineStubException("body not extracted for getAllQuotes — see lower::lir's module docs");
    }

    // TODO(body: retention_calls_stubbed_member:facesExternalContext)
    public String getQuotesBySymbols() {
        throw new provenant.engine.EngineStubException("body not extracted for getQuotesBySymbols — see lower::lir's module docs");
    }

    // TODO(body: retention_calls_stubbed_member:dataTable)
    public String buy() {
        throw new provenant.engine.EngineStubException("body not extracted for buy — see lower::lir's module docs");
    }

    public void setQuotes(QuoteData[] quotes) {
        this.quotes = quotes;
    }

    public QuoteData[] getQuotes() {
        return quotes;
    }

    public void setSymbols(String symbols) {
        this.symbols = symbols;
    }

    public String getSymbols() {
        return symbols;
    }

    // setDataTable() not reproduced: its real signature needs javax.faces.component.html.HtmlDataTable — no equivalent in the target stack.

    // getDataTable() not reproduced: its real signature needs javax.faces.component.html.HtmlDataTable — no equivalent in the target stack.

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantity() {
        return quantity;
    }
}

package com.ibm.websphere.samples.daytrader;

import com.ibm.websphere.samples.daytrader.web.jsf.AccountDataJSF;
import com.ibm.websphere.samples.daytrader.web.jsf.PortfolioJSF;
import com.ibm.websphere.samples.daytrader.web.jsf.QuoteJSF;
import com.ibm.websphere.samples.daytrader.web.jsf.TradeAppJSF;
import com.ibm.websphere.samples.daytrader.web.prims.PingCDIJSFBean;
import java.math.BigDecimal;
import java.util.Date;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Daytrader7Controller {

    private final ObjectProvider<AccountDataJSF> accountDataJSFProvider;
    private final ObjectProvider<PortfolioJSF> portfolioJSFProvider;
    private final ObjectProvider<QuoteJSF> quoteJSFProvider;
    private final ObjectProvider<TradeAppJSF> tradeAppJSFProvider;
    private final ObjectProvider<PingCDIJSFBean> pingCDIJSFBeanProvider;

    public Daytrader7Controller(ObjectProvider<AccountDataJSF> accountDataJSFProvider, ObjectProvider<PortfolioJSF> portfolioJSFProvider, ObjectProvider<QuoteJSF> quoteJSFProvider, ObjectProvider<TradeAppJSF> tradeAppJSFProvider, ObjectProvider<PingCDIJSFBean> pingCDIJSFBeanProvider) {
        this.accountDataJSFProvider = accountDataJSFProvider;
        this.portfolioJSFProvider = portfolioJSFProvider;
        this.quoteJSFProvider = quoteJSFProvider;
        this.tradeAppJSFProvider = tradeAppJSFProvider;
        this.pingCDIJSFBeanProvider = pingCDIJSFBeanProvider;
    }

    // delegates to the retained backing bean com.ibm.websphere.samples.daytrader.web.jsf.AccountDataJSF (one Spring-managed instance per call; legacy scope @RequestScoped)
    // TODO(endpoint_security_unproven: no_declaration)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/accountdata/logoutCount")
    public ResponseEntity<Integer> apiAccountdataLogoutcount() {
        AccountDataJSF bean = accountDataJSFProvider.getObject();
        return ResponseEntity.ok(bean.getLogoutCount());
    }

    // delegates to the retained backing bean com.ibm.websphere.samples.daytrader.web.jsf.PortfolioJSF (one Spring-managed instance per call; legacy scope @RequestScoped)
    // TODO(endpoint_security_unproven: no_declaration)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/portfolio/totalGain")
    public ResponseEntity<BigDecimal> apiPortfolioTotalgain() {
        PortfolioJSF bean = portfolioJSFProvider.getObject();
        return ResponseEntity.ok(bean.getTotalGain());
    }

    // delegates to the retained backing bean com.ibm.websphere.samples.daytrader.web.jsf.AccountDataJSF (one Spring-managed instance per call; legacy scope @RequestScoped)
    // TODO(endpoint_security_unproven: no_declaration)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/accountdata/accountID")
    public ResponseEntity<Integer> apiAccountdataAccountid() {
        AccountDataJSF bean = accountDataJSFProvider.getObject();
        return ResponseEntity.ok(bean.getAccountID());
    }

    // delegates to the retained backing bean com.ibm.websphere.samples.daytrader.web.jsf.AccountDataJSF (one Spring-managed instance per call; legacy scope @RequestScoped)
    // TODO(endpoint_security_unproven: no_declaration)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/accountdata/profileID")
    public ResponseEntity<String> apiAccountdataProfileid() {
        AccountDataJSF bean = accountDataJSFProvider.getObject();
        return ResponseEntity.ok(bean.getProfileID());
    }

    // delegates to the retained backing bean com.ibm.websphere.samples.daytrader.web.jsf.QuoteJSF (one Spring-managed instance per call; legacy scope @RequestScoped)
    // TODO(endpoint_security_unproven: no_declaration)
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/api/quotedata/buy")
    public ResponseEntity<Void> apiQuotedataBuy(@RequestBody QuotedataBuyRequest body) {
        QuoteJSF bean = quoteJSFProvider.getObject();
        bean.setQuantity(body.quantity());
        bean.setSymbols(body.symbols());
        bean.buy();
        return ResponseEntity.noContent().build();
    }

    // the form values of the command, applied as the legacy's Update Model phase did
    public record QuotedataBuyRequest(Integer quantity, String symbols) {}

    // delegates to the retained backing bean com.ibm.websphere.samples.daytrader.web.jsf.AccountDataJSF (one Spring-managed instance per call; legacy scope @RequestScoped)
    // TODO(endpoint_security_unproven: no_declaration)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/accountdata/gainHTML")
    public ResponseEntity<String> apiAccountdataGainhtml() {
        AccountDataJSF bean = accountDataJSFProvider.getObject();
        return ResponseEntity.ok(bean.getGainHTML());
    }

    // delegates to the retained backing bean com.ibm.websphere.samples.daytrader.web.jsf.AccountDataJSF (one Spring-managed instance per call; legacy scope @RequestScoped)
    // TODO(endpoint_security_unproven: no_declaration)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/accountdata/loginCount")
    public ResponseEntity<Integer> apiAccountdataLogincount() {
        AccountDataJSF bean = accountDataJSFProvider.getObject();
        return ResponseEntity.ok(bean.getLoginCount());
    }

    // delegates to the retained backing bean com.ibm.websphere.samples.daytrader.web.jsf.AccountDataJSF (one Spring-managed instance per call; legacy scope @RequestScoped)
    // TODO(endpoint_security_unproven: no_declaration)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/accountdata/numberHoldings")
    public ResponseEntity<Integer> apiAccountdataNumberholdings() {
        AccountDataJSF bean = accountDataJSFProvider.getObject();
        return ResponseEntity.ok(bean.getNumberHoldings());
    }

    // delegates to the retained backing bean com.ibm.websphere.samples.daytrader.web.prims.PingCDIJSFBean (one Spring-managed instance per call; legacy scope @SessionScoped)
    // TODO(endpoint_security_unproven: no_declaration)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/pingCDIJSFBean/hitCount")
    public ResponseEntity<Integer> apiPingcdijsfbeanHitcount() {
        PingCDIJSFBean bean = pingCDIJSFBeanProvider.getObject();
        return ResponseEntity.ok(bean.getHitCount());
    }

    // delegates to the retained backing bean com.ibm.websphere.samples.daytrader.web.jsf.AccountDataJSF (one Spring-managed instance per call; legacy scope @RequestScoped)
    // TODO(endpoint_security_unproven: no_declaration)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/accountdata/creationDate")
    public ResponseEntity<Date> apiAccountdataCreationdate() {
        AccountDataJSF bean = accountDataJSFProvider.getObject();
        return ResponseEntity.ok(bean.getCreationDate());
    }

    // delegates to the retained backing bean com.ibm.websphere.samples.daytrader.web.jsf.TradeAppJSF (one Spring-managed instance per call; legacy scope @SessionScoped)
    // TODO(endpoint_security_unproven: no_declaration)
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/api/tradeapp/updateProfile")
    public ResponseEntity<Void> apiTradeappUpdateprofile(@RequestBody TradeappUpdateProfileRequest body) {
        TradeAppJSF bean = tradeAppJSFProvider.getObject();
        bean.setAddress(body.address());
        bean.setCcn(body.ccn());
        bean.setCpassword(body.cpassword());
        bean.setEmail(body.email());
        bean.setFullname(body.fullname());
        bean.setPassword(body.password());
        bean.setUserID(body.userID());
        bean.updateProfile();
        return ResponseEntity.noContent().build();
    }

    // the form values of the command, applied as the legacy's Update Model phase did
    public record TradeappUpdateProfileRequest(String address, String ccn, String cpassword, String email, String fullname, String password, String userID) {}

    // delegates to the retained backing bean com.ibm.websphere.samples.daytrader.web.jsf.AccountDataJSF (one Spring-managed instance per call; legacy scope @RequestScoped)
    // TODO(endpoint_security_unproven: no_declaration)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/accountdata/currentTime")
    public ResponseEntity<Date> apiAccountdataCurrenttime() {
        AccountDataJSF bean = accountDataJSFProvider.getObject();
        return ResponseEntity.ok(bean.getCurrentTime());
    }

    // delegates to the retained backing bean com.ibm.websphere.samples.daytrader.web.jsf.TradeAppJSF (one Spring-managed instance per call; legacy scope @SessionScoped)
    // TODO(endpoint_security_unproven: no_declaration)
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/api/tradeapp/logout")
    public ResponseEntity<Void> apiTradeappLogout(@RequestBody TradeappLogoutRequest body) {
        TradeAppJSF bean = tradeAppJSFProvider.getObject();
        bean.setAddress(body.address());
        bean.setCcn(body.ccn());
        bean.setCpassword(body.cpassword());
        bean.setEmail(body.email());
        bean.setFullname(body.fullname());
        bean.setPassword(body.password());
        bean.setUserID(body.userID());
        bean.logout();
        return ResponseEntity.noContent().build();
    }

    // the form values of the command, applied as the legacy's Update Model phase did
    public record TradeappLogoutRequest(String address, String ccn, String cpassword, String email, String fullname, String password, String userID) {}

    // delegates to the retained backing bean com.ibm.websphere.samples.daytrader.web.jsf.PortfolioJSF (one Spring-managed instance per call; legacy scope @RequestScoped)
    // TODO(endpoint_security_unproven: no_declaration)
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/api/portfolio/sell")
    public ResponseEntity<Void> apiPortfolioSell() {
        PortfolioJSF bean = portfolioJSFProvider.getObject();
        bean.sell();
        return ResponseEntity.noContent().build();
    }

    // delegates to the retained backing bean com.ibm.websphere.samples.daytrader.web.jsf.PortfolioJSF (one Spring-managed instance per call; legacy scope @RequestScoped)
    // TODO(endpoint_security_unproven: no_declaration)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/portfolio/numberHoldings")
    public ResponseEntity<Integer> apiPortfolioNumberholdings() {
        PortfolioJSF bean = portfolioJSFProvider.getObject();
        return ResponseEntity.ok(bean.getNumberHoldings());
    }

    // delegates to the retained backing bean com.ibm.websphere.samples.daytrader.web.jsf.PortfolioJSF (one Spring-managed instance per call; legacy scope @RequestScoped)
    // TODO(endpoint_security_unproven: no_declaration)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/portfolio/totalBasis")
    public ResponseEntity<BigDecimal> apiPortfolioTotalbasis() {
        PortfolioJSF bean = portfolioJSFProvider.getObject();
        return ResponseEntity.ok(bean.getTotalBasis());
    }

    // delegates to the retained backing bean com.ibm.websphere.samples.daytrader.web.jsf.TradeAppJSF (one Spring-managed instance per call; legacy scope @SessionScoped)
    // TODO(endpoint_security_unproven: no_declaration)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/tradeapp/results")
    public ResponseEntity<String> apiTradeappResults() {
        TradeAppJSF bean = tradeAppJSFProvider.getObject();
        return ResponseEntity.ok(bean.getResults());
    }

    // delegates to the retained backing bean com.ibm.websphere.samples.daytrader.web.jsf.AccountDataJSF (one Spring-managed instance per call; legacy scope @RequestScoped)
    // TODO(endpoint_security_unproven: no_declaration)
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/api/accountdata/toggleShowAllRows")
    public ResponseEntity<Void> apiAccountdataToggleshowallrows() {
        AccountDataJSF bean = accountDataJSFProvider.getObject();
        bean.toggleShowAllRows();
        return ResponseEntity.noContent().build();
    }

    // delegates to the retained backing bean com.ibm.websphere.samples.daytrader.web.jsf.AccountDataJSF (one Spring-managed instance per call; legacy scope @RequestScoped)
    // TODO(endpoint_security_unproven: no_declaration)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/accountdata/holdingsTotal")
    public ResponseEntity<BigDecimal> apiAccountdataHoldingstotal() {
        AccountDataJSF bean = accountDataJSFProvider.getObject();
        return ResponseEntity.ok(bean.getHoldingsTotal());
    }

    // delegates to the retained backing bean com.ibm.websphere.samples.daytrader.web.jsf.AccountDataJSF (one Spring-managed instance per call; legacy scope @RequestScoped)
    // TODO(endpoint_security_unproven: no_declaration)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/accountdata/gainPercentHTML")
    public ResponseEntity<String> apiAccountdataGainpercenthtml() {
        AccountDataJSF bean = accountDataJSFProvider.getObject();
        return ResponseEntity.ok(bean.getGainPercentHTML());
    }

    // delegates to the retained backing bean com.ibm.websphere.samples.daytrader.web.jsf.AccountDataJSF (one Spring-managed instance per call; legacy scope @RequestScoped)
    // TODO(endpoint_security_unproven: no_declaration)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/accountdata/sessionCreationDate")
    public ResponseEntity<Date> apiAccountdataSessioncreationdate() {
        AccountDataJSF bean = accountDataJSFProvider.getObject();
        return ResponseEntity.ok(bean.getSessionCreationDate());
    }

    // delegates to the retained backing bean com.ibm.websphere.samples.daytrader.web.jsf.QuoteJSF (one Spring-managed instance per call; legacy scope @RequestScoped)
    // TODO(endpoint_security_unproven: no_declaration)
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/api/quotedata/getQuotesBySymbols")
    public ResponseEntity<Void> apiQuotedataGetquotesbysymbols(@RequestBody String body) {
        QuoteJSF bean = quoteJSFProvider.getObject();
        bean.setSymbols(body);
        bean.getQuotesBySymbols();
        return ResponseEntity.noContent().build();
    }

    // delegates to the retained backing bean com.ibm.websphere.samples.daytrader.web.jsf.TradeAppJSF (one Spring-managed instance per call; legacy scope @SessionScoped)
    // TODO(endpoint_security_unproven: no_declaration)
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/api/tradeapp/login")
    public ResponseEntity<Void> apiTradeappLogin(@RequestBody TradeappLoginRequest body) {
        TradeAppJSF bean = tradeAppJSFProvider.getObject();
        bean.setPassword(body.password());
        bean.setUserID(body.userID());
        bean.login();
        return ResponseEntity.noContent().build();
    }

    // the form values of the command, applied as the legacy's Update Model phase did
    public record TradeappLoginRequest(String password, String userID) {}

    // delegates to the retained backing bean com.ibm.websphere.samples.daytrader.web.jsf.AccountDataJSF (one Spring-managed instance per call; legacy scope @RequestScoped)
    // TODO(endpoint_security_unproven: no_declaration)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/accountdata/balance")
    public ResponseEntity<BigDecimal> apiAccountdataBalance() {
        AccountDataJSF bean = accountDataJSFProvider.getObject();
        return ResponseEntity.ok(bean.getBalance());
    }

    // delegates to the retained backing bean com.ibm.websphere.samples.daytrader.web.jsf.PortfolioJSF (one Spring-managed instance per call; legacy scope @RequestScoped)
    // TODO(endpoint_security_unproven: no_declaration)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/portfolio/totalValue")
    public ResponseEntity<BigDecimal> apiPortfolioTotalvalue() {
        PortfolioJSF bean = portfolioJSFProvider.getObject();
        return ResponseEntity.ok(bean.getTotalValue());
    }

    // delegates to the retained backing bean com.ibm.websphere.samples.daytrader.web.jsf.AccountDataJSF (one Spring-managed instance per call; legacy scope @RequestScoped)
    // TODO(endpoint_security_unproven: no_declaration)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/accountdata/lastLogin")
    public ResponseEntity<Date> apiAccountdataLastlogin() {
        AccountDataJSF bean = accountDataJSFProvider.getObject();
        return ResponseEntity.ok(bean.getLastLogin());
    }

    // delegates to the retained backing bean com.ibm.websphere.samples.daytrader.web.jsf.AccountDataJSF (one Spring-managed instance per call; legacy scope @RequestScoped)
    // TODO(endpoint_security_unproven: no_declaration)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/accountdata/openBalance")
    public ResponseEntity<BigDecimal> apiAccountdataOpenbalance() {
        AccountDataJSF bean = accountDataJSFProvider.getObject();
        return ResponseEntity.ok(bean.getOpenBalance());
    }

    // delegates to the retained backing bean com.ibm.websphere.samples.daytrader.web.jsf.AccountDataJSF (one Spring-managed instance per call; legacy scope @RequestScoped)
    // TODO(endpoint_security_unproven: no_declaration)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/accountdata/sumOfCashHoldings")
    public ResponseEntity<BigDecimal> apiAccountdataSumofcashholdings() {
        AccountDataJSF bean = accountDataJSFProvider.getObject();
        return ResponseEntity.ok(bean.getSumOfCashHoldings());
    }

    // delegates to the retained backing bean com.ibm.websphere.samples.daytrader.web.jsf.PortfolioJSF (one Spring-managed instance per call; legacy scope @RequestScoped)
    // TODO(endpoint_security_unproven: no_declaration)
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/api/portfolio/totalGainPercentHTML")
    public ResponseEntity<String> apiPortfolioTotalgainpercenthtml() {
        PortfolioJSF bean = portfolioJSFProvider.getObject();
        return ResponseEntity.ok(bean.getTotalGainPercentHTML());
    }
}

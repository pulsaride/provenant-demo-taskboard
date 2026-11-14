package com.ibm.websphere.samples.daytrader.web.jsf;

import org.springframework.web.context.annotation.SessionScope;

import com.ibm.websphere.samples.daytrader.TradeAction;
import com.ibm.websphere.samples.daytrader.entities.AccountDataBean;
import com.ibm.websphere.samples.daytrader.entities.AccountProfileDataBean;
import com.ibm.websphere.samples.daytrader.util.Log;
import jakarta.inject.Named;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.Serializable;
import java.math.BigDecimal;

// TODO(field: injection_point_jsf_api_type_not_emitted: facesExternalContext: ExternalContext)
@Named(value = "tradeapp")
@SessionScope
public class TradeAppJSF implements Serializable {

    private final TradeAction tradeAction;

    private static final long serialVersionUID = 2L;

    private String userID = "uid:0";

    private String password = "xxx";

    private String cpassword;

    private String results;

    private String fullname;

    private String address;

    private String email;

    private String ccn;

    private String money;

    public TradeAppJSF(TradeAction tradeAction) {
        this.tradeAction = tradeAction;
    }

    // TODO(body: retention_calls_stubbed_member:facesExternalContext)
    public String login() {
        throw new provenant.engine.EngineStubException("body not extracted for login — see lower::lir's module docs");
    }

    // TODO(body: retention_calls_stubbed_member:login)
    public String register() {
        throw new provenant.engine.EngineStubException("body not extracted for register — see lower::lir's module docs");
    }

    public String updateProfile() {
        TradeAction tAction = new TradeAction();
        // First verify input data
        boolean doUpdate = true;

        if (password.equals(cpassword) == false) {
            results = "Update profile error: passwords do not match";
            doUpdate = false;
        }

        AccountProfileDataBean accountProfileData = new AccountProfileDataBean(userID, password, fullname, address, email, ccn);

        try {
            if (doUpdate) {
                accountProfileData = tAction.updateAccountProfile(accountProfileData);
                results = "Account profile update successful";
            }

        } catch (java.lang.IllegalArgumentException e) {
            // this is a user error so I will
            // forward them to another page rather than throw a 500
            setResults("invalid argument, check userID is correct, and the database is populated" + userID);
            Log.error(e, "TradeServletAction.doAccount(...)", "illegal argument, information should be in exception string",
                    "treating this as a user error and forwarding on to a new page");
        } catch (Exception e) {
            // log the exception with error page
            e.printStackTrace();
        }
        // Go to account.xhtml
        return "Go to account";
    }

    // TODO(body: retention_calls_stubbed_member:facesExternalContext)
    public String logout() {
        throw new provenant.engine.EngineStubException("body not extracted for logout — see lower::lir's module docs");
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCpassword() {
        return cpassword;
    }

    public void setCpassword(String cpassword) {
        this.cpassword = cpassword;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getResults() {
        String tempResults = results;
        results = "";
        return tempResults;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCcn() {
        return ccn;
    }

    public void setCcn(String ccn) {
        this.ccn = ccn;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}

package com.ibm.websphere.samples.daytrader.entities;

import jakarta.validation.constraints.NotNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import com.ibm.websphere.samples.daytrader.util.Log;
import com.ibm.websphere.samples.daytrader.util.TradeConfig;
import jakarta.persistence.FetchType;

@Entity(name = "accountprofileejb")
@Table(name = "accountprofileejb")
public class AccountProfileDataBean implements java.io.Serializable {

    public AccountProfileDataBean() {
    }

    public AccountProfileDataBean(String userID, String password, String fullName, String address, String email, String creditCard) {
        setUserID(userID);
        setPassword(password);
        setFullName(fullName);
        setAddress(address);
        setEmail(email);
        setCreditCard(creditCard);
    }

    private static final long serialVersionUID = 2794584136675420624L;

    @Id
    @NotNull
    @Column(name = "USERID", nullable = false)
    private String userID;

    @Column(name = "PASSWD")
    private String passwd;

    @Column(name = "FULLNAME")
    private String fullName;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "CREDITCARD")
    private String creditCard;

    @OneToOne(mappedBy = "profile", fetch = FetchType.LAZY)
    private AccountDataBean account;

    // TODO(body: retention_shadowed_type_name)
    public static AccountProfileDataBean getRandomInstance() {
        throw new provenant.engine.EngineStubException("body not extracted for getRandomInstance — see lower::lir's module docs");
    }

    @Override
    public String toString() {
        return (((((((((("\n\tAccount Profile Data for userID:" + getUserID()) + "\n\t\t   passwd:") + getPassword()) + "\n\t\t   fullName:") + getFullName()) + "\n\t\t    address:") + getAddress()) + "\n\t\t      email:") + getEmail()) + "\n\t\t creditCard:") + getCreditCard();
    }

    public String toHTML() {
        return (((((((((((((((("<BR>Account Profile Data for userID: <B>" + getUserID()) + "</B>") + "<LI>   passwd:") + getPassword()) + "</LI>") + "<LI>   fullName:") + getFullName()) + "</LI>") + "<LI>    address:") + getAddress()) + "</LI>") + "<LI>      email:") + getEmail()) + "</LI>") + "<LI> creditCard:") + getCreditCard()) + "</LI>";
    }

    public void print() {
        Log.log(this.toString());
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return passwd;
    }

    public void setPassword(String password) {
        this.passwd = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public AccountDataBean getAccount() {
        return account;
    }

    public void setAccount(AccountDataBean account) {
        this.account = account;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.userID != null ? this.userID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AccountProfileDataBean)) {
            return false;
        }
        AccountProfileDataBean other = (AccountProfileDataBean) object;

        if (this.userID != other.userID && (this.userID == null || !this.userID.equals(other.userID))) {
            return false;
        }

        return true;
    }
}

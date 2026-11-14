package com.ibm.websphere.samples.daytrader.util;

import com.ibm.websphere.samples.daytrader.TradeServices;
import com.ibm.websphere.samples.daytrader.direct.TradeDirect;
import com.ibm.websphere.samples.daytrader.ejb3.TradeSLSBBean;
import jakarta.ejb.EJBException;
import jakarta.transaction.UserTransaction;
import javax.naming.InitialContext;

public class CompleteOrderThread implements Runnable {

    public CompleteOrderThread(Integer id, boolean twoPhase) {
        orderID = id;
        this.twoPhase = twoPhase;
    }

    // Isolated: final/initialization semantics are not preserved for this field.
    Integer orderID;

    boolean twoPhase;

    // TODO(annotation: Override)
    public void run() {
            TradeServices trade;
            UserTransaction ut = null;
            
            try {
                // TODO: Sometimes, rarely, the commit does not complete before the find in completeOrder (leads to null order)
                // Adding delay here for now, will try to find a better solution in the future.
                Thread.sleep(500);
                
                InitialContext context = new InitialContext();
                ut = (UserTransaction) context.lookup("java:comp/UserTransaction");
                
                ut.begin();
                
                if (TradeConfig.getRunTimeMode() == TradeConfig.EJB3) {
                    trade = (TradeSLSBBean) context.lookup("java:module/TradeSLSBBean");
                } else {
                    trade = new TradeDirect(); 
                }
                
                trade.completeOrder(orderID, twoPhase);
                
                ut.commit();
            } catch (Exception e) {
                
                try {
                    ut.rollback();
                } catch (Exception e1) {
                    throw new EJBException(e1);
                } 
                throw new EJBException(e);
            } 
    }
}

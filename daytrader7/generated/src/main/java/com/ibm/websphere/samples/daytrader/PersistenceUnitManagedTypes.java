package com.ibm.websphere.samples.daytrader;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.persistenceunit.PersistenceManagedTypes;

// The legacy persistence unit listed its classes and excluded all others (5 managed here).
@Configuration
public class PersistenceUnitManagedTypes {

    @Bean
    PersistenceManagedTypes persistenceManagedTypes() {
        return PersistenceManagedTypes.of(
                "com.ibm.websphere.samples.daytrader.entities.AccountDataBean",
                "com.ibm.websphere.samples.daytrader.entities.AccountProfileDataBean",
                "com.ibm.websphere.samples.daytrader.entities.HoldingDataBean",
                "com.ibm.websphere.samples.daytrader.entities.OrderDataBean",
                "com.ibm.websphere.samples.daytrader.entities.QuoteDataBean"
        );
    }
}

package com.ibm.websphere.samples.daytrader.ejb3;

import com.ibm.websphere.samples.daytrader.util.Log;
import com.ibm.websphere.samples.daytrader.util.MDBStats;
import com.ibm.websphere.samples.daytrader.util.TimerStat;
import com.ibm.websphere.samples.daytrader.util.TradeConfig;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;

// TODO(field: ejb_context_owner_not_managed: mdc)
// TODO(field: container_api_type_not_emitted: cdi_event_field_escapes)
// TODO(message_listener: mdb_topic_listener_not_built)
public class DTStreamer3MDB implements MessageListener {

    public DTStreamer3MDB() {
        if (Log.doTrace()) {
            Log.trace("DTStreamer3MDB:DTStreamer3MDB()");
        }
        if (statInterval <= 0) {
            statInterval = 10000;
        }
        mdbStats = MDBStats.getInstance();
    }

    // Isolated: final/initialization semantics are not preserved for this field.
    private MDBStats mdbStats;

    private int statInterval = 10000;

    // TODO(body: retention_calls_stubbed_member:jmsEvent)
    // TODO(annotation: Override)
    public void onMessage(Message message) {
        throw new provenant.engine.EngineStubException("body not extracted for onMessage — see lower::lir's module docs");
    }
}

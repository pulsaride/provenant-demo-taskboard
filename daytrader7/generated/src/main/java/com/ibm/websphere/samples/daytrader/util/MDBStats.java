package com.ibm.websphere.samples.daytrader.util;

public class MDBStats extends java.util.HashMap<String, TimerStat> {

    private MDBStats() {
    }

    private static final long serialVersionUID = -3759835921094193760L;

    private static MDBStats mdbStats = null;

    public static synchronized MDBStats getInstance() {
        if (mdbStats == null) {
            mdbStats = new MDBStats();
        }
        return mdbStats;
    }

    public TimerStat addTiming(String type, long sendTime, long recvTime) {
        TimerStat stats = null;
        synchronized (type) {

            stats = get(type);
            if (stats == null) {
                stats = new TimerStat();
            }

            long time = recvTime - sendTime;
            if (time > stats.getMax()) {
                stats.setMax(time);
            }
            if (time < stats.getMin()) {
                stats.setMin(time);
            }
            stats.setCount(stats.getCount() + 1);
            stats.setTotalTime(stats.getTotalTime() + time);

            put(type, stats);
        }
        return stats;
    }

    public synchronized void reset() {
        clear();
    }
}

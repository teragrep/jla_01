package com.teragrep.jla_01.syslog;

import com.teragrep.rlo_14.SDElement;
import com.teragrep.rlo_14.SyslogMessage;
import java.util.UUID;

public final class SyslogRecordEventID implements SyslogRecord {

    private final SyslogRecord syslogRecord;
    private final String hostname;


    public SyslogRecordEventID(SyslogRecord syslogRecord, String hostname) {
        this.syslogRecord = syslogRecord;
        this.hostname = hostname;
    }

    @Override
    public SyslogMessage getRecord() {
        final SDElement eventIdSDE = new SDElement("event_id@48577");

        eventIdSDE.addSDParam("hostname", hostname);

        String uuid = UUID.randomUUID().toString();
        eventIdSDE.addSDParam("uuid", uuid);
        eventIdSDE.addSDParam("source", "source");

        long unixtime = System.currentTimeMillis();
        String epochtime = Long.toString(unixtime);
        eventIdSDE.addSDParam("unixtime", epochtime);

        return syslogRecord.getRecord().withSDElement(eventIdSDE);
    }
}

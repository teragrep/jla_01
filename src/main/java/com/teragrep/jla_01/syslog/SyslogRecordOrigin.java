package com.teragrep.jla_01.syslog;

import com.teragrep.rlo_14.SDElement;
import com.teragrep.rlo_14.SyslogMessage;

public class SyslogRecordOrigin implements SyslogRecord {

    private final SyslogRecord syslogRecord;
    private final String hostname;
    public SyslogRecordOrigin(SyslogRecord syslogRecord, String hostname) {
        this.syslogRecord = syslogRecord;
        this.hostname = hostname;
    }

    @Override
    public SyslogMessage getRecord() {
        final SDElement origin = new SDElement("origin@48577");
        origin.addSDParam("hostname", hostname);

        return syslogRecord.getRecord().withSDElement(origin);
    }

}

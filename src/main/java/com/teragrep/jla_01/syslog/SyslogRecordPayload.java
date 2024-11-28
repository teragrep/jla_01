package com.teragrep.jla_01.syslog;

import com.teragrep.rlo_14.SyslogMessage;

public class SyslogRecordPayload implements SyslogRecord {

    private final SyslogRecord syslogRecord;
    private final String payload;
    public SyslogRecordPayload(SyslogRecord syslogRecord, String payload) {
        this.syslogRecord = syslogRecord;
        this.payload = payload;
    }

    @Override
    public SyslogMessage getRecord() {
        SyslogMessage syslogMessage = syslogRecord.getRecord();
        syslogMessage.withMsg(payload);
        return syslogMessage;
    }
}

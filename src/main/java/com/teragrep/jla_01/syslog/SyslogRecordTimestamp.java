package com.teragrep.jla_01.syslog;

import com.teragrep.rlo_14.SyslogMessage;

import java.time.Instant;

public final class SyslogRecordTimestamp implements SyslogRecord {

    private final SyslogRecord syslogRecord;
    private final Instant timestamp;

    public SyslogRecordTimestamp(SyslogRecord syslogRecord) {
        this(syslogRecord, Instant.now());
    }

    public SyslogRecordTimestamp(SyslogRecord syslogRecord, Instant timestamp) {
        this.syslogRecord = syslogRecord;
        this.timestamp = timestamp;
    }

    @Override
    public SyslogMessage getRecord() {
        return syslogRecord.getRecord().withTimestamp(timestamp);
    }
}

package com.teragrep.jla_01.syslog;

import com.teragrep.rlo_14.Facility;
import com.teragrep.rlo_14.Severity;
import com.teragrep.rlo_14.SyslogMessage;

public class SyslogRecordConfigured implements SyslogRecord {

    private final String hostname;
    private final String appName;
    private final Severity severity;
    private final Facility facility;

    public SyslogRecordConfigured(String hostname, String appName) {
        this(hostname, appName, Severity.WARNING, Facility.USER);
    }

    public SyslogRecordConfigured(String hostname, String appName, Severity severity, Facility facility) {
        this.hostname = hostname;
        this.appName = appName;
        this.severity = severity;
        this.facility = facility;
    }

    @Override
    public SyslogMessage getRecord() {
        SyslogMessage syslogMessage = new SyslogMessage();
        syslogMessage = syslogMessage.withFacility(facility);
        syslogMessage = syslogMessage.withSeverity(severity);
        syslogMessage = syslogMessage.withHostname(hostname);
        syslogMessage = syslogMessage.withAppName(appName);

        return syslogMessage;
    }
}

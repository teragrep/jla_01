package com.teragrep.jla_01.syslog;

import com.teragrep.rlo_14.SyslogMessage;

public interface SyslogRecord {
    SyslogMessage getRecord();
}

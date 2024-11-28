package com.teragrep.jla_01.syslog;

import com.teragrep.rlo_14.SDElement;
import com.teragrep.rlo_14.SyslogMessage;

import java.util.Map;

public class SyslogRecordMDC implements SyslogRecord {

    private final SyslogRecord syslogRecord;
    private final Map<String, String> mdc;

    public SyslogRecordMDC(SyslogRecord syslogRecord, Map<String, String> mdc) {
        this.syslogRecord = syslogRecord;
        this.mdc = mdc;
    }

    @Override
    public SyslogMessage getRecord() {
        final SDElement mdcElement = new SDElement("jla_01_mdc@48577");
        mdc.forEach(mdcElement::addSDParam);
        return syslogRecord.getRecord().withSDElement(mdcElement);
    }
}

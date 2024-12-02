/*
   Reliable Event Logging Protocol (RELP) Logback plugin
   Copyright (C) 2021-2024  Suomen Kanuuna Oy

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at
   http://www.apache.org/licenses/LICENSE-2.0
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.teragrep.jla_01.syslog;

import com.teragrep.rlo_14.Facility;
import com.teragrep.rlo_14.Severity;
import com.teragrep.rlo_14.SyslogMessage;

public final class SyslogRecordConfigured implements SyslogRecord {

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

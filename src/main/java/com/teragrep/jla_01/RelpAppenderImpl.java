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
package com.teragrep.jla_01;

import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import com.teragrep.jla_01.syslog.*;
import com.teragrep.rlp_01.client.IManagedRelpConnection;
import com.teragrep.rlp_01.pool.Pool;

import java.nio.charset.StandardCharsets;

public final class RelpAppenderImpl<E> implements RelpAppender<E> {

    private final Pool<IManagedRelpConnection> relpConnectionPool;
    private final String hostname;
    private final String appName;
    private final String originalHostname;
    private final boolean enableEventId48577;
    private final LayoutWrappingEncoder<E> encoder;

    public RelpAppenderImpl(Pool<IManagedRelpConnection> relpConnectionPool, String hostname, String appName, String originalHostname, boolean enableEventId48577, LayoutWrappingEncoder<E> encoder) {
        this.relpConnectionPool = relpConnectionPool;
        this.hostname = hostname;
        this.appName = appName;
        this.originalHostname = originalHostname;
        this.enableEventId48577 = enableEventId48577;
        this.encoder = encoder;
    }

    @Override
    public void append(E iLoggingEvent) {
        {
            SyslogRecord syslogRecord = new SyslogRecordConfigured(hostname, appName);
            syslogRecord = new SyslogRecordTimestamp(syslogRecord);
            syslogRecord = new SyslogRecordOrigin(syslogRecord, originalHostname);
            if (enableEventId48577) {
                syslogRecord = new SyslogRecordEventID(syslogRecord, originalHostname);
            }

            //syslogRecord = new SyslogRecordMDC(syslogRecord, new HashMap<>());

            String payload = encoder.getLayout().doLayout(iLoggingEvent);
            syslogRecord = new SyslogRecordPayload(syslogRecord, payload);

            IManagedRelpConnection connection = relpConnectionPool.get();

            connection.ensureSent(syslogRecord.getRecord().toRfc5424SyslogMessage().getBytes(StandardCharsets.UTF_8));
            relpConnectionPool.offer(connection);
        }
    }

    @Override
    public void stop() {
        relpConnectionPool.close();
    }

    @Override
    public boolean isStub() {
        return false;
    }
}

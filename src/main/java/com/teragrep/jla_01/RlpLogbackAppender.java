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

import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import com.teragrep.jla_01.syslog.*;
import com.teragrep.jla_01.syslog.hostname.Hostname;
import com.teragrep.rlp_01.client.*;
import com.teragrep.rlp_01.pool.Pool;
import com.teragrep.rlp_01.pool.UnboundPool;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

/**
 * Unsynchronized version of RELP Appender for Logback. It is configured in a Bean manner using setters by Logback.
 * @param <E>
 */
public final class RlpLogbackAppender<E> extends UnsynchronizedAppenderBase<E> implements IRelpAppenderConfig<E> {

    private LayoutWrappingEncoder<E> encoder;

    private String originalHostname;
    private int relpPort;
    private Boolean enableEventId48577;
    private String relpHostAddress;
    private String appName;
    private String hostname;
    private int connectionTimeout;
    private int writeTimeout;
    private int readTimeout;
    private int reconnectInterval;
    private boolean keepAliveEnabled;
    private int reconnectIfNoMessagesInterval;
    private boolean connectOnStart;
    private boolean rebindEnabled;
    private int rebindAmount;
    private boolean synchronizedAccess;


    private boolean useTls;
    private String keystorePath;
    private String keystorePassword;
    private String tlsProtocol;

    private Pool<IManagedRelpConnection> relpConnectionPool;

    private final Lock appendLock = new ReentrantLock();

    private final Lock beanLock = new ReentrantLock();

    private final AtomicBoolean started = new AtomicBoolean();

    public RlpLogbackAppender() {
        // just defaults here

        encoder = new LayoutWrappingEncoder<>();

        originalHostname = "";
        relpPort = 601;
        enableEventId48577 = true;
        relpHostAddress = "127.0.0.1";
        appName = "jla-01";
        hostname = "localhost.localdomain";
        connectionTimeout = 2500;
        writeTimeout = 1500;
        readTimeout = 1500;
        reconnectInterval = 500;
        keepAliveEnabled = true;
        reconnectIfNoMessagesInterval = 150000;
        connectOnStart = false;
        rebindEnabled = false;
        rebindAmount = 100000;
        synchronizedAccess = false;

        useTls = false;
        keystorePath = "/unset/path/to/keystore";
        keystorePassword = "";
        tlsProtocol = "TLSv1.3";

        relpConnectionPool = new UnboundPool<>(new Supplier<IManagedRelpConnection>() {
            @Override
            public IManagedRelpConnection get() {
                return new ManagedRelpConnectionStub();
            }
        }, new ManagedRelpConnectionStub());

        started.set(false);
    }

    @Override
    public void setEncoder(LayoutWrappingEncoder<E> encoder) {
        beanLock.lock();
        try {
            this.encoder = encoder;
        }
        finally {
            beanLock.unlock();
        }
    }

    @Override
    public void setRelpPort(int relpPort) {
        beanLock.lock();
        try {
            this.relpPort = relpPort;
        }
        finally {
            beanLock.lock();
        }
    }

    @Override
    public void setEnableEventId48577(Boolean enableEventId48577) {
        beanLock.lock();
        try {
            this.enableEventId48577 = enableEventId48577;
        }
        finally {
            beanLock.lock();
        }
    }

    @Override
    public void setRelpHostAddress(String relpHostAddress) {
        beanLock.lock();
        try {
            this.relpHostAddress = relpHostAddress;
        }
        finally {
            beanLock.unlock();
        }
    }

    @Override
    public void setAppName(String appName) {
        beanLock.lock();
        try {
            this.appName = appName;
        }
        finally {
            beanLock.unlock();
        }
    }

    @Override
    public void setHostname(String hostname) {
        beanLock.lock();
        try {
            this.hostname = hostname;
        }
        finally {
            beanLock.lock();
        }
    }

    @Override
    public void setConnectionTimeout(int timeout) {
        beanLock.lock();
        try {
            this.connectionTimeout = timeout;
        }
        finally {
            beanLock.lock();
        }
    }

    @Override
    public void setWriteTimeout(int timeout) {
        beanLock.lock();
        try {
            this.writeTimeout = timeout;
        }
        finally {
            beanLock.lock();
        }
    }

    @Override
    public void setReadTimeout(int timeout) {
        beanLock.lock();
        try {
            this.readTimeout = timeout;
        }
        finally {
            beanLock.unlock();
        }
    }

    @Override
    public void setReconnectInterval(int interval) {
        beanLock.lock();
        try {
            this.reconnectInterval = interval;
        }
        finally {
            beanLock.unlock();
        }
    }

    @Override
    public void setKeepAlive(boolean on) {
        beanLock.lock();
        try {
            this.keepAliveEnabled = on;
        }
        finally {
            beanLock.unlock();
        }
    }

    @Override
    public void setReconnectIfNoMessagesInterval(int interval) {
        beanLock.lock();
        try {
            this.reconnectIfNoMessagesInterval = interval;
        }
        finally {
            beanLock.unlock();
        }
    }

    @Override
    public void setUseTLS(boolean on) {
        beanLock.lock();
        try {
            this.useTls = on;
        }
        finally {
            beanLock.unlock();
        }
    }

    @Override
    public void setKeystorePath(String keystorePath) {
        beanLock.lock();
        try {
            this.keystorePath = keystorePath;
        }
        finally {
            beanLock.unlock();
        }
    }

    @Override
    public void setKeystorePassword(String keystorePassword) {
        beanLock.lock();
        try {
            this.keystorePassword = keystorePassword;
        }
        finally {
            beanLock.unlock();
        }
    }

    @Override
    public void setTlsProtocol(String tlsProtocol) {
        beanLock.lock();
        try {
            this.tlsProtocol = tlsProtocol;
        }
        finally {
            beanLock.unlock();
        }
    }

    @Override
    public void setConnectOnStart(boolean connectOnStart) {
        beanLock.lock();
        try {
            this.connectOnStart = connectOnStart;
        }
        finally {
            beanLock.unlock();
        }
    }

    @Override
    public void setRebindEnabled(boolean rebindEnabled) {
        beanLock.lock();
        try {
            this.rebindEnabled = rebindEnabled;
        }
        finally {
            beanLock.unlock();
        }
    }

    @Override
    public void setRebindAmount(int rebindAmount) {
        beanLock.lock();
        try {
            this.rebindAmount = rebindAmount;
        }
        finally {
            beanLock.unlock();
        }
    }

    @Override
    public void setSynchronizedAccess(boolean synchronizedAccess) {
        beanLock.lock();
        try {
            this.synchronizedAccess = synchronizedAccess;
        }
        finally {
            beanLock.unlock();
        }
    }

    @Override
    public void start() {
        beanLock.lock();
        try {
            originalHostname = new Hostname("").hostname();

            boolean maxIdleEnabled = reconnectIfNoMessagesInterval > 0;

            final RelpConfig relpConfig = new RelpConfig(relpHostAddress, relpPort, reconnectInterval, rebindAmount, rebindEnabled, Duration.ofMillis(reconnectIfNoMessagesInterval), maxIdleEnabled);

            final SocketConfig socketConfig = new SocketConfigImpl(readTimeout, writeTimeout, connectionTimeout, keepAliveEnabled);

            final SSLContextSupplier sslContextSupplier;
            if (useTls) {
                sslContextSupplier = new SSLContextSupplierKeystore(keystorePath, keystorePassword, tlsProtocol);
            } else {
                sslContextSupplier = new SSLContextSupplierStub();
            }

            RelpConnectionFactory relpConnectionFactory = new RelpConnectionFactory(relpConfig, socketConfig, sslContextSupplier);

            relpConnectionPool = new UnboundPool<>(relpConnectionFactory, new ManagedRelpConnectionStub());

            if (connectOnStart) {
                IManagedRelpConnection managedRelpConnection = relpConnectionPool.get();
                relpConnectionPool.offer(managedRelpConnection);
            }

            started.set(true);
        }
        finally {
            beanLock.unlock();
        }
        super.start();
    }

    @Override
    public void stop() {
        relpConnectionPool.close();
        super.stop();
    }

    @Override
    protected void append(E iLoggingEvent) {

        if (!started.get()) {
            throw new IllegalStateException("Appender has not been started");
        }

        if (synchronizedAccess) {
            appendLock.lock();
            try {
                internalAppend(iLoggingEvent);
            }
            finally {
                appendLock.unlock();
            }
        }
        else {
            internalAppend(iLoggingEvent);
        }
    }

    private void internalAppend(E iLoggingEvent) {
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
}

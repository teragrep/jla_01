package com.teragrep.jla_01;

import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import com.teragrep.jla_01.syslog.*;
import com.teragrep.jla_01.syslog.hostname.Hostname;
import com.teragrep.rlp_01.RelpConnection;
import com.teragrep.rlp_01.client.*;
import com.teragrep.rlp_01.pool.Pool;
import com.teragrep.rlp_01.pool.UnboundPool;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
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

    private boolean useTls;
    private String keystorePath;
    private String keystorePassword;
    private String tlsProtocol;

    private Pool<IManagedRelpConnection> relpConnectionPool;

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
    }

    @Override
    public void setEncoder(LayoutWrappingEncoder<E> encoder) {
        this.encoder = encoder;
    }

    @Override
    public void setRelpPort(int relpPort) {
        this.relpPort = relpPort;
    }

    @Override
    public void setEnableEventId48577(Boolean enableEventId48577) {
        this.enableEventId48577 = enableEventId48577;
    }

    @Override
    public void setRelpHostAddress(String relpHostAddress) {
        this.relpHostAddress = relpHostAddress;
    }

    @Override
    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Override
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    @Override
    public void setConnectionTimeout(int timeout) {
        this.connectionTimeout = timeout;
    }

    @Override
    public void setWriteTimeout(int timeout) {
        this.writeTimeout = timeout;
    }

    @Override
    public void setReadTimeout(int timeout) {
        this.readTimeout = timeout;
    }

    @Override
    public void setReconnectInterval(int interval) {
        this.reconnectInterval = interval;
    }

    @Override
    public void setKeepAlive(boolean on) {
        this.keepAliveEnabled = on;
    }

    @Override
    public void setReconnectIfNoMessagesInterval(int interval) {
        this.reconnectIfNoMessagesInterval = interval;
    }

    @Override
    public void setUseTLS(boolean on) {
        this.useTls = on;
    }

    @Override
    public void setKeystorePath(String keystorePath) {
        this.keystorePath = keystorePath;
    }

    @Override
    public void setKeystorePassword(String keystorePassword) {
        this.keystorePassword = keystorePassword;
    }

    @Override
    public void setTlsProtocol(String tlsProtocol) {
        this.tlsProtocol = tlsProtocol;
    }

    @Override
    public void start() {

        originalHostname = new Hostname("").hostname();
        // TODO add rebind, TODO set maxIdle based on reconnectIfNoMessagesInterval is 0

        boolean maxIdleEnabled = reconnectIfNoMessagesInterval > 0;

        final RelpConfig relpConfig = new RelpConfig(relpHostAddress, relpPort, reconnectInterval, 0,false, Duration.ofMillis(reconnectIfNoMessagesInterval), maxIdleEnabled);

        final SocketConfig socketConfig = new SocketConfigImpl(readTimeout, writeTimeout, connectionTimeout, keepAliveEnabled);

        final SSLContextSupplier sslContextSupplier;
        if (useTls) {
            sslContextSupplier = new SSLContextSupplierKeystore(keystorePath, keystorePassword, tlsProtocol);
        }
        else {
            sslContextSupplier = new SSLContextSupplierStub();
        }

        RelpConnectionFactory relpConnectionFactory = new RelpConnectionFactory(relpConfig, socketConfig, sslContextSupplier);

        relpConnectionPool = new UnboundPool<>(relpConnectionFactory, new ManagedRelpConnectionStub());

        boolean testConnection = false; // todo make configurable
        if (testConnection) {
            IManagedRelpConnection managedRelpConnection = relpConnectionPool.get();
            relpConnectionPool.offer(managedRelpConnection);
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

        SyslogRecord syslogRecord = new SyslogRecordConfigured(hostname, appName);
        syslogRecord = new SyslogRecordTimestamp(syslogRecord);
        syslogRecord = new SyslogRecordOrigin(syslogRecord, originalHostname);
        if (enableEventId48577) {
            syslogRecord = new SyslogRecordEventID(syslogRecord, originalHostname);
        }

        //syslogRecord = new SyslogRecordMDC(syslogRecord, new HashMap<>());

        String payload = encoder.getLayout().doLayout(iLoggingEvent);
        syslogRecord = new SyslogRecordPayload(syslogRecord, payload);

        // TODO locking decorator to retain old synchronized use compatibility
        // TODO full implementation
        IManagedRelpConnection connection = relpConnectionPool.get();

        connection.ensureSent(syslogRecord.getRecord().toRfc5424SyslogMessage().getBytes(StandardCharsets.UTF_8));
        relpConnectionPool.offer(connection);
    }
}

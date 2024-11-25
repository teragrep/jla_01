package com.teragrep.jla_01;

import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import com.teragrep.rlp_01.RelpConnection;

/**
 * Bean interface that reflects settings in logback.xml
 * @param <E>
 */
public interface IRelpAppenderConfig<E> {
    void setEncoder(LayoutWrappingEncoder encoder);

    void setSender(RelpConnection sender);

    void setRelpPort(int relpPort);

    void setEnableEventId48577(Boolean enableEventId48577);

    void setRelpHostAddress(String relpHostAddress);

    void setAppName(String appName);

    void setHostname(String hostname);

    // set connectionTimeout in seconds
    void setConnectionTimeout(int timeout);

    void setWriteTimeout(int timeout);

    void setReadTimeout(int timeout);

    //set reconnectInterval in milliseconds
    void setReconnectInterval(int interval);

    void setKeepAlive(boolean on);

    void setReconnectIfNoMessagesInterval(int interval);

    // tls
    void setUseTLS(boolean on);

    void setKeystorePath(String keystorePath);

    void setKeystorePassword(String keystorePassword);

    void setTlsProtocol(String tlsProtocol);
}

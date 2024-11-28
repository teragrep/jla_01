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
import com.teragrep.rlp_01.RelpConnection;

/**
 * Bean interface that reflects settings in logback.xml
 * @param <E>
 */
public interface IRelpAppenderConfig<E> {
    void setEncoder(LayoutWrappingEncoder<E> encoder);

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

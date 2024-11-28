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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import com.teragrep.rlo_14.SDElement;
import com.teragrep.rlo_14.SyslogMessage;

import com.teragrep.rlp_01.RelpBatch;
import ch.qos.logback.core.AppenderBase;
import com.teragrep.rlp_01.RelpConnection;
import com.teragrep.rlp_01.SSLContextFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

public class RlpLogbackAppender<E> extends AppenderBase<E> implements IRelpAppenderConfig<E> {

	// see also https://stackoverflow.com/questions/31415899/correct-way-to-stop-custom-logback-async-appender

	private RelpConnection sender;
	private LayoutWrappingEncoder<E> encoder;

	// settings for syslog messages
	private boolean enableEventId48577;
	private String hostname = "";
	private String appName = "";
	private String realHostname = "";

	// settings for relp window
	private String relpHostAddress = "localhost";
	private int relpPort = 1234;
	
	//
	//settings for timeouts, if they are 0 that we skip them
	//default are 0
	private int connectionTimeout = 0;
	private int readTimeout = 0;
	private int writeTimeout = 0;
	private int reconnectInterval = 500;
	private boolean keepAlive = true;
	private long reconnectIfNoMessagesInterval = 150000;
	private long lastMessageSent = 0;

	// tls
	private boolean useTLS = false;
	private String keystorePath = "";
	private String keystorePassword = "";
	private String tlsProtocol = "";


	@Override
	public void setEncoder(LayoutWrappingEncoder<E> encoder) {
		this.encoder = encoder;
	}

	@Override
	public void setSender(RelpConnection sender) {
		this.sender = sender;
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

	// set connectionTimeout in seconds
	@Override
	public void setConnectionTimeout(int timeout) {
		if (timeout > 0) {
			this.connectionTimeout = timeout;
		}
	}

	@Override
	public void setWriteTimeout(int timeout) {
		if (timeout > 0) {
			this.writeTimeout = timeout;
		}
	}

	@Override
	public void setReadTimeout(int timeout) {
		if (timeout > 0) {
			this.readTimeout = timeout;
		}
	}

	//set reconnectInterval in milliseconds
	@Override
	public void setReconnectInterval(int interval) {
		if (interval > 0) {
			this.reconnectInterval = interval;
		}
	}

	@Override
	public void setKeepAlive(boolean on) {
		this.keepAlive=on;
	}

	@Override
	public void setReconnectIfNoMessagesInterval(int interval) {
		this.reconnectIfNoMessagesInterval = interval;
	}

	// tls
	@Override
	public void setUseTLS(boolean on) {
		this.useTLS = on;
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

	private void connect() {
		if (System.getenv("JLA01_DEBUG") != null) {
			System.out.println("RlpLogbackAppender.connect>");
		}

		boolean notConnected = true;
		while (notConnected) {
			boolean connected = false;
			try {
				realHostname = java.net.InetAddress.getLocalHost().getHostName();
				connected = this.sender.connect(this.relpHostAddress, this.relpPort);
			} catch (Exception e) {
				System.out.println("RlpLogbackAppender.connect> reconnecting to relp server <["+relpHostAddress+"]> at port <["+relpPort+"]> after <[" + reconnectInterval + "]> milliseconds due to exception <" + e.getMessage() +">");
			}
			if (connected) {
				notConnected = false;
			} else {
				try {
					Thread.sleep(this.reconnectInterval);
				} catch (InterruptedException e) {
					System.out.println("RlpLogbackAppender.connect> reconnect timer interrupted, reconnecting now");
				}
			}
		}
	}

	private void tearDown() {
		if (System.getenv("JLA01_DEBUG") != null) {
			System.out.println("RlpLogbackAppender.tearDown>");
		}
		this.sender.tearDown();
	}

	private void disconnect() {
		if (System.getenv("JLA01_DEBUG") != null) {
			System.out.println("RlpLogbackAppender.disconnect>");
		}
		boolean disconnected = false;
		try {
			disconnected = this.sender.disconnect();
		} catch (IllegalStateException | IOException | TimeoutException e) {
			System.out.println("RlpLogbackAppender.disconnect> forcefully closing connection due to exception <" + e.getMessage() + ">");
		}
		finally {
			this.tearDown();
		}
		if (System.getenv("JLA01_DEBUG") != null) {
			System.out.println("RlpLogbackAppender.disconnect> disconnected: " + disconnected);
		}
	}

	@Override
	public void start() {
		if (started)
			return;

		// initialize events sender
		if (useTLS) {
			Supplier<SSLEngine> sslEngineSupplier = new Supplier<SSLEngine>() {
				private final SSLContext sslContext;
				{
					try {
						sslContext = SSLContextFactory.authenticatedContext(keystorePath, keystorePassword, tlsProtocol);
					} catch (GeneralSecurityException | IOException e) {
						throw new RuntimeException(e);
					}
				}

				@Override
				public SSLEngine get() {
					return sslContext.createSSLEngine();
				}
			};

			this.sender = new RelpConnection(sslEngineSupplier);
		}
		else {
			this.sender = new RelpConnection();
		}


		this.sender.setConnectionTimeout(connectionTimeout);
		this.sender.setReadTimeout(this.readTimeout);
		this.sender.setWriteTimeout(this.writeTimeout);
		this.sender.setKeepAlive(this.keepAlive);

		this.connect();
		super.start();
	}



	@Override
	public void stop() {
		if (System.getenv("JLA01_DEBUG") != null) {
			System.out.println("RlpLogbackAppender.stop>");
		}
		if (!started)
			return;

		this.disconnect();
		super.stop();
	}

	@Override
	protected void append(E eventObject) {
		if (System.getenv("JLA01_DEBUG") != null) {
			System.out.println("RlpLogbackAppender.append> entry");
		}
		String logMessage = encoder.getLayout().doLayout(eventObject);

		if (logMessage == null) {
			throw new IllegalArgumentException("layout not able to encode event to string");
		}

		final RelpBatch relpBatch = new RelpBatch();

		if (enableEventId48577) {
			final SDElement event_id_48577 = eventId48577();
			final SDElement origin_48577 = origin48577();
			SyslogMessage sl = LoggingEventConverter.getDefaultSyslogMessageWithSDElement(logMessage, appName, hostname,
					event_id_48577);
			sl.withSDElement(origin_48577);
			relpBatch.insert(sl.toRfc5424SyslogMessage().getBytes(StandardCharsets.UTF_8));
		} else {
			SyslogMessage sl = LoggingEventConverter.getDefaultSyslogMessage(logMessage, appName, hostname);
			relpBatch.insert(sl.toRfc5424SyslogMessage().getBytes(StandardCharsets.UTF_8));
		}

		boolean notSent = true;
		if (
				reconnectIfNoMessagesInterval > 0 &&
				System.currentTimeMillis() > (lastMessageSent + reconnectIfNoMessagesInterval)
		) {
			this.tearDown();
			this.connect();
		}
		while (notSent) {
			try {
				this.sender.commit(relpBatch);
			} catch (IllegalStateException | IOException | java.util.concurrent.TimeoutException e) {
				System.out.println("RlpLogbackAppender.append> will retry sending due to exception <" + e.getMessage() + ">");
			}

			if (!relpBatch.verifyTransactionAll()) {
				relpBatch.retryAllFailed();
				this.tearDown();
				this.connect();
			} else {
				notSent = false;
			}
		}
		lastMessageSent = System.currentTimeMillis();
		if (System.getenv("JLA01_DEBUG") != null) {
			System.out.println("RlpLogbackAppender.append> exit");
		}
	}


	private SDElement eventId48577() {
		final SDElement event_id_48577 = new SDElement("event_id@48577");
		if (realHostname != null) {
			event_id_48577.addSDParam("hostname", realHostname);
		}
                else {
			event_id_48577.addSDParam("hostname", "");
                }

		String uuid = UUID.randomUUID().toString();
		event_id_48577.addSDParam("uuid", uuid);
		event_id_48577.addSDParam("source", "source");

		long unixtime = System.currentTimeMillis();
		String epochtime = Long.toString(unixtime);
		event_id_48577.addSDParam("unixtime", epochtime);
		return event_id_48577;

	}


	private SDElement origin48577() {
		final SDElement origin_48577 = new SDElement("origin@48577");
		if (realHostname != null) {
			origin_48577.addSDParam("hostname", realHostname);
		}
		else {
			origin_48577.addSDParam("hostname", "");
		}

		return origin_48577;
	}
}

/*
   Reliable Event Logging Protocol (RELP) Logback plugin
   Copyright (C) 2021  Suomen Kanuuna Oy
  
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

import java.io.ByteArrayInputStream;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicLong;

import ch.qos.logback.classic.LoggerContext;
import com.teragrep.jla_01.server.TestServer;
import com.teragrep.jla_01.server.TestServerFactory;
import com.teragrep.rlo_06.RFC5424Frame;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RlpLogbackAppenderTest {

	@Test
	public void testRelpOnLocalHost() {
		Logger logger = LoggerFactory.getLogger("relp");
		logger.info("relp");
	}


	@Test()
	public void testDefaultSyslogMessage() throws Exception {
		TestServerFactory serverFactory = new TestServerFactory();

		final int serverPort = 22601;

		final ConcurrentLinkedDeque<byte[]> messageList = new ConcurrentLinkedDeque<>();
		AtomicLong openCount = new AtomicLong();
		AtomicLong closeCount = new AtomicLong();

		Assertions.assertDoesNotThrow(() -> {
					try (TestServer server = serverFactory.create(serverPort, messageList, openCount, closeCount)) {
						server.run();

						ILoggingEvent eventObject = new TestILoggingEvent();

						LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();


						PatternLayoutEncoder encoder = new PatternLayoutEncoder();
						encoder.setPattern("%-5level %logger{36} - %msg%n");
						encoder.setContext(loggerContext);
						encoder.start();


						RlpLogbackAppender<ILoggingEvent> relpAppender = new RlpLogbackAppender<>();
						relpAppender.setEncoder(encoder);
						relpAppender.setAppName("appName");
						relpAppender.setHostname("localhost");
						relpAppender.setRelpPort(serverPort);
						relpAppender.start();

						relpAppender.append(eventObject);
						relpAppender.stop();
					}

				}
		);

		Assertions.assertEquals(1, messageList.size(), "messageList size not expected");


		for (byte[] message : messageList) {
			RFC5424Frame rfc5424Frame = new RFC5424Frame();
			rfc5424Frame.load(new ByteArrayInputStream(message));

			Assertions.assertTrue(rfc5424Frame.next());

			Assertions.assertEquals("localhost", rfc5424Frame.hostname.toString());
			Assertions.assertEquals("appName", rfc5424Frame.appName.toString());
			Assertions.assertEquals("DEBUG logger - none\n", rfc5424Frame.msg.toString());
		}


		Assertions.assertTrue (openCount.get() >= 1, "openCount not expected");
		Assertions.assertEquals(1, closeCount.get(), "closeCount not expected");
	}



	/*
	@Test()
	public void testDefaultSyslogMessageWithSDElement() {
		RelpConnection sender = mock(RelpConnection.class);
		try (MockedStatic<RelpConnectionInstance> rlpClazz = Mockito.mockStatic(RelpConnectionInstance.class)) {
			rlpClazz.when(RelpConnectionInstance::getRelpConnection).thenReturn(sender);
			RlpLogbackAppender<ILoggingEvent> adapter = new RlpLogbackAppender<ILoggingEvent>();
			adapter.setAppName("appName");
			adapter.setEnableEventId48577(Boolean.TRUE);
			adapter.start();

			TestILoggingEvent eventObject = new TestILoggingEvent();

			PatternLayoutEncoder encoder = mock(PatternLayoutEncoder.class);
			Layout layout = mock(Layout.class);
			when(encoder.getLayout()).thenReturn(layout);
			when(layout.doLayout(any())).thenReturn("message");
			adapter.setEncoder(encoder);

			RelpWindow relpWindow = mock(RelpWindow.class);
			when(sender.begin()).thenReturn(relpWindow);

			adapter.append(eventObject);
			verify(relpWindow, times(1)).insert(any());
			try {
				verify(sender).commit(relpWindow);
			} catch (TimeoutException e) {
				e.printStackTrace();
			}

		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Test()
	public void testStopSyncAdapter() {
		RelpConnection sender = mock(RelpConnection.class);
		try (MockedStatic<RelpConnectionInstance> rlpClazz = Mockito.mockStatic(RelpConnectionInstance.class)) {
			rlpClazz.when(RelpConnectionInstance::getRelpConnection).thenReturn(sender);
			RlpLogbackAppender<ILoggingEvent> adapter = new RlpLogbackAppender<ILoggingEvent>();
			adapter.setAppName("appName");
			adapter.setEnableEventId48577(Boolean.TRUE);
			adapter.start();

			RelpWindow relpWindow = mock(RelpWindow.class);
			when(sender.begin()).thenReturn(relpWindow);
			adapter.stop();
			verify(relpWindow, times(0)).insert(any());
			try {
				verify(sender).disconnect();
			} catch (TimeoutException e) {
				e.printStackTrace();
			}

		} catch (IllegalStateException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	 */

}

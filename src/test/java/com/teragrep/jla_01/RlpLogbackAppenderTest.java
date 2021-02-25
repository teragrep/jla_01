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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.junit.Ignore;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.teragrep.jla_01.RlpLogbackAppender;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Layout;
import com.teragrep.rlp_01.RelpConnection;
import com.teragrep.rlp_01.RelpWindow;

public class RlpLogbackAppenderTest {

	@Test
	@Ignore
	public void testRelpOnLocalHost() {
		Logger logger = LoggerFactory.getLogger("relp");
		logger.info("relp");
	}

	/*
	@Test()
	public void testDefaultSyslogMessage() throws IllegalStateException, IOException {
		RelpConnection sender = mock(RelpConnection.class);
		try (MockedStatic<RelpConnectionInstance> rlpClazz = Mockito.mockStatic(RelpConnectionInstance.class)) {
			rlpClazz.when(RelpConnectionInstance::getRelpConnection).thenReturn(sender);
			RlpLogbackAppender<ILoggingEvent> adapter = new RlpLogbackAppender<ILoggingEvent>();
			adapter.setAppName("appName");
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
		}

	}

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

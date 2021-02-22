/*
 * Reliable Event Logging Protocol (RELP) Logback plugin
 * Copyright (C) 2021  Suomen Kanuuna Oy
 *  
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *  
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *  
 *  
 * Additional permission under GNU Affero General Public License version 3
 * section 7
 *  
 * If you modify this Program, or any covered work, by linking or combining it 
 * with other code, such other code is not for that reason alone subject to any
 * of the requirements of the GNU Affero GPL version 3 as long as this Program
 * is the same Program as licensed from Suomen Kanuuna Oy without any additional
 * modifications.
 *  
 * Supplemented terms under GNU Affero General Public License version 3
 * section 7
 *  
 * Origin of the software must be attributed to Suomen Kanuuna Oy. Any modified
 * versions must be marked as "Modified version of" The Program.
 *  
 * Names of the licensors and authors may not be used for publicity purposes.
 *  
 * No rights are granted for use of trade names, trademarks, or service marks
 * which are in The Program if any.
 *  
 * Licensee must indemnify licensors and authors for any liability that these
 * contractual assumptions impose on licensors and authors.
 *  
 * To the extent this program is licensed as part of the Commercial versions of
 * Teragrep, the applicable Commercial License may apply to this file if you as
 * a licensee so wish it.
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

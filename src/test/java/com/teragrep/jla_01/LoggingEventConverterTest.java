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

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Marker;

import com.cloudbees.syslog.SyslogMessage;
import com.teragrep.jla_01.LoggingEventConverter;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.LoggerContextVO;

public class LoggingEventConverterTest {

	@Test
    public void testgetDefaultSyslogMessage() {
		ILoggingEvent loggingEventTest =  new TestILoggingEvent(); 
		Map<String, String> mdc = new HashMap<String, String>();
		loggingEventTest.getMDCPropertyMap().put("ack_id", "100");
		SyslogMessage sm = LoggingEventConverter.getDefaultSyslogMessage(
				"log event",
				"",
				"");
		System.out.println(sm.toRfc5424SyslogMessage());
		assertNotNull(sm);
        
    }
	
	
	@Test
	public void testEvent_id_48577() {

		ILoggingEvent loggingEventTest = new TestILoggingEvent(); 
		
		//[	event_id@48577
		// 	hostname=${current.hostname} 
		//	uuid=${generated.uuid} 
		//	unixtime=${epochtime} 
		//	id_source="source"] 
		//	structured element with a boolean configurable 
		//	that says enableEventId48577=true/false from logback.xml
		
		Map<String, String> parameters = new LinkedHashMap<String, String>();
		parameters.put("hostname", "hostname");
		parameters.put("uuid", "uuid");
		
		LocalDate ld = LocalDate.parse("2020-01-02", DateTimeFormatter.ISO_DATE);
		long unixtime = ld.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000;
		// long unixtime = Instant.now().toEpochMilli();
		parameters.put("unixtime", Long.toString(unixtime));
		parameters.put("source", "source");
		
		SyslogMessage sm = LoggingEventConverter.getDefaultSyslogMessage(
				"log event",
				"",
				"");
		LoggingEventConverter.addSDElement(sm, "event_id@48577", parameters);

		String expected = "   - - [event_id@48577 hostname=\"hostname\" uuid=\"uuid\" unixtime=\"1577916000000\" source=\"source\"] log event";
		assertEquals(expected, sm.toRfc5424SyslogMessage().substring(30));
		System.out.println(sm.toRfc5424SyslogMessage());
	}
	

	@Test
	public void testEvent48577() {

		ILoggingEvent loggingEventTest = new TestILoggingEvent(); 
		
		//[	event_id@48577
		// 	hostname=${current.hostname} 
		//	uuid=${generated.uuid} 
		//	unixtime=${epochtime} 
		//	id_source="source"] 
		//	structured element with a boolean configurable 
		//	that says enableEventId48577=true/false from logback.xml
			
		
		
		Map<String, String> parameters = new LinkedHashMap<String, String>();
		parameters.put("hostname", "hostname");
		parameters.put("uuid", "uuid");
		
		LocalDate ld = LocalDate.parse("2020-01-02", DateTimeFormatter.ISO_DATE);
		long unixtime = ld.atStartOfDay(ZoneId.systemDefault()).toEpochSecond() * 1000;
		// long unixtime = Instant.now().toEpochMilli();
		parameters.put("unixtime", Long.toString(unixtime));
		parameters.put("source", "source");
		
		SyslogMessage sm = LoggingEventConverter.getDefaultSyslogMessage(
				"log event",
				"",
				"");
		LoggingEventConverter.addSDElement(sm, "event_id@48577", parameters);

		String expected = "   - - [event_id@48577 hostname=\"hostname\" uuid=\"uuid\" unixtime=\"1577916000000\" source=\"source\"] log event";
		assertEquals(expected, sm.toRfc5424SyslogMessage().substring(30));
		System.out.println(sm.toRfc5424SyslogMessage());
	}
}

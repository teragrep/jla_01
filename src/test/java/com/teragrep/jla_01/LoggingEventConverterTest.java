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

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import com.teragrep.rlo_14.SyslogMessage;
import ch.qos.logback.classic.spi.ILoggingEvent;

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

		String expected = "   - - [event_id@48577 hostname=\"hostname\" uuid=\"uuid\" unixtime=\""+ unixtime +"\" source=\"source\"] log event";
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

		String expected = "   - - [event_id@48577 hostname=\"hostname\" uuid=\"uuid\" unixtime=\""+ unixtime +"\" source=\"source\"] log event";
		assertEquals(expected, sm.toRfc5424SyslogMessage().substring(30));
		System.out.println(sm.toRfc5424SyslogMessage());
	}
}

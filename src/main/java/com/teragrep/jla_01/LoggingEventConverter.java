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

import java.util.Date;
import java.util.Map;

import com.cloudbees.syslog.Facility;
import com.cloudbees.syslog.SDElement;
import com.cloudbees.syslog.Severity;
import com.cloudbees.syslog.SyslogMessage;

public class LoggingEventConverter {

	private LoggingEventConverter() {
	}

	public static SyslogMessage getDefaultSyslogMessageWithSDElement(String message, String appName, String hostname,
			SDElement metadata48577) {
		if (metadata48577 == null) {
			return getDefaultSyslogMessage(message, appName, hostname);
		}

		SyslogMessage syslog = new SyslogMessage().withTimestamp(new Date().getTime()).withSeverity(Severity.WARNING)
				.withAppName(appName)
				.withHostname(hostname)
				.withFacility(Facility.USER)
				.withSDElement(metadata48577)
				.withMsg(message);
		return syslog;
	}

	public static SyslogMessage getDefaultSyslogMessage(String message, String appName, String hostname) {
		SyslogMessage syslog = new SyslogMessage().withTimestamp(new Date().getTime()).withSeverity(Severity.WARNING)
				.withAppName(appName)
				.withHostname(hostname)
				.withFacility(Facility.USER).withMsg(message);
		return syslog;
	}

	public static SyslogMessage addSDElement(SyslogMessage syslog, String sdElementName,
			Map<String, String> parameters) {
		if (syslog == null) {
			return syslog;
		}

		SDElement metadataSDE = getStructuredDataParams(sdElementName, parameters);
		

		return syslog.withSDElement(metadataSDE);
	}

	/*
	 * Gets the data from the mdc and adds it to SD Element as SD Parameters.
	 */
	private static SDElement getStructuredDataParams(String name, Map<String, String> parameters) {
		SDElement metadataSDE = new SDElement(name == null ? "" : name);
		if (parameters == null) {
			return metadataSDE;
		}
		parameters.forEach((k, v) -> {
			metadataSDE.addSDParam(k, v);
		});
		return metadataSDE;
	}

}

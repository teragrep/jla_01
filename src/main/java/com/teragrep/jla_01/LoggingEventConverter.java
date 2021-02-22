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

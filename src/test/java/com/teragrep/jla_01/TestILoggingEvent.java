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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Marker;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.LoggerContextVO;

public class TestILoggingEvent implements ILoggingEvent {
	
	Map<String, String> mdc = new HashMap<String, String>();
	Object[] getArgumentArray;
	
	@Override
	public String getThreadName() {
		return "main";
	}

	@Override
	public Level getLevel() {
		return Level.DEBUG;
	}

	@Override
	public String getMessage() {
		return StringUtils.EMPTY;
	}

	@Override
	public Object[] getArgumentArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFormattedMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLoggerName() {
		return "logger";
	}

	@Override
	public LoggerContextVO getLoggerContextVO() {
		return null;
	}

	@Override
	public IThrowableProxy getThrowableProxy() {
		return null;
	}

	@Override
	public StackTraceElement[] getCallerData() {
		return null;
	}

	@Override
	public boolean hasCallerData() {
		return false;
	}

	@Override
	public Marker getMarker() {
		return null;
	}

	@Override
	public Map<String, String> getMDCPropertyMap() {
		
		return mdc;
	}

	@Override
	public Map<String, String> getMdc() {
		return mdc;
	}

	@Override
	public long getTimeStamp() {
		return 0;
	}

	@Override
	public void prepareForDeferredProcessing() {
	}
}

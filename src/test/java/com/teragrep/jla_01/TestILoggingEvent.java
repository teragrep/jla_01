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

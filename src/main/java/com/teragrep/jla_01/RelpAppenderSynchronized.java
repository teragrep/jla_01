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

public final class RelpAppenderSynchronized<E> implements RelpAppender<E> {

    private final RelpAppender<E> appender;

    public RelpAppenderSynchronized(RelpAppender<E> relpAppender) {
        this.appender = relpAppender;
    }

    @Override
    public synchronized void append(E iLoggingEvent) {
        appender.append(iLoggingEvent);
    }

    @Override
    public void stop() {
        appender.stop();
    }

    @Override
    public boolean isStub() {
        return false;
    }
}

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
package com.teragrep.jla_01.server;

import com.teragrep.rlp_03.frame.delegate.FrameContext;
import com.teragrep.rlp_03.frame.delegate.event.RelpEvent;
import com.teragrep.rlp_03.frame.delegate.event.RelpEventClose;

import java.util.concurrent.atomic.AtomicLong;

class RelpEventCloseCounting extends RelpEvent {
    private final AtomicLong closeCount;
    private final RelpEventClose relpEventClose;

    RelpEventCloseCounting(AtomicLong closeCount) {
        this.closeCount = closeCount;
        this.relpEventClose = new RelpEventClose();
    }

    @Override
    public void accept(FrameContext frameContext) {
        relpEventClose.accept(frameContext);
        closeCount.incrementAndGet();
    }
}

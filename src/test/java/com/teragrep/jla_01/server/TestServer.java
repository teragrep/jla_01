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

import com.teragrep.net_01.eventloop.EventLoop;
import com.teragrep.net_01.server.Server;
import org.junit.jupiter.api.Assertions;

import java.util.concurrent.ExecutorService;

public class TestServer implements Runnable, AutoCloseable {
    private final EventLoop eventLoop;
    private final Thread eventLoopThread;
    private final ExecutorService executorService;
    private final Server server;

    public TestServer(EventLoop eventLoop, Thread eventLoopThread, ExecutorService executorService, Server server) {
        this.eventLoop = eventLoop;
        this.eventLoopThread = eventLoopThread;
        this.executorService = executorService;
        this.server = server;
    }

    @Override
    public void close() throws Exception {
        eventLoop.stop();
        executorService.shutdown();
        eventLoopThread.join();
        server.close(); // closes port
    }

    @Override
    public void run() {
        eventLoopThread.start();
    }
}

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

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ch.qos.logback.classic.LoggerContext;
import com.teragrep.jla_01.server.TestServer;
import com.teragrep.jla_01.server.TestServerFactory;
import com.teragrep.rlo_06.RFC5424Frame;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RlpLogbackAppenderTest {

	@Test()
	public void testDefaultSyslogMessage() {
		TestServerFactory serverFactory = new TestServerFactory();

		final int serverPort = 22601;

		final String testPayload = "some payload";

		final ConcurrentLinkedDeque<byte[]> messageList = new ConcurrentLinkedDeque<>();
		AtomicLong openCount = new AtomicLong();
		AtomicLong closeCount = new AtomicLong();

		Assertions.assertDoesNotThrow(() -> {
					try (TestServer server = serverFactory.create(serverPort, messageList, openCount, closeCount)) {
						server.run();

						ILoggingEvent eventObject = new TestILoggingEvent(testPayload);

						LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();


						PatternLayoutEncoder encoder = new PatternLayoutEncoder();
						encoder.setPattern("%-5level %logger{36} - %msg%n");
						encoder.setContext(loggerContext);
						encoder.start();


						RlpLogbackAppender<ILoggingEvent> relpAppender = new RlpLogbackAppender<>();
						relpAppender.setEncoder(encoder);
						relpAppender.setAppName("appName");
						relpAppender.setHostname("localhost");
						relpAppender.setRelpPort(serverPort);
						relpAppender.start();

						relpAppender.append(eventObject);
						relpAppender.stop();
					}

				}
		);

		Assertions.assertEquals(1, messageList.size(), "messageList size not expected");


		for (byte[] message : messageList) {
			RFC5424Frame rfc5424Frame = new RFC5424Frame();
			rfc5424Frame.load(new ByteArrayInputStream(message));

			AtomicBoolean hasNext = new AtomicBoolean();
			Assertions.assertDoesNotThrow(() -> hasNext.set(rfc5424Frame.next()));
			Assertions.assertTrue(hasNext.get());

			Assertions.assertEquals("localhost", rfc5424Frame.hostname.toString());
			Assertions.assertEquals("appName", rfc5424Frame.appName.toString());

			Pattern timestampPattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}.\\d{3}Z");

			Assertions.assertTrue(timestampPattern.matcher(rfc5424Frame.timestamp.toString()).matches());

			Assertions.assertEquals("DEBUG logger - "+testPayload+"\n", rfc5424Frame.msg.toString());
		}


		Assertions.assertEquals (1, openCount.get(), "openCount not expected");
		Assertions.assertEquals(1, closeCount.get(), "closeCount not expected");
	}


	@Test
	public void testDefaultSyslogMessageWithSDElement() {
		TestServerFactory serverFactory = new TestServerFactory();

		final int serverPort = 22602;
		final String testPayload = "test string two";

		final ConcurrentLinkedDeque<byte[]> messageList = new ConcurrentLinkedDeque<>();
		AtomicLong openCount = new AtomicLong();
		AtomicLong closeCount = new AtomicLong();

		Assertions.assertDoesNotThrow(() -> {
					try (TestServer server = serverFactory.create(serverPort, messageList, openCount, closeCount)) {
						server.run();


						TestILoggingEvent eventObject = new TestILoggingEvent(testPayload);

						LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

						PatternLayoutEncoder encoder = new PatternLayoutEncoder();
						encoder.setPattern("%-5level %logger{36} - %msg%n");
						encoder.setContext(loggerContext);
						encoder.start();

						RlpLogbackAppender<ILoggingEvent> adapter = new RlpLogbackAppender<>();
						adapter.setEncoder(encoder);
						adapter.setHostname("host1");
						adapter.setAppName("appName");
						adapter.setRelpPort(serverPort);
						adapter.setEnableEventId48577(Boolean.TRUE);
						adapter.start();

						adapter.append(eventObject);

						adapter.stop();
					}
				}
		);
		Assertions.assertEquals(1, messageList.size(), "messageList size not expected");


		for (byte[] message : messageList) {
			RFC5424Frame rfc5424Frame = new RFC5424Frame();
			rfc5424Frame.load(new ByteArrayInputStream(message));

			AtomicBoolean frameNext = new AtomicBoolean();
			Assertions.assertDoesNotThrow( () -> {frameNext.set(rfc5424Frame.next());});
			Assertions.assertTrue(frameNext.get());

			Assertions.assertEquals("host1", rfc5424Frame.hostname.toString());
			Assertions.assertEquals("appName", rfc5424Frame.appName.toString());
			Assertions.assertEquals("DEBUG logger - "+testPayload+"\n", rfc5424Frame.msg.toString());
		}


		Assertions.assertEquals(1, openCount.get(), "openCount not expected");
		Assertions.assertEquals(1, closeCount.get(), "closeCount not expected");
	}

	@Test
	public void threadedTest() {
		TestServerFactory serverFactory = new TestServerFactory();

		final int serverPort = 22603;
		final int testCycles = 10_000;
		final String appName = "someApp";
		final String hostname = "someHost";

		final ConcurrentLinkedDeque<byte[]> messageList = new ConcurrentLinkedDeque<>();
		AtomicLong openCount = new AtomicLong();
		AtomicLong closeCount = new AtomicLong();

		Assertions.assertDoesNotThrow(() -> {
			try (TestServer server = serverFactory.create(serverPort, messageList, openCount, closeCount)) {
				server.run();


				LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();


				PatternLayoutEncoder encoder = new PatternLayoutEncoder();
				encoder.setPattern("%-5level %logger{36} - %msg%n");
				encoder.setContext(loggerContext);
				encoder.start();


				RlpLogbackAppender<ILoggingEvent> relpAppender = new RlpLogbackAppender<>();
				relpAppender.setEncoder(encoder);
				relpAppender.setAppName(appName);
				relpAppender.setHostname(hostname);
				relpAppender.setRelpPort(serverPort);
				relpAppender.start();

				CountDownLatch countDownLatch = new CountDownLatch(testCycles);

				for (int i = 0; i < testCycles; i++) {
					final String testString = "hey this is relp " + i;
					ForkJoinPool.commonPool().submit(() -> {

						ILoggingEvent eventObject = new TestILoggingEvent(testString);
						relpAppender.append(eventObject);

						countDownLatch.countDown();
					});
				}

				countDownLatch.await();
				relpAppender.stop();


			}
		});


		Assertions.assertEquals(testCycles, messageList.size(), "messageList size not expected");

		Pattern pattern = Pattern.compile("DEBUG logger - hey this is relp (\\d+)\n");

		Map<Integer, Boolean> testIterationsMap = new HashMap<>();
		for (int i = 0; i < testCycles; i++) {
			testIterationsMap.put(i,true);
		}

		for (byte[] message : messageList) {
			RFC5424Frame rfc5424Frame = new RFC5424Frame();
			rfc5424Frame.load(new ByteArrayInputStream(message));

			AtomicBoolean frameNext = new AtomicBoolean();
			Assertions.assertDoesNotThrow( () -> {frameNext.set(rfc5424Frame.next());});
			Assertions.assertTrue(frameNext.get());

			Assertions.assertEquals(hostname, rfc5424Frame.hostname.toString());
			Assertions.assertEquals(appName, rfc5424Frame.appName.toString());

			Matcher matcher = pattern.matcher(rfc5424Frame.msg.toString());
			boolean matches = matcher.matches();
			Assertions.assertTrue(matches, "payload unexpected");

			String testIterationValue = matcher.group(1);

			Assertions.assertDoesNotThrow( () -> {Integer.parseInt(testIterationValue);}, "extracted test iteration not integer");

			int testIteration = Integer.parseInt(testIterationValue);

			Boolean iterationValue = testIterationsMap.remove(testIteration);
			Assertions.assertNotNull(iterationValue);
			Assertions.assertTrue(iterationValue);
		}
		Assertions.assertTrue(testIterationsMap.isEmpty(), "testIterationsMap was not empty: some messages were not delivered successfully");

		Assertions.assertTrue( openCount.get() >= 1, "openCount not expected");
		Assertions.assertTrue(closeCount.get() >= 1, "closeCount not expected");
	}
}

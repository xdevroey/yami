package be.yami.web;

/*-
 * #%L
 * YAMI - Yet Another Model Inference tool
 * %%
 * Copyright (C) 2014 - 2018 University of Namur
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import be.yami.web.UserRequest;
import be.yami.web.UserSession;
import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class UserSessionTest {

	private static final Logger logger = LoggerFactory.getLogger(UserSessionTest.class);

	@Rule
	public TestRule watcher = new TestWatcher() {
		protected void starting(Description description) {
			logger.info(String.format("Starting test: %s()...",
					description.getMethodName()));
		};
	};

	@Test
	public void testNew() {
		String userId = "userId";
		UserSession<UserRequest> session = new UserSession<UserRequest>(userId);
		assertEquals("Wrong userId!", userId, session.getUserId());
	}

	@Test
	public void testEnqueue() throws Exception {
		String userId = "userId";
		int size = 20;
		UserRequest[] requests = generateRequest(size);
		UserSession<UserRequest> session = new UserSession<UserRequest>(userId);
		for (UserRequest req : requests) {
			session.enqueue(req);
		}
		assertEquals("Wrong number of elements!", size, session.size());
		assertArrayEquals("Wrong elements!", requests, Lists.newArrayList(session.iterator()).toArray());
	}
	
//	@Test(expected=IllegalArgumentException.class)
//	public void testEnqueueNewBefore() throws Exception {
//		String userId = "userId";
//		int size = 20;
//		UserRequest before = generateRequest(1)[0];
//		Thread.sleep(5);
//		UserRequest[] requests = generateRequest(size);
//		UserSession<UserRequest> session = new UserSession<UserRequest>(userId);
//		for (UserRequest req : requests) {
//			session.enqueue(req);
//		}
//		assertTrue("Wrong value for test!", before.getTime().before(requests[0].getTime()));
//		session.enqueue(before);
//	}

	@Test
	public void testEnqueueNewAfter() throws Exception {
		String userId = "userId";
		int size = 20;
		UserRequest[] requests = generateRequest(size);
		UserSession<UserRequest> session = new UserSession<UserRequest>(userId);
		for (UserRequest req : requests) {
			session.enqueue(req);
		}
		Thread.sleep(5);
		UserRequest after = generateRequest(1)[0];
		assertTrue("Wrong value for test!", session.getEndTime().before(after.getTime()));
		session.enqueue(after);
		assertEquals("Wrong end time!", session.getEndTime(), after.getTime());
	}

	private static final String[] types = new String[] { "HEAD", "GET", "POST" };

	UserRequest[] generateRequest(int size){
		UserRequest[] requests = new UserRequest[size];
		for(int i = 0 ; i < requests.length ; i++){
			requests[i] = new UserRequest("Cli"+i, new Date(), types[i % 3], "resource"+i , new String[i % 10],  new String[i % 10]);
		}
		return requests;
	}
}

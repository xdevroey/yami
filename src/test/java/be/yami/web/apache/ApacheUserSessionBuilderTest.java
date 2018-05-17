package be.yami.web.apache;

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

import be.yami.web.apache.ApacheUserSession;
import be.yami.web.apache.ApacheUserSessionBuilder;
import be.yami.web.apache.ApacheUserRequest;
import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.ArrayList;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.yami.web.UserSessionProcessor;
import be.yami.EntryFilter;


public class ApacheUserSessionBuilderTest {

	private static final Logger logger = LoggerFactory
			.getLogger(ApacheUserSessionBuilderTest.class);
	
	
	@Rule
	public TestRule watcher = new TestWatcher() {
		protected void starting(Description description) {
			logger.info(String.format("Starting test: %s()...",
					description.getMethodName()));
		};
	};
	
	@Test
	public void testBuildSessions() throws Exception {
		ApacheUserSessionBuilder builder = ApacheUserSessionBuilder.newInstance();
		final ArrayList<ApacheUserSession> sessions = new  ArrayList<ApacheUserSession>();
		builder.addListener(new UserSessionProcessor<ApacheUserSession>() {
			@Override
			public void process(ApacheUserSession session) {
				sessions.add(session);
			}
		});
		builder.exclude(new EntryFilter<ApacheUserRequest>() {
			@Override
			public boolean filter(ApacheUserRequest request) {
				return request.getClient().equals("0.0.0.0");
			}
		});
		InputStream in = ApacheUserSessionBuilderTest.class.getClassLoader().getResourceAsStream("test.log");
		assertNotNull("Test file ''test.log'' not found!",in);
		builder.buildSessions(in);
		logger.debug("Built sessions: {}", sessions);
		assertEquals("Wrong number of sessions!", 4, sessions.size());
		
                for(ApacheUserSession session : sessions){
                    if(session.getUserId().equals("1.1.1.1")){
                        assertEquals("Wrong number of entries!", 6, session.size()); 
                    } else if(session.getUserId().equals("1.1.1.2")){
                        assertEquals("Wrong number of entries!", 6, session.size()); 
                    } else if(session.getUserId().equals("1.1.1.3")){
                        assertEquals("Wrong number of entries!", 6, session.size()); 
                    } else {
                        fail("Unexpected user session");
                    }
                }
		
	}

}

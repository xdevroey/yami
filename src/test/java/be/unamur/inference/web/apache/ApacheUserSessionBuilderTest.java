package be.unamur.inference.web.apache;

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

import be.unamur.inference.web.UserRequestFilter;
import be.unamur.inference.web.UserSessionProcessor;


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
		builder.exclude(new UserRequestFilter<ApacheUserRequest>() {
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

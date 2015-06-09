package be.unamur.inference.ngram;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.inference.web.UserRequestFilter;
import be.unamur.inference.web.UserSessionProcessor;
import be.unamur.inference.web.apache.ApacheUserRequest;
import be.unamur.inference.web.apache.ApacheUserSession;
import be.unamur.inference.web.apache.ApacheUserSessionBuilder;
import be.unamur.inference.web.apache.ApacheUserSessionBuilderTest;
import be.unamur.inference.web.apache.UserRequesRRNKeyGenerator;
import be.unamur.transitionsystem.usagemodel.UsageModel;

public class BigramTest {
	
	private static final Logger logger = LoggerFactory.getLogger(BigramTest.class);
	
	@Rule
	public TestRule watcher = new TestWatcher() {
		protected void starting(Description description) {
			logger.info(String.format("Starting test: %s()...",
					description.getMethodName()));
		};
	};

	@Test
	public void testBigramConstruction() throws Exception {
		final Bigram<ApacheUserRequest> bigram = new Bigram<ApacheUserRequest>(UserRequesRRNKeyGenerator.getInstance());
		ApacheUserSessionBuilder builder = ApacheUserSessionBuilder.newInstance();
		builder.addListener(new UserSessionProcessor<ApacheUserSession>() {
			@Override
			public void process(ApacheUserSession session) {
				bigram.addTrace(session.iterator());
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
		
		UsageModel model = bigram.getModel();
		assertNotNull(model);
		assertEquals("Wrong start state!", Bigram.START_STATE_ID, model.getInitialState().getName());
		assertNotNull("No end state!", model.getState(Bigram.END_STATE_ID));
		assertEquals("Wrong number of states!" , 19,model.numberOfStates());
		
	}

}

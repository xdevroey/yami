package be.unamur.inference.web.apache;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.inference.web.apache.ApacheUserRequest;
import be.unamur.inference.web.apache.UserRequesRRNVKeyGenerator;


public class UserRequesRRNVKeyGeneratorTest {
	
	private static final Logger logger = LoggerFactory
			.getLogger(UserRequesRRNVKeyGeneratorTest.class);
	
	@Rule
	public TestRule watcher = new TestWatcher() {
		protected void starting(Description description) {
			logger.info(String.format("Starting test: %s()...",
					description.getMethodName()));
		};
	};

	@Test
	public void test() {
		UserRequesRRNVKeyGenerator gen = UserRequesRRNVKeyGenerator.getInstance();
		ApacheUserRequest request = new ApacheUserRequest("127.0.0.1", new Date(), "GET",
				"resources/resource.php", new String[] { "param1", "param2" },
				new String[] { "", "val2" });
		assertEquals("Wrong key value!", "GET resources/resource.php?param1=&param2=val2", gen.generateKey(request));
	}
	

	@Test
	public void testReorder() {
		UserRequesRRNVKeyGenerator gen = UserRequesRRNVKeyGenerator.getInstance();
		ApacheUserRequest request = new ApacheUserRequest("127.0.0.1", new Date(), "GET",
				"resources/resource.php", new String[] { "param2", "param1" },
				new String[] { "val2", "" });
		assertEquals("Wrong key value!", "GET resources/resource.php?param1=&param2=val2", gen.generateKey(request));
	}

}

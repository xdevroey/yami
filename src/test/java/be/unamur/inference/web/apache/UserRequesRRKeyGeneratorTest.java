package be.unamur.inference.web.apache;

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
import be.unamur.inference.web.apache.UserRequesRRKeyGenerator;

public class UserRequesRRKeyGeneratorTest {
	
	private static final Logger logger = LoggerFactory
			.getLogger(UserRequesRRKeyGeneratorTest.class);
	
	@Rule
	public TestRule watcher = new TestWatcher() {
		protected void starting(Description description) {
			logger.info(String.format("Starting test: %s()...",
					description.getMethodName()));
		};
	};

	@Test
	public void test() {
		UserRequesRRKeyGenerator gen = UserRequesRRKeyGenerator.getInstance();
		ApacheUserRequest request = new ApacheUserRequest("127.0.0.1", new Date(), "GET",
				"resources/resource.php", new String[] { "param1", "param2" },
				new String[] { "", "val2" });
		assertEquals("Wrong key value!", "GET resources/resource.php", gen.generateKey(request));
	}
	
	@Test
	public void test2() {
		UserRequesRRKeyGenerator gen = UserRequesRRKeyGenerator.getInstance();
		ApacheUserRequest request = new ApacheUserRequest("127.0.0.1", new Date(), "",
				"resources/resource.php", new String[] { "param1", "param2" },
				new String[] { "", "val2" });
		assertEquals("Wrong key value!", " resources/resource.php", gen.generateKey(request));
	}
	
	
}

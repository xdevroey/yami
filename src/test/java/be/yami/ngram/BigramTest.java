package be.yami.ngram;

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
import be.yami.ngram.Bigram;
import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.yami.web.UserSessionProcessor;
import be.yami.web.apache.ApacheUserRequest;
import be.yami.web.apache.ApacheUserSession;
import be.yami.web.apache.ApacheUserSessionBuilder;
import be.yami.web.apache.ApacheUserSessionBuilderTest;
import be.yami.web.apache.UserRequesRRNKeyGenerator;
import be.vibes.ts.UsageModel;

public class BigramTest {

    private static final Logger LOG = LoggerFactory.getLogger(BigramTest.class);

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            LOG.info(String.format("Starting test: %s()...",
                    description.getMethodName()));
        }
    ;

    };

	@Test
    public void testBigramConstruction() throws Exception {
        final Bigram<ApacheUserRequest> bigram = new Bigram<>("test", UserRequesRRNKeyGenerator.getInstance());
        ApacheUserSessionBuilder builder = ApacheUserSessionBuilder.newInstance();
        builder.addListener((UserSessionProcessor<ApacheUserSession>) (ApacheUserSession session) -> {
            bigram.addTrace(session);
        });
        builder.exclude((ApacheUserRequest request) -> request.getClient().equals("0.0.0.0"));
        InputStream in = ApacheUserSessionBuilderTest.class.getClassLoader().getResourceAsStream("test.log");
        assertNotNull("Test file ''test.log'' not found!", in);
        builder.buildSessions(in);

        UsageModel model = bigram.getModel();
        assertNotNull(model);
        assertEquals("Wrong start state!", Bigram.START_STATE_ID, model.getInitialState().getName());
        assertNotNull("No end state!", model.getState(Bigram.END_STATE_ID));
        assertEquals("Wrong number of states!", 19, model.getStatesCount());

    }

}

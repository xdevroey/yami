package be.yami.main;

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
import java.io.File;
import java.io.FileInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.yami.ngram.Bigram;
import be.yami.web.UserSessionProcessor;
import be.yami.web.apache.ApacheLogFormatPatternBuilder;
import be.yami.web.apache.ApacheUserRequest;
import be.yami.web.apache.ApacheUserSession;
import be.yami.web.apache.ApacheUserSessionBuilder;
import be.yami.web.apache.UserRequesRRKeyGenerator;
import be.vibes.dsl.io.Xml;
import be.vibes.ts.UsageModel;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;

/**
 * This is the Main class provided to process Apache Web Logs with this library.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public class MainWeb {

    private static final Logger LOG = LoggerFactory.getLogger(MainWeb.class);

    /**
     * Create a usage model from a WordPress Apache Log.
     *
     * @param args Arguments from the command line. Input file name must be at
     * position 0.
     * @throws java.lang.Exception Because shit happens...
     */
    public static void main(String[] args) throws Exception {

        // Input Log file is the first parameter provided to the application
        File input = new File(args[0]);

        long startTime = System.currentTimeMillis();

        // The bigram which will construct the model
        final Bigram<ApacheUserRequest> bigram = new Bigram<>("apache",
                UserRequesRRKeyGenerator.getInstance());

        // The session builder (Apache sessions in this case)
        ApacheUserSessionBuilder builder = ApacheUserSessionBuilder.newInstance()
                // Configure the log format to use 
                .logFormat(ApacheLogFormatPatternBuilder.COMBINED_LOG_FORMAT);

        // Add session listener that will enrich the model (via bigram) using the session
        final List<Integer> sizes = Lists.newArrayList();
        builder.addListener(new UserSessionProcessor<ApacheUserSession>() {
            int i = 0;

            @Override
            public void process(ApacheUserSession session) {
                sizes.add(session.size());
                i++;
                LOG.trace("Sessions processed: {}", i);
                LOG.trace("Session: {}", session);
                bigram.addTrace(session);
            }
        });

        // Include resources ending by '.php' or '/' or '.js'
        builder.include((ApacheUserRequest request) -> {
            if (request == null) {
                LOG.error("Request is null!");
                return false;
            }
            return request.getResource() != null
                    && (request.getResource().endsWith(".php")
                    || request.getResource().endsWith("/") || request
                    .getResource().endsWith(".js"));
        });

        // Exclude localhost and jetpack requests
        builder.exclude((ApacheUserRequest request) -> {
            if (request == null) {
                LOG.error("Request is null!");
                return true;
            }
            return request.getClient() == null
                    || request.getClient().equals("localhost")
                    || request.getClient().equals("127.0.0.1")
                    || (request.getUserAgent() != null && request
                    .getUserAgent()
                    .equals("jetmon/1.0 (Jetpack Site Uptime Monitor by WordPress.com)"));
        });

        // Launch the session building from the input file 
        builder.buildSessions(new FileInputStream(input));

        // Get the usage model from the Bigram
        UsageModel model = bigram.getModel();

        // Print XML model on System.out
        Xml.print(model, System.out);

        // Print statistics
        double sum = 0.0;
        for (Integer i : sizes) {
            sum = sum + i;
        }
        double mean = sum / sizes.size();

        double temp = 0;
        for (Integer i : sizes) {
            temp = temp + (i - mean) * (i - mean);
        }
        double variance = temp / sizes.size();
        double stdev = Math.sqrt(variance);

        System.err.println("Sessions count = " + sizes.size());
        System.err.println("Average session size = " + mean);
        System.err.println("Stdev session size = " + stdev);
        System.err.println("Min session size = " + Collections.min(sizes));
        System.err.println("Max session size = " + Collections.max(sizes));
        System.err.println("Computation time = " + (System.currentTimeMillis() - startTime) / 1000 + " sec.");
    }

}

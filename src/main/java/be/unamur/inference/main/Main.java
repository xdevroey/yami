package be.unamur.inference.main;

import java.io.File;
import java.io.FileInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.inference.ngram.Bigram;
import be.unamur.inference.web.UserRequestFilter;
import be.unamur.inference.web.UserSessionProcessor;
import be.unamur.inference.web.apache.ApacheLogFormatPatternBuilder;
import be.unamur.inference.web.apache.ApacheUserRequest;
import be.unamur.inference.web.apache.ApacheUserSession;
import be.unamur.inference.web.apache.ApacheUserSessionBuilder;
import be.unamur.inference.web.apache.UserRequesRRNKeyGenerator;
import be.unamur.transitionsystem.usagemodel.UsageModel;

import static be.unamur.transitionsystem.dsl.TransitionSystemXmlPrinter.*;

/**
 * This is the default Main class provided with the library. See source code for
 * usage model inference example.
 * 
 * @author Xavier Devroey - xavier.devroey@unamur.be
 * 
 */
public class Main {

	private static final Logger logger = LoggerFactory.getLogger(Main.class);

	/**
	 * Create a usage model from a Wordpress Apache Log.
	 */
	public static void main(String[] args) throws Exception {

		// Input Log file is the first parameter provided to the application
		File input = new File(args[0]);

		// The bigram which will construct the model
		final Bigram<ApacheUserRequest> bigram = new Bigram<ApacheUserRequest>(
				UserRequesRRNKeyGenerator.getInstance());

		// The session builder (Apache sessions in this case)
		ApacheUserSessionBuilder builder = ApacheUserSessionBuilder.newInstance()
		// Configure the log format to use 
				.logFormat(ApacheLogFormatPatternBuilder.COMBINED_LOG_FORMAT);

		// Add session listener that will enrich the model (via bigram) using the session
		builder.addListener(new UserSessionProcessor<ApacheUserSession>() {
			int i = 0;

			@Override
			public void process(ApacheUserSession session) {
				i++;
				System.err.println("" + i + " sessions processed");
				System.err.println(session);
				bigram.addTrace(session.iterator());
			}
		});

		// Include resources ending by '.php' or '/' or '.js'
		builder.include(new UserRequestFilter<ApacheUserRequest>() {
			@Override
			public boolean filter(ApacheUserRequest request) {
				if (request == null) {
					logger.error("Request is null!");
					return false;
				}
				return request.getResource() != null
						&& (request.getResource().endsWith(".php")
								|| request.getResource().endsWith("/") || request
								.getResource().endsWith(".js"));
			}
		});

		// Exclude localhost and jetpack requests
		builder.exclude(new UserRequestFilter<ApacheUserRequest>() {
			@Override
			public boolean filter(ApacheUserRequest request) {
				if (request == null) {
					logger.error("Request is null!");
					return true;
				}
				return request.getClient() == null
						|| request.getClient().equals("localhost")
						|| request.getClient().equals("127.0.0.1")
						|| (request.getUserAgent() != null && request
								.getUserAgent()
								.equals("jetmon/1.0 (Jetpack Site Uptime Monitor by WordPress.com)"));
			}
		});

		// Launch the session building from the input file 
		builder.buildSessions(new FileInputStream(input));

		// Get the usage model from the Bigram
		UsageModel model = bigram.getModel();

		// Print XML model on System.out
		print(model, System.out);
	}

}

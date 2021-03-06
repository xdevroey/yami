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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.regexp.Matcher;
import com.google.code.regexp.Pattern;

import be.yami.SequenceBuilder;
import be.yami.exception.SessionBuildException;
import static be.yami.web.apache.ApacheLogFormatPatternBuilder.*;

/**
 * This class is used to process Apache user sessions from an Apache web log.
 * The default usage of this class is:
 *
 * <pre>
 * SequenceProcessor&lt;ApacheUserSession&gt; listener;
 * EntryFilter&lt;ApacheUserRequest&gt; includeFilter;
 * EntryFilter&lt;ApacheUserRequest&gt; excludeFilter;
 * // ...
 * newInstance().logFormat(ApacheLogFormatPatternBuilder.COMMON_LOG_FORMAT)
 * 		.sessionTimeout(ApacheUserSessionBuilder.DEFAULT_TIMEOUT)
 * 		.include(includeFilter)
 * 		.exclude(excludeFilter)
 * 		.addListener(listener)
 * 		.buildSessions(new FileInputStream(&quot;apache-web.log&quot;));
 * </pre>
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 * @see ApacheUserSession
 * @see ApacheLogFormatPatternBuilder
 */
public class ApacheUserSessionBuilder extends
        SequenceBuilder<ApacheUserSession, ApacheUserRequest> {

    /**
     * The default timeout for the sessions.
     */
    public static final long DEFAULT_TIMEOUT = 1800000;

    private static final Logger LOG = LoggerFactory
            .getLogger(ApacheUserSessionBuilder.class);

    private String logFormat;
    private long sessionTimeout;
    private final Map<String, ApacheUserSession> sessions;

    /**
     * The Apache request format used to extract the different fields from the
     * request part of the log entry.
     */
    private final Pattern requestPattern = ApacheLogFormatPatternBuilder.getInstance()
            .buildPattern(ApacheLogFormatPatternBuilder.REQUEST_FORMAT);

    private ApacheUserSessionBuilder() {
        super();
        this.sessions = new HashMap<>();
        this.logFormat = ApacheLogFormatPatternBuilder.COMMON_LOG_FORMAT;
        this.sessionTimeout = DEFAULT_TIMEOUT;
    }

    /**
     * Returns a new instance of this class.
     *
     * @return A fresh instance of this class.
     */
    public static ApacheUserSessionBuilder newInstance() {
        return new ApacheUserSessionBuilder();
    }

    /**
     * Specify the log format to use. Default value is
     * {@link ApacheLogFormatPatternBuilder}.COMMON_LOG_FORMAT.
     *
     * @param logFormat The new log format to use.
     * @return This object.
     */
    public ApacheUserSessionBuilder logFormat(String logFormat) {
        this.logFormat = logFormat;
        return this;
    }

    /**
     * Set the timeout to consider for the user sessions. Default is
     * {@link ApacheUserSessionBuilder}.DEFAULT_TIMEOUT
     *
     * @param timeout The new timeout.
     * @return This object.
     */
    public ApacheUserSessionBuilder sessionTimeout(long timeout) {
        this.sessionTimeout = timeout;
        return this;
    }

    @Override
    public void buildSessions(InputStream input) throws SessionBuildException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line = reader.readLine();
            Pattern pattern = ApacheLogFormatPatternBuilder.getInstance().buildPattern(
                    this.logFormat);
            java.util.regex.Pattern p = pattern.pattern();
            java.util.regex.Matcher m;
            Matcher match;
            Map<String, String> groups;
            ApacheUserRequest userRequest;
            while (line != null) {
                LOG.trace("Processing line: {}", line);
                line = line.replace("\\\"", "''");
                m = p.matcher(line);
                if (m.matches()) {
                    match = pattern.matcher(line);
                    if (match.find()) {
                        LOG.trace("Matching line: {}", line);
                        groups = match.namedGroups();
                        userRequest = buildRequest(groups);
                        checkSessionsTimeout(userRequest.getTime());
                        if (this.isAcceptedEntry(userRequest)) {
                            addRequest(groups.get(HOST_DIRECTIVE), userRequest);
                        }
                    } else {
                        LOG.info("Skipping line: {}", line);
                    }
                } else {
                    LOG.info("Skipping line: {}", line);
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
            throw new SessionBuildException("Exception while reading input!", e);
        }
        flushSessions();
    }

    /**
     * Builds an {@link ApacheUserRequest} from the given groups.
     *
     * @see ApacheLogFormatPatternBuilder
     */
    private ApacheUserRequest buildRequest(Map<String, String> groups) {
        String req = groups.get(REQUEST_DIRECTIVE);
        if (req == null) {
            LOG.error("Request part is null in {}!", groups);
        } else {
            Matcher match = requestPattern.matcher(req.substring(1, req.length() - 1));
            if (match.matches()) {
                groups.putAll(match.namedGroups());
            } else {
                LOG.debug("Request {} did not match pattern!", req);
            }
        }
        String params = groups.get(REQUEST_QUERY_PARAMETERS_DIRECTIVE);
        String[] paramNames, paramValues;
        if (params != null) {
            String[] couples = params.split("\\&");
            paramNames = new String[couples.length];
            paramValues = new String[couples.length];
            String[] param;
            for (int i = 0; i < couples.length; i++) {
                param = couples[i].split("=");
                paramNames[i] = param.length > 0 ? param[0] : null;
                paramValues[i] = param.length > 1 ? param[1] : null;
            }
        } else {
            paramNames = new String[0];
            paramValues = new String[0];
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                ApacheLogFormatPatternBuilder.DATE_FORMAT,
                ApacheLogFormatPatternBuilder.DATE_LOCALE);
        ApacheUserRequest request = null;
        try {
            request = new ApacheUserRequest(groups.get(HOST_DIRECTIVE),
                    dateFormat.parse(groups.get(TIME_DIRECTIVE).substring(1,
                            groups.get(TIME_DIRECTIVE).length() - 1)),
                    groups.get(REQUEST_METHOD_DIRECTIVE),
                    groups.get(REQUEST_QUERY_PATH_DIRECTIVE), paramNames, paramValues);
            if (groups.get(STATUS_CODE_DIRECTIVE) != null) {
                try {
                    request.setStatusCode(Integer.parseInt(groups
                            .get(STATUS_CODE_DIRECTIVE)));
                } catch (NumberFormatException e) {
                    LOG.trace("Unable to parse status code {}!",
                            groups.get(STATUS_CODE_DIRECTIVE), e);
                }
            }
            if (groups.get(SIZE_DIRECTIVE) != null) {
                try {
                    request.setObjSize(Integer.parseInt(groups.get(SIZE_DIRECTIVE)));
                } catch (NumberFormatException e) {
                    LOG.trace("Unable to parse object size {}!",
                            groups.get(SIZE_DIRECTIVE), e);
                }
            }
            request.setReferrer(groups.get(REFERRER_DIRECTIVE));
            request.setUserAgent(groups.get(USER_AGENT_DIRECTIVE));
        } catch (ParseException e) {
            LOG.error("Error while parsing date {}!", groups.get(TIME_DIRECTIVE), e);
        }
        return request;
    }

    private void addRequest(String host, ApacheUserRequest request) {
        LOG.trace("Adding request {} for host {}", request, host);
        ApacheUserSession session = this.sessions.get(host);
        if (session == null) {
            LOG.trace("Creating new session for client {}", host);
            session = new ApacheUserSession(host);
            session.enqueue(request);
            this.sessions.put(host, session);
        } else {
            session.enqueue(request);
        }
    }

    /**
     * Check if there is no session in timeout. Sessions in timeout are sent to
     * the listeners.
     */
    private void checkSessionsTimeout(Date date) {
        Iterator<ApacheUserSession> it = this.sessions.values().iterator();
        ApacheUserSession session;
        while (it.hasNext()) {
            session = it.next();
            if (date.getTime() - session.getEndTime().getTime() > this.sessionTimeout) {
                this.sequenceCompleted(session);
                it.remove();
            }
        }
    }

    /**
     * Sends all the remaining sessions to the listeners.
     */
    private void flushSessions() {
        for (ApacheUserSession session : this.sessions.values()) {
            this.sequenceCompleted(session);
        }
        this.sessions.clear();
    }

}

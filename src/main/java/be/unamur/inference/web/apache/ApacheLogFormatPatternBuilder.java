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
import java.util.Locale;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.code.regexp.Matcher;
import com.google.code.regexp.Pattern;

/**
 * This class is used to generate regular expression {@link Pattern}s to parse
 * Apache web log entries. The patterns are built using groups names by the
 * different static Strings declared in this class. The usage of this class is
 * to get the singleton instance using getInstance() method and to call the
 * buildPattern method on this object using one of the constants defining a
 * format: COMMON_LOG_FORMAT, COMBINED_LOG_FORMAT, REQUEST_FORMAT, or
 * DATE_FORMAT. The returned {@link Pattern} is used to match a given string and
 * the resulting {@link Matcher} object may return the different groups of the
 * used pattern using the names defined in the _DIRECTIVE constants (see
 * <a>http://httpd.apache.org/docs/</a> for the different log formats). Example:
 *
 * <pre>
 * {
 * 	&#064;code
 * 	Pattern pattern = getInstance().buildPattern(COMMON_LOG_FORMAT);
 * 	Matcher match = pattern.matcher(logLine);
 * 	String request = match.group(REQUEST_DIRECTIVE);
 * 	String host = match.group(HOST_DIRECTIVE);
 * }
 * </pre>
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public class ApacheLogFormatPatternBuilder {

    /**
     * The host directive group name.
     */
    public static final String HOST_DIRECTIVE = "h";
    /**
     * The identity directive group name.
     */
    public static final String IDENTITY_DIRECTIVE = "l";
    /**
     * The user directive group name.
     */
    public static final String USER_DIRECTIVE = "u";
    /**
     * The time directive group name.
     */
    public static final String TIME_DIRECTIVE = "t";
    /**
     * The request directive groupe name.
     */
    public static final String REQUEST_DIRECTIVE = "r";
    /**
     * The status code directive group name.
     */
    public static final String STATUS_CODE_DIRECTIVE = "s";
    /**
     * The size directive group name.
     */
    public static final String SIZE_DIRECTIVE = "b";
    /**
     * The referrer directive group name.
     */
    public static final String REFERRER_DIRECTIVE = "i";
    /**
     * The user agent directive group name.
     */
    public static final String USER_AGENT_DIRECTIVE = "a";

    /**
     * The request method directive group name.
     */
    public static final String REQUEST_METHOD_DIRECTIVE = "m";
    /**
     * The request query path directive group name.
     */
    public static final String REQUEST_QUERY_PATH_DIRECTIVE = "q";
    /**
     * The request query parameters directive group name.
     */
    public static final String REQUEST_QUERY_PARAMETERS_DIRECTIVE = "c";
    /**
     * The request protocole directive group name.
     */
    public static final String REQUEST_PROTOCOL_DIRECTIVE = "p";

    /**
     * The common Apache web log format regular expression.
     */
    public static final String COMMON_LOG_FORMAT = String.format(
            "%s%s\\ %s%s\\ %s%s\\ %s%s\\ %s%s\\ %s%s\\ %s%s", "%", HOST_DIRECTIVE, "%",
            IDENTITY_DIRECTIVE, "%", USER_DIRECTIVE, "%", TIME_DIRECTIVE, "%",
            REQUEST_DIRECTIVE, "%", STATUS_CODE_DIRECTIVE, "%", SIZE_DIRECTIVE);

    /**
     * The combined Apache web log format regular expression.
     */
    public static final String COMBINED_LOG_FORMAT = String.format(
            "%s%s\\ %s%s\\ %s%s\\ %s%s\\ %s%s\\ %s%s\\ %s%s\\ %s%s\\ %s%s", "%",
            HOST_DIRECTIVE, "%", IDENTITY_DIRECTIVE, "%", USER_DIRECTIVE, "%",
            TIME_DIRECTIVE, "%", REQUEST_DIRECTIVE, "%", STATUS_CODE_DIRECTIVE, "%",
            SIZE_DIRECTIVE, "%", REFERRER_DIRECTIVE, "%", USER_AGENT_DIRECTIVE);

    /**
     * The Apache request format regular expression.
     */
    public static final String REQUEST_FORMAT = String.format(
            "%s%s\\ %s%s(\\?%s%s)?\\ %s%s", "%", REQUEST_METHOD_DIRECTIVE, "%",
            REQUEST_QUERY_PATH_DIRECTIVE, "%", REQUEST_QUERY_PARAMETERS_DIRECTIVE, "%",
            REQUEST_PROTOCOL_DIRECTIVE, "%");

    /**
     * Date format used in Apache logs.
     */
    public static final String DATE_FORMAT = "dd/MMM/yyyy:hh:mm:ss Z";

    /**
     * The {@link Locale} value used in the dates of the log. This value has to
     * be provided to {@link DateParser} objects.
     */
    public static final Locale DATE_LOCALE = Locale.ENGLISH;

    /**
     * The instance of this singleton class.
     */
    private static ApacheLogFormatPatternBuilder instance;

    /**
     * Return the instance of this singleton class.
     * @return The instance of this singleton.
     */
    public static ApacheLogFormatPatternBuilder getInstance() {
        return instance == null ? instance = new ApacheLogFormatPatternBuilder()
                : instance;
    }

    /**
     * The directives and their regular expressions.
     */
    private final ImmutableMap<String, String> directives = new ImmutableMap.Builder<String, String>()
            .put(HOST_DIRECTIVE,
                    "(?<"
                    + HOST_DIRECTIVE
                    + ">([0-9]+\\.[0-9]+\\.[0-9]+\\.[0-9]+)|(([0-9a-z-A-Z\\-]+.)*[0-9a-z-A-Z\\-]+))")
            .put(IDENTITY_DIRECTIVE,
                    "(?<" + IDENTITY_DIRECTIVE + ">(\\-)|([a-zA-Z0-9\\-\\_\\.]+))")
            .put(USER_DIRECTIVE,
                    "(?<" + USER_DIRECTIVE + ">(\\-)|(\\\"\\\")|([a-zA-Z0-9\\-\\_\\.]+))")
            .put(TIME_DIRECTIVE,
                    "(?<"
                    + TIME_DIRECTIVE
                    + ">\\[\\d{2}/[A-Za-z]{3}/\\d{4}:\\d{2}:\\d{2}:\\d{2} (\\+|\\-)\\d{4}\\])")
            .put(REQUEST_DIRECTIVE, "(?<" + REQUEST_DIRECTIVE + ">\\\"[^\\\"]*\\\")")
            .put(STATUS_CODE_DIRECTIVE, "(?<" + STATUS_CODE_DIRECTIVE + ">\\d\\d\\d)")
            .put(SIZE_DIRECTIVE, "(?<" + SIZE_DIRECTIVE + ">(\\d+)|(\\-))")
            .put(REFERRER_DIRECTIVE, "(?<" + REFERRER_DIRECTIVE + ">\\\"[^\\\"]*\\\")")
            .put(USER_AGENT_DIRECTIVE,
                    "(?<" + USER_AGENT_DIRECTIVE + ">\\\"[^\\\"]*\\\")")
            .put(REQUEST_METHOD_DIRECTIVE,
                    "(?<" + REQUEST_METHOD_DIRECTIVE + ">HEAD|POST|GET)")
            .put(REQUEST_QUERY_PATH_DIRECTIVE,
                    "(?<" + REQUEST_QUERY_PATH_DIRECTIVE + ">[^ \\t\\?]+)")
            .put(REQUEST_QUERY_PARAMETERS_DIRECTIVE,
                    "(?<" + REQUEST_QUERY_PARAMETERS_DIRECTIVE + ">[^ \\t]*)")
            .put(REQUEST_PROTOCOL_DIRECTIVE,
                    "(?<" + REQUEST_PROTOCOL_DIRECTIVE + ">[^ \\t]+)").build();

    private ApacheLogFormatPatternBuilder() {
    }

    /**
     * Return a pattern corresponding to the given regular expression string.
     *
     * @param format The format of the regular expression.
     * @return A {@link Pattern} corresponding tot the given regular expression
     * string.
     */
    public Pattern buildPattern(String format) {
        StringBuffer buff = new StringBuffer();
        char directive;
        String element;
        for (String str : Splitter.on('%').omitEmptyStrings().split(format)) {
            directive = str.charAt(0);
            element = str.replace(Character.toString(directive),
                    directives.get(Character.toString(directive)));
            buff.append(element);
        }
        return Pattern.compile(buff.toString());
    }

}

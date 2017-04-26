package be.unamur.inference.web.apache;

import static org.junit.Assert.*;
import static be.unamur.inference.web.apache.ApacheLogFormatPatternBuilder.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.regexp.Matcher;
import com.google.code.regexp.Pattern;

public class ApacheLogFormatPatternBuilderTest {
	
	private static final Logger logger = LoggerFactory.getLogger(ApacheLogFormatPatternBuilderTest.class);

	@Rule
	public TestRule watcher = new TestWatcher() {
		protected void starting(Description description) {
			logger.info(String.format("Starting test: %s()...",
					description.getMethodName()));
		};
	};

	@Test
	public void testGetInstance() {
		assertNotNull("ApacheLogFormat object may not be null!", ApacheLogFormatPatternBuilder.getInstance());
	}
	
	// ---------------------------------------------------------------------------------------------
	
	@Test
	public void testGetPatternCOMMON_LOG_FORMAT1() throws Exception {
		Pattern pattern = ApacheLogFormatPatternBuilder.getInstance().buildPattern(COMMON_LOG_FORMAT);
		String line = "138.48.33.128 - - [28/Feb/2014:16:25:21 +0100] \"GET /wordpress/wp-includes/css/admin-bar.min.css?ver=3.8.1 HTTP/1.1\" 304 -";
		Matcher matcher = pattern.matcher(line);
		assertTrue("Should match line "+line, matcher.matches());
		assertEquals("Wrong host!", "138.48.33.128", matcher.group(HOST_DIRECTIVE));
		assertEquals("Wrong identity!", "-", matcher.group(IDENTITY_DIRECTIVE));
		assertEquals("Wrong user!", "-", matcher.group(USER_DIRECTIVE));
		assertEquals("Wrong time!", "[28/Feb/2014:16:25:21 +0100]", matcher.group(TIME_DIRECTIVE));
		assertEquals("Wrong request!", "\"GET /wordpress/wp-includes/css/admin-bar.min.css?ver=3.8.1 HTTP/1.1\"", matcher.group(REQUEST_DIRECTIVE));
		assertEquals("Wrong status code!", "304", matcher.group(STATUS_CODE_DIRECTIVE));
		assertEquals("Wrong size!", "-", matcher.group(SIZE_DIRECTIVE));
	}
	
	@Test
	public void testGetPatternCOMMON_LOG_FORMAT2() throws Exception {
		Pattern pattern = ApacheLogFormatPatternBuilder.getInstance().buildPattern(COMMON_LOG_FORMAT);
		String line = "rollier.info.fundp.ac.be moi xde [28/Feb/2014:16:27:48 +0100] \"POST /wordpress/wp-admin/admin-ajax.php HTTP/1.1\" 200 26";
		Matcher matcher = pattern.matcher(line);
		assertTrue("Should match line "+line, matcher.matches());
		assertEquals("Wrong host!", "rollier.info.fundp.ac.be", matcher.group(HOST_DIRECTIVE));
		assertEquals("Wrong identity!", "moi", matcher.group(IDENTITY_DIRECTIVE));
		assertEquals("Wrong user!", "xde", matcher.group(USER_DIRECTIVE));
		assertEquals("Wrong time!", "[28/Feb/2014:16:27:48 +0100]", matcher.group(TIME_DIRECTIVE));
		assertEquals("Wrong request!", "\"POST /wordpress/wp-admin/admin-ajax.php HTTP/1.1\"", matcher.group(REQUEST_DIRECTIVE));
		assertEquals("Wrong status code!", "200", matcher.group(STATUS_CODE_DIRECTIVE));
		assertEquals("Wrong size!", "26", matcher.group(SIZE_DIRECTIVE));
	}

	@Test
	public void testGetPatternCOMBINED_LOG_FORMAT3() throws Exception {
		Pattern pattern = ApacheLogFormatPatternBuilder.getInstance().buildPattern(COMMON_LOG_FORMAT);
		String line = "41.197.132.41 - \"\" [02/Jan/2013:06:50:15 +0100] \"GET /claroline/document/document.php?cmd=exChDir&file=L0NPVVJTX1dFQl9TRVJWSUNF&cidReset=true&cidReq=INNOM361 HTTP/1.1\" 302 -";
		Matcher matcher = pattern.matcher(line);
		assertTrue("Should match line "+line, matcher.matches());
		assertEquals("Wrong host!", "41.197.132.41", matcher.group(HOST_DIRECTIVE));
		assertEquals("Wrong identity!", "-", matcher.group(IDENTITY_DIRECTIVE));
		assertEquals("Wrong user!", "\"\"", matcher.group(USER_DIRECTIVE));
		assertEquals("Wrong time!", "[02/Jan/2013:06:50:15 +0100]", matcher.group(TIME_DIRECTIVE));
		assertEquals("Wrong request!", "\"GET /claroline/document/document.php?cmd=exChDir&file=L0NPVVJTX1dFQl9TRVJWSUNF&cidReset=true&cidReq=INNOM361 HTTP/1.1\"", matcher.group(REQUEST_DIRECTIVE));
		assertEquals("Wrong status code!", "302", matcher.group(STATUS_CODE_DIRECTIVE));
		assertEquals("Wrong size!", "-", matcher.group(SIZE_DIRECTIVE));
	}

	@Test
	public void testGetPatternCOMMON_LOG_FORMATInvalid1() throws Exception {
		Pattern pattern = ApacheLogFormatPatternBuilder.getInstance().buildPattern(COMMON_LOG_FORMAT);
		String line = "";
		Matcher matcher = pattern.matcher(line);
		assertFalse("Should not match line "+line, matcher.matches());
	}
	
	@Test
	public void testGetPatternCOMMON_LOG_FORMATInvalid2() throws Exception {
		Pattern pattern = ApacheLogFormatPatternBuilder.getInstance().buildPattern(COMMON_LOG_FORMAT);
		String line = "sdfljs n mlkqj kmlsqj mml ksmlkjqs mqmj";
		Matcher matcher = pattern.matcher(line);
		assertFalse("Should not match line "+line, matcher.matches());
	}
	
	
	
	// ---------------------------------------------------------------------------------------------

	@Test
	public void testGetPatternCOMBINED_LOG_FORMAT1() throws Exception {
		Pattern pattern = ApacheLogFormatPatternBuilder.getInstance().buildPattern(COMBINED_LOG_FORMAT);
		String line = "138.48.209.58 - - [15/May/2013:20:29:41 +0200] \"GET /wp-content/plugins/jetpack/modules/wpgroho.js?ver=3.4.2 HTTP/1.1\" 304 282 \"http://www.age-namur.be/\" \"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.63 Safari/537.31\"";
		Matcher matcher = pattern.matcher(line);
		assertTrue("Should match line "+line, matcher.matches());
		assertEquals("Wrong host!", "138.48.209.58", matcher.group(HOST_DIRECTIVE));
		assertEquals("Wrong identity!", "-", matcher.group(IDENTITY_DIRECTIVE));
		assertEquals("Wrong user!", "-", matcher.group(USER_DIRECTIVE));
		assertEquals("Wrong time!", "[15/May/2013:20:29:41 +0200]", matcher.group(TIME_DIRECTIVE));
		assertEquals("Wrong request!", "\"GET /wp-content/plugins/jetpack/modules/wpgroho.js?ver=3.4.2 HTTP/1.1\"", matcher.group(REQUEST_DIRECTIVE));
		assertEquals("Wrong status code!", "304", matcher.group(STATUS_CODE_DIRECTIVE));
		assertEquals("Wrong size!", "282", matcher.group(SIZE_DIRECTIVE));
		assertEquals("Wrong referer!", "\"http://www.age-namur.be/\"", matcher.group(REFERRER_DIRECTIVE));
		assertEquals("Wrong referer!", "\"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.63 Safari/537.31\"", matcher.group(USER_AGENT_DIRECTIVE));
	}

	@Test
	public void testGetPatternCOMBINED_LOG_FORMAT2() throws Exception {
		Pattern pattern = ApacheLogFormatPatternBuilder.getInstance().buildPattern(COMBINED_LOG_FORMAT);
		String line = "rollier.info.fundp.ac.be moi xde [15/May/2013:20:29:41 +0200] \"GET /wp-content/plugins/jetpack/modules/wpgroho.js?ver=3.4.2 HTTP/1.1\" 304 - \"http://www.age-namur.be/\" \"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.63 Safari/537.31\"";
		Matcher matcher = pattern.matcher(line);
		assertTrue("Should match line "+line, matcher.matches());
		assertEquals("Wrong host!", "rollier.info.fundp.ac.be", matcher.group(HOST_DIRECTIVE));
		assertEquals("Wrong identity!", "moi", matcher.group(IDENTITY_DIRECTIVE));
		assertEquals("Wrong user!", "xde", matcher.group(USER_DIRECTIVE));
		assertEquals("Wrong time!", "[15/May/2013:20:29:41 +0200]", matcher.group(TIME_DIRECTIVE));
		assertEquals("Wrong request!", "\"GET /wp-content/plugins/jetpack/modules/wpgroho.js?ver=3.4.2 HTTP/1.1\"", matcher.group(REQUEST_DIRECTIVE));
		assertEquals("Wrong status code!", "304", matcher.group(STATUS_CODE_DIRECTIVE));
		assertEquals("Wrong size!", "-", matcher.group(SIZE_DIRECTIVE));
		assertEquals("Wrong referer!", "\"http://www.age-namur.be/\"", matcher.group(REFERRER_DIRECTIVE));
		assertEquals("Wrong referer!", "\"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.31 (KHTML, like Gecko) Chrome/26.0.1410.63 Safari/537.31\"", matcher.group(USER_AGENT_DIRECTIVE));
	}
	
	@Test
	public void testGetPatternCOMBINED_LOG_FORMATInvalid1() throws Exception {
		Pattern pattern = ApacheLogFormatPatternBuilder.getInstance().buildPattern(COMBINED_LOG_FORMAT);
		String line = "";
		Matcher matcher = pattern.matcher(line);
		assertFalse("Should not match line "+line, matcher.matches());
	}

	@Test
	public void testGetPatternCOMBINED_LOG_FORMATInvalid2() throws Exception {
		Pattern pattern = ApacheLogFormatPatternBuilder.getInstance().buildPattern(COMBINED_LOG_FORMAT);
		String line = "sdnlkjnd  mlkjqmlk  mlqkj smflk mlkqjslmq jm lkjqsdmfkljqsmdlkj";
		Matcher matcher = pattern.matcher(line);
		assertFalse("Should not match line "+line, matcher.matches());
	}
	
	// ---------------------------------------------------------------------------------------------
	
	@Test
	public void testGetPatternREQUEST_FORMAT1() throws Exception {
		Pattern pattern = ApacheLogFormatPatternBuilder.getInstance().buildPattern(REQUEST_FORMAT);
		String line = "GET /wp-content/plugins/jetpack/modules/wpgroho.js?ver=3.4.2&bla=tutu&truc=machin HTTP/1.1";
		Matcher matcher = pattern.matcher(line);
		assertTrue("Should match line "+line, matcher.matches());
		assertEquals("Wrong request method!", "GET", matcher.group(REQUEST_METHOD_DIRECTIVE));
		assertEquals("Wrong query!", "/wp-content/plugins/jetpack/modules/wpgroho.js", matcher.group(REQUEST_QUERY_PATH_DIRECTIVE));
		assertEquals("Wrong query!", "ver=3.4.2&bla=tutu&truc=machin", matcher.group(REQUEST_QUERY_PARAMETERS_DIRECTIVE));
		assertEquals("Wrong protocol!", "HTTP/1.1", matcher.group(REQUEST_PROTOCOL_DIRECTIVE));
	}
	
	@Test
	public void testGetPatternREQUEST_FORMAT2() throws Exception {
		Pattern pattern = ApacheLogFormatPatternBuilder.getInstance().buildPattern(REQUEST_FORMAT);
		String line = "GET /wp-content/plugins/jetpack/modules/wpgroho.js HTTP/1.1";
		Matcher matcher = pattern.matcher(line);
		assertTrue("Should match line "+line, matcher.matches());
		assertEquals("Wrong request method!", "GET", matcher.group(REQUEST_METHOD_DIRECTIVE));
		assertEquals("Wrong query!", "/wp-content/plugins/jetpack/modules/wpgroho.js", matcher.group(REQUEST_QUERY_PATH_DIRECTIVE));
		assertNull("Wrong query!",  matcher.group(REQUEST_QUERY_PARAMETERS_DIRECTIVE));
		assertEquals("Wrong protocol!", "HTTP/1.1", matcher.group(REQUEST_PROTOCOL_DIRECTIVE));
	}
	
	@Test
	public void testGetPatternREQUEST_FORMAT3() throws Exception {
		Pattern pattern = ApacheLogFormatPatternBuilder.getInstance().buildPattern(REQUEST_FORMAT);
		String line = "GET /wp-content/plugins/jetpack/modules/wpgroho.js? HTTP/1.1";
		Matcher matcher = pattern.matcher(line);
		assertTrue("Should match line "+line, matcher.matches());
		assertEquals("Wrong request method!", "GET", matcher.group(REQUEST_METHOD_DIRECTIVE));
		assertEquals("Wrong query!", "/wp-content/plugins/jetpack/modules/wpgroho.js", matcher.group(REQUEST_QUERY_PATH_DIRECTIVE));
		assertEquals("Wrong query!", "", matcher.group(REQUEST_QUERY_PARAMETERS_DIRECTIVE));
		assertEquals("Wrong protocol!", "HTTP/1.1", matcher.group(REQUEST_PROTOCOL_DIRECTIVE));
	}
	
	public void testGetPatternREQUEST_FORMATInvalid1() throws Exception {
		Pattern pattern = ApacheLogFormatPatternBuilder.getInstance().buildPattern(REQUEST_FORMAT);
		String line = "";
		Matcher matcher = pattern.matcher(line);
		assertFalse("Should not match line "+line, matcher.matches());
	}
	

	public void testGetPatternREQUEST_FORMATInvalid2() throws Exception {
		Pattern pattern = ApacheLogFormatPatternBuilder.getInstance().buildPattern(REQUEST_FORMAT);
		String line = "qsddsmjfmldksf lkjs mlksjfs";
		Matcher matcher = pattern.matcher(line);
		assertFalse("Should not match line "+line, matcher.matches());
	}
	
	
}

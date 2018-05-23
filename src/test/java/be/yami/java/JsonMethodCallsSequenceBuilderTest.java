package be.yami.java;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class JsonMethodCallsSequenceBuilderTest {

    private static final Logger LOG = LoggerFactory.getLogger(JsonMethodCallsSequenceBuilderTest.class);

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
    public void testBuildSessions() throws Exception {
        InputStream is = JsonMethodCallsSequenceBuilderTest.class.getClassLoader().getResourceAsStream("javacalls.json");
        JsonMethodCallsSequenceBuilder builder = new JsonMethodCallsSequenceBuilder();
        List<MethodCallSequence> result = new ArrayList<>();
        builder.addListener((seq) -> {
            result.add(seq);
        });
        builder.buildSessions(is);
        LOG.info("Generated result: {}", result);
        assertThat(result, hasSize(8));
        assertThat(result.get(0).getClassName(), equalTo("java.lang.IllegalStateException"));
        assertThat(result.get(1).getClassName(), equalTo("java.lang.IllegalStateException"));
        for (int i = 2; i < result.size(); i++) {
            assertThat(result.get(i).getClassName(), equalTo("org.apache.commons.lang3.builder.EqualsBuilderTest$TestACanEqualB"));
        }
    }

}

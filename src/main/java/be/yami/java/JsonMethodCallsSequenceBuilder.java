package be.yami.java;

import be.yami.SequenceBuilder;
import be.yami.exception.SessionBuildException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is used to process JSON entries that describe sequences of method
 * calls for Java classes. The default usage of this class is:
 *
 * <pre>
 * SequenceProcessor&lt;MethodCallSequence&gt; listener;
 * EntryFilter&lt;MethodCall&gt; includeFilter;
 * EntryFilter&lt;MethodCall&gt; excludeFilter;
 * // ...
 * newInstance().include(includeFilter)
 * 		.exclude(excludeFilter)
 * 		.addListener(listener)
 * 		.buildSessions(new FileInputStream(&quot;calls.json&quot;));
 * </pre>
 *
 * The format of the JSON file must be a
 * {@code HashMap<String, Set<List<MethodCalls>>>} generated from
 * <a href="https://github.com/Mozhan/evosuite_extension/blob/5a1b0edb8ae18b4f756a85b593b78ef0930cd772/client/src/main/java/org/evosuite/seeding/MethodCalls.java">MethodCalls.java</a>.
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class JsonMethodCallsSequenceBuilder extends SequenceBuilder<MethodCallSequence, MethodCall> {

    private static final Logger LOG = LoggerFactory.getLogger(JsonMethodCallsSequenceBuilder.class);

    private class MCall {

        String methodName;
        List<String> params;

        public MCall() {
        }

        public String getMethodName() {
            return methodName;
        }

        public List<String> getParams() {
            return params;
        }

    }

    private JsonMethodCallsSequenceBuilder() {
    }

    /**
     * Returns a new instance of this class.
     *
     * @return A fresh instance of this class.
     */
    public static JsonMethodCallsSequenceBuilder newInstance() {
        return new JsonMethodCallsSequenceBuilder();
    }

    @Override
    public void buildSessions(InputStream input) throws SessionBuildException {
        try (JsonReader reader = new JsonReader(new InputStreamReader(input))) {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            Map<String, List<List<MCall>>> result;
            // Configuring Gson with the right type
            result = gson.fromJson(reader, new TypeToken<Map<String, List<List<MCall>>>>() {
            }.getType());
            for (Map.Entry<String, List<List<MCall>>> entry : result.entrySet()) {
                String className = entry.getKey();
                LOG.debug("Building sequences for class: {}", className);
                List<List<MCall>> values = entry.getValue();
                for (List<MCall> callSequence : values) {
                    MethodCallSequence seq = new MethodCallSequence(className);
                    for (MCall c : callSequence) {
                        MethodCall call = new MethodCall(className, c.getMethodName(), c.getParams());
                        if (isAcceptedEntry(call)) {
                            seq.add(call);
                        }
                    }
                    if (seq.callsCount() > 0) {
                        LOG.debug("Sequence completed: {}", seq);
                        sequenceCompleted(seq);
                    }
                }
            }
        } catch (IOException ex) {
            LOG.error("Error while accessing input stream!", ex);
            throw new SessionBuildException("Error while processing input stream!", ex);
        }
    }

}

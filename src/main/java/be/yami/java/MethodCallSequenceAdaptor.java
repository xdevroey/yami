package be.yami.java;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class MethodCallSequenceAdaptor implements JsonDeserializer<MethodCallSequence> {
    
    private static final Logger LOG = LoggerFactory.getLogger(MethodCallSequenceAdaptor.class);
    
    @Override
    public MethodCallSequence deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        LOG.info("JSon element {}", json);
        
        return new MethodCallSequence("null");
    }
    
}

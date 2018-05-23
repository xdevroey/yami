package be.yami.java;

import be.yami.SequenceProcessor;
import be.yami.exception.ModelGenerationException;
import be.yami.ngram.NGram;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Process the method call sequences for different NGram models (one for each
 * class).
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public abstract class MultipleModelsProcessor implements SequenceProcessor<MethodCallSequence> {

    private static final Logger LOG = LoggerFactory.getLogger(MultipleModelsProcessor.class);

    private final Map<String, NGram<MethodCall>> models;

    public MultipleModelsProcessor() {
        models = new HashMap<>();
    }

    @Override
    public void process(MethodCallSequence seq) {
        String className = seq.getClassName();
        NGram<MethodCall> ngram = models.get(className);
        if (ngram == null) {
            ngram = buildNewNGram(className);
            models.put(className, ngram);
        }
        try {
            ngram.addTrace(seq);
        } catch (ModelGenerationException ex) {
            LOG.error("Error while processing sequence using NGram({})!", ngram.getClass(), ex);
        }
    }

    /**
     * Returns the NGrams generated for each class.
     *
     * @return The NGrams generated for each class.
     */
    public Iterator<NGram<MethodCall>> nGrams() {
        return models.values().iterator();
    }

    /**
     * Returns the NGrams generated for each class.
     *
     * @return The NGrams generated for each class.
     */
    public Set<NGram<MethodCall>> getNGrams() {
        return new HashSet<>(models.values());
    }

    /**
     * Build a new NGram to use for model inference. This method is called
     * whenever a new class is found in the sequences.
     *
     * @param name The name of the new ngram.
     * @return A new NGram with the given name
     */
    protected abstract NGram<MethodCall> buildNewNGram(String name);

}

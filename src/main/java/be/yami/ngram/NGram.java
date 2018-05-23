package be.yami.ngram;

import be.vibes.ts.UsageModel;
import be.yami.Sequence;
import be.yami.SequenceEntry;
import be.yami.exception.ModelGenerationException;

/**
 * This class implements methods to generate a {@link UsageModel} from a set of
 * sequences using an ngram inference method. Usage of the class should be:
 * <ul>
 * <li>Creation of the object by providing a key generator used to generate the
 * key of each sequence's element</li>
 * <li>Adding traces to infer the model using addTrace(List&lt;T&gt;)
 * method.</li>
 * <li>Getting he {@link UsageModel} using getModel() method.</li>
 * </ul>
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 * @param <T> The kind of sequences supported by the N gram
 */
public interface NGram<T extends SequenceEntry> {
    
    /**
     * Returns the name of the ngram.
     * @return The name of the ngram.
     */
    public String getName();

    /**
     * Add a sequence to the ngram. This sequence is used to enrich the model.
     *
     * @param seq The sequence to add.
     * @throws be.yami.exception.ModelGenerationException If an error happens
     * during the adding of a sequence to the model.
     */
    public void addTrace(Sequence<T> seq) throws ModelGenerationException;

    /**
     * Return the model inferred from the given sequences. This method should be
     * called once all the traces have been added to the ngram instance.
     *
     * @return The inferred model.
     * @throws be.yami.exception.ModelGenerationException If an error happens
     * during the adding of a sequence to the model.
     */
    public UsageModel getModel() throws ModelGenerationException;

}

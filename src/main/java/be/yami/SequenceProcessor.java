package be.yami;

/**
 * Represents a processor for sequences of entry of type T. Typically, this
 * processor will enrich a model with the given sequences.
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 * @param <T> The type of the elements in the sequence
 */
public interface SequenceProcessor<T extends Iterable<?>> {

    /**
     * This method is called by the SequenceBuilder when a sequence has been
     * built.
     *
     * @param seq The built session.
     */
    void process(T seq);

}

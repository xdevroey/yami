package be.yami;

/**
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 * @param <T> The type of entry in the sequence.
 */
public interface Sequence<T extends SequenceEntry> extends Iterable<T> {
    
    /**
     * Returns the number of elements in this sequence.
     * @return The number of elements in this sequence.
     */
    public int size();

}

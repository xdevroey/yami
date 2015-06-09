package be.unamur.inference.ngram;

/**
 * Defines methods to uniquely identify a given object.
 * 
 * @author Xavier Devroey - xavier.devroey@unamur.be
 * 
 * @param <T> The type of the object to identify.
 */
public interface ObjectKeyGenerator<T> {

	/**
	 * Generates a unique key for the given T object.
	 * 
	 * @param object The T object to identify.
	 * @return A unique {@link String} for the given object.
	 */
	public String generateKey(T object);

}

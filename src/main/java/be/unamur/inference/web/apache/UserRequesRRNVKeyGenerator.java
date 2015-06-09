package be.unamur.inference.web.apache;

import java.util.Arrays;

import be.unamur.inference.ngram.ObjectKeyGenerator;

/**
 * Request Resource Parameter Names and Values key generator. Generates a key
 * equal to the type of request (HEAD, GET, POST), the resource name, the names
 * of the parameters and their values. Parameters are sorted by name in the key.
 * 
 * @author Xavier Devroey <xavier.devroey@unamur.be>
 * 
 */
public class UserRequesRRNVKeyGenerator implements ObjectKeyGenerator<ApacheUserRequest> {

	private static UserRequesRRNVKeyGenerator instance = null;

	private UserRequesRRNVKeyGenerator() {
	}

	/**
	 * Returns the singleton instance of this class.
	 */
	public static UserRequesRRNVKeyGenerator getInstance() {
		return instance == null ? instance = new UserRequesRRNVKeyGenerator() : instance;
	}

	@Override
	public String generateKey(ApacheUserRequest request) {
		StringBuffer buff = new StringBuffer(String.format("%s %s?",
				request.getRequestType(), request.getResource()));
		String[] parameters = Arrays.copyOf(request.getParameters(),
				request.getParameters().length);
		String[] values = Arrays.copyOf(request.getParametersValues(),
				request.getParametersValues().length);
		assert (parameters.length == values.length);

		//Sort parameters and values
		insertionSort(parameters, values);

		for (int i = 0; i < parameters.length; i++) {
			if (i > 0) {
				buff.append("&");
			}
			buff.append(parameters[i]);
			buff.append("=");
			buff.append(values[i]);
		}
		return buff.toString();
	}

	private void insertionSort(String[] parameters, String[] values) {
		String x, y;
		int j;
		for (int i = 1; i < parameters.length; i++) {
			x = parameters[i];
			y = values[i];
			j = i;
			while (j > 0 && parameters[j - 1].compareTo(x) > 0) {
				parameters[j] = parameters[j - 1];
				values[j] = values[j - 1];
				j--;
			}
			parameters[j] = x;
			values[j] = y;
		}
	}

}

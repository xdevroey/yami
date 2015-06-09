package be.unamur.inference.web;

import java.util.Arrays;
import java.util.Date;
import static com.google.common.base.Preconditions.*;

/**
 * This object encapsulate user requests for web log entries.
 * 
 * @author Xavier Devroey - xavier.devroey@unamur.be
 * 
 */
public class UserRequest {

	/**
	 * The client of the request : ip or host usually.
	 */
	private String client;
	
	/**
	 * The time of the request.
	 */
	private Date time;
	
	/**
	 * The type of the request.
	 */
	private String requestType;
	
	/**
	 * The requested resource.
	 */
	private String resource;
	
	/**
	 * The parameter names of the request.
	 */
	private String[] parameters;
	
	/**
	 * The parameter values of the request.
	 */
	private String[] parametersValues;

	/**
	 * Creates a new request based on the given values.
	 * 
	 * @param client The client issuing the request (e.g., IP adresss or host
	 *            name).
	 * @param time The time at which the request has been issued.
	 * @param requestType The type of the request (e.g., HEAD, POST, GET)
	 * @param resource The requested resource.
	 * @param parameters The parameters provided when the request is made. Has
	 *            to be the same size as parametersValues.
	 * @param parametersValues The parameters values when the request is made.
	 *            Has to be the same size as parameters.
	 */
	public UserRequest(String client, Date time, String requestType, String resource,
			String[] parameters, String[] parametersValues) {
		checkArgument(parameters.length == parametersValues.length);
		this.client = client;
		this.time = time;
		this.requestType = requestType;
		this.resource = resource;
		this.parameters = parameters;
		this.parametersValues = parametersValues;
	}

	/**
	 * Returns the client who has issued the request.
	 */
	public String getClient() {
		return client;
	}

	/**
	 * Returns the time of the request.
	 */
	public Date getTime() {
		return time;
	}

	/**
	 * Returns the request type.
	 */
	public String getRequestType() {
		return requestType;
	}

	/**
	 * Returns the requested resource.
	 */
	public String getResource() {
		return resource;
	}

	/**
	 * Return the parameter names of the request.
	 */
	public String[] getParameters() {
		return parameters;
	}

	/**
	 * Return the parameter values of the request.
	 */
	public String[] getParametersValues() {
		return parametersValues;
	}

	@Override
	public String toString() {
		return "UserRequest [client=" + client + ", time=" + time + ", requestType="
				+ requestType + ", resource=" + resource + ", parameters="
				+ Arrays.toString(parameters) + ", parametersValues="
				+ Arrays.toString(parametersValues) + "]";
	}

}

package be.unamur.inference.web.apache;

/*-
 * #%L
 * YAMI - Yet Another Model Inference tool
 * %%
 * Copyright (C) 2014 - 2018 University of Namur
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.Arrays;
import java.util.Date;

import be.unamur.inference.web.UserRequest;

/**
 * This class represent a {@link UserRequest} as recorded by Apache in Apache
 * web logs (see <a>http://httpd.apache.org/docs/</a> for the different log
 * formats and their contents).
 * 
 * @author Xavier Devroey - xavier.devroey@unamur.be
 * 
 */
public class ApacheUserRequest extends UserRequest {

	private int statusCode;
	private int objSize;
	private String referrer;
	private String userAgent;

	/**
	 * Creates a new Apache {@link UserRequest} with minimal information needed
	 * for a request.
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
	public ApacheUserRequest(String client, Date time, String requestType,
			String resource, String[] parameters, String[] parametersValues) {
		super(client, time, requestType, resource, parameters, parametersValues);
	}

	/**
	 * Creates a new Apache {@link UserRequest} with the additional information
	 * recorded in Apache web logs.
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
	 * @param statusCode The status code of the request.
	 * @param objSize The size of the requested object.
	 */
	public ApacheUserRequest(String client, Date time, String requestType,
			String resource, String[] parameters, String[] parametersValues,
			int statusCode, int objSize) {
		super(client, time, requestType, resource, parameters, parametersValues);
		this.statusCode = statusCode;
		this.objSize = objSize;
	}

	/**
	 * Creates a new Apache {@link UserRequest} with the additional information
	 * recorded in Apache web logs.
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
	 * @param statusCode The status code of the request.
	 * @param objSize The size of the requested object.
	 * @param referrer The referrer of the user request.
	 * @param userAgent The user agent of the user request.
	 */
	public ApacheUserRequest(String client, Date time, String requestType,
			String resource, String[] parameters, String[] parametersValues,
			int statusCode, int objSize, String referrer, String userAgent) {
		super(client, time, requestType, resource, parameters, parametersValues);
		this.statusCode = statusCode;
		this.objSize = objSize;
		this.referrer = referrer;
		this.userAgent = userAgent;
	}

	/**
	 * Returns the status code of the request.
	 */
	public int getStatusCode() {
		return statusCode;
	}

	/**
	 * Returns the size of the requested object.
	 */
	public int getObjSize() {
		return objSize;
	}

	/**
	 * Returns the referrer of the request
	 */
	public String getReferrer() {
		return referrer;
	}

	/**
	 * Returns the UserAgent of the request.
	 */
	public String getUserAgent() {
		return userAgent;
	}

	/**
	 * Sets the status code of the request.
	 * @param statusCode The new status code.
	 */
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	/**
	 * Sets the size of the requested object.
	 * @param objSize The new size of the requested object.
	 */
	public void setObjSize(int objSize) {
		this.objSize = objSize;
	}

	/**
	 * Sets the referrer of the request.
	 * @param referrer The new referrer of the request.
	 */
	public void setReferrer(String referrer) {
		this.referrer = referrer;
	}

	/**
	 * Sets the user agent of the request.
	 * @param userAgent The new user agent of the request.
	 */
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	@Override
	public String toString() {
		return "ApacheUserRequest [getClient()=" + getClient() + ", getTime()="
				+ getTime() + ", getRequestType()=" + getRequestType()
				+ ", getResource()=" + getResource() + ", getParameters()="
				+ Arrays.toString(getParameters()) + ", getParametersValues()="
				+ Arrays.toString(getParametersValues()) + ", statusCode=" + statusCode
				+ ", objSize=" + objSize + ", referer=" + referrer + ", userAgent="
				+ userAgent + "]";
	}

}

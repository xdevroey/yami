package be.unamur.inference.web.exception;

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

/**
 * This exception represents an error occuring the building of the sessions.
 * 
 * @author Xavier Devroey - xavier.devroey@unamur.be
 * 
 */
public class SessionBuildException extends Exception {

	private static final long serialVersionUID = -5256467229942330643L;

	/**
	 * Creates a new {@link SessionBuildException}.
	 * 
	 * @param message The error message of the exception.
	 */
	public SessionBuildException(String message) {
		super(message);
	}

	/**
	 * Creates a new {@link SessionBuildException}.
	 * 
	 * @param message The error message of the exception.
	 * @param cause The cause of the exception.
	 */
	public SessionBuildException(String message, Exception cause) {
		super(message, cause);
	}

}

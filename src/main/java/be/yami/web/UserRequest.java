package be.yami.web;

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
import be.yami.SequenceEntry;
import java.util.Arrays;
import java.util.Date;
import static com.google.common.base.Preconditions.*;

/**
 * This object encapsulate user requests for web log entries.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public class UserRequest implements SequenceEntry{

    /**
     * The client of the request : ip or host usually.
     */
    private final String client;

    /**
     * The time of the request.
     */
    private final Date time;

    /**
     * The type of the request.
     */
    private final String requestType;

    /**
     * The requested resource.
     */
    private final String resource;

    /**
     * The parameter names of the request.
     */
    private final String[] parameters;

    /**
     * The parameter values of the request.
     */
    private final String[] parametersValues;

    /**
     * Creates a new request based on the given values.
     *
     * @param client The client issuing the request (e.g., IP adresss or host
     * name).
     * @param time The time at which the request has been issued.
     * @param requestType The type of the request (e.g., HEAD, POST, GET)
     * @param resource The requested resource.
     * @param parameters The parameters provided when the request is made. Has
     * to be the same size as parametersValues.
     * @param parametersValues The parameters values when the request is made.
     * Has to be the same size as parameters.
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
     *
     * @return The client who has issued the request.
     */
    public String getClient() {
        return client;
    }

    /**
     * Returns the time of the request.
     *
     * @return The time of the request.
     */
    public Date getTime() {
        return time;
    }

    /**
     * Returns the request type.
     *
     * @return The request type.
     */
    public String getRequestType() {
        return requestType;
    }

    /**
     * Returns the requested resource.
     *
     * @return The requested resource.
     */
    public String getResource() {
        return resource;
    }

    /**
     * Return the parameter names of the request.
     *
     * @return The parameter names of the request.
     */
    public String[] getParameters() {
        return parameters;
    }

    /**
     * Return the parameter values of the request.
     *
     * @return The parameter values of the request.
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

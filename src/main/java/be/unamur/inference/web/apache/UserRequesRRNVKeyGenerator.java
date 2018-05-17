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

import be.unamur.inference.ngram.ObjectKeyGenerator;

/**
 * Request Resource Parameter Names and Values key generator. Generates a key
 * equal to the type of request (HEAD, GET, POST), the resource name, the names
 * of the parameters and their values. Parameters are sorted by name in the key.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public class UserRequesRRNVKeyGenerator implements ObjectKeyGenerator<ApacheUserRequest> {

    private static UserRequesRRNVKeyGenerator instance = null;

    private UserRequesRRNVKeyGenerator() {
    }

    /**
     * Returns the singleton instance of this class.
     * @return The singleton instance of this class.
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

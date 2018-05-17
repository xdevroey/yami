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
 * Request Resource Parameter Names key generator. Generates a key equal to the
 * type of request (HEAD, GET, POST), the resource name and the names of the
 * parameters. Parameters are sorted by name in the key.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public class UserRequesRRNKeyGenerator implements ObjectKeyGenerator<ApacheUserRequest> {

    private static UserRequesRRNKeyGenerator instance = null;

    private UserRequesRRNKeyGenerator() {
    }

    /**
     * Returns the singleton instance of this class.
     *
     * @return The singleton instance of this class.
     */
    public static UserRequesRRNKeyGenerator getInstance() {
        return instance == null ? instance = new UserRequesRRNKeyGenerator() : instance;
    }

    @Override
    public String generateKey(ApacheUserRequest request) {
        StringBuffer buff = new StringBuffer(String.format("%s %s?",
                request.getRequestType(), request.getResource()));
        String[] parameters = Arrays.copyOf(request.getParameters(),
                request.getParameters().length);
        Arrays.sort(parameters);
        for (int i = 0; i < parameters.length; i++) {
            if (i > 0) {
                buff.append("&");
            }
            buff.append(parameters[i]);
            buff.append("=");
        }
        return buff.toString();
    }

}

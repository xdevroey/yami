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
import be.unamur.inference.ngram.ObjectKeyGenerator;

/**
 * Request Resource key generator. Generates a key equal to the type of request
 * (HEAD, GET, POST) and the resource name.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 */
public class UserRequesRRKeyGenerator implements ObjectKeyGenerator<ApacheUserRequest> {

    private static UserRequesRRKeyGenerator instance = null;

    private UserRequesRRKeyGenerator() {
    }

    /**
     * Returns the singleton instance of this class.
     *
     * @return The singleton instance of this class.
     */
    public static UserRequesRRKeyGenerator getInstance() {
        return instance == null ? instance = new UserRequesRRKeyGenerator() : instance;
    }

    @Override
    public String generateKey(ApacheUserRequest request) {
        return String.format("%s %s", request.getRequestType(), request.getResource());
    }

}

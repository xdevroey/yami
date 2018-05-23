package be.yami;


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
 * Defines methods to filter the entries of type T.
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 * @param <T> The type of entry to filter.
 */
public interface EntryFilter<T extends SequenceEntry> {

    /**
     * Returns true if the given entry has to be filtered.
     *
     * @param entry The entry to consider.
     * @return True if the given entry has to be filtered.
     */
    public boolean filter(T entry);

}

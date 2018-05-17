package be.unamur.inference.ngram;

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
import be.vibes.ts.Action;
import be.vibes.ts.UsageModel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements methods to generate a {@link UsageModel} from a set of
 * traces. The model is generated using the bigram method, meaning that the
 * probability for each next state only depends on the previous state. Usage of
 * the class should be:
 * <ul>
 * <li>Creation of the object by providing a key generator used to generate the
 * key of each trace's element</li>
 * <li>Adding traces to infer the model using addTrace(List&ltT&gt) method.</li>
 * <li>Getting he {@link UsageModel} using getModel() method.</li>
 * </ul>
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 * @param <T> The object type passed to the ObjectKeyGenerator provided at
 * construction time of Bigram object.
 */
public class Bigram<T> {

    public static final String START_STATE_ID = "s0";
    public static final String END_STATE_ID = "sX";

    private static final Logger LOG = LoggerFactory.getLogger(Bigram.class);

    private final ObjectKeyGenerator<T> keyGen;
    private int nbrTraces, nbrEntries;
    private final BigramUsageModelFactory factory;
    private final Map<String, String> statesIds;

    //private boolean computed = false;
    /**
     * Create a new {@link Bigram} object.
     *
     * @param keyGen The key generator used to generate the key corresponding to
     * each trace entry.
     */
    public Bigram(ObjectKeyGenerator<T> keyGen) {
        this.keyGen = keyGen;
        this.factory = new BigramUsageModelFactory(START_STATE_ID);
        this.statesIds = new HashMap<>();
        factory.addState(END_STATE_ID);
        factory.addTransition(START_STATE_ID, Action.EPSILON_ACTION, START_STATE_ID);
    }

    /**
     * Add a trace to the bigram. This trace is used to enrich the model.
     *
     * @param trace The trace to add.
     */
    public void addTrace(Iterator<T> trace) {
        //computed = false;
        String state = START_STATE_ID;
        String next;
        String key;
        T request;
        while (trace.hasNext()) {
            request = trace.next();
            key = this.keyGen.generateKey(request);
            next = getState(key);
            factory.addTransition(state, key, next);
            state = next;
            this.nbrEntries++;
        }
        // Final state has a transition to end state
        factory.addTransition(state, Action.EPSILON_ACTION, END_STATE_ID);
        this.nbrTraces++;
    }

    private int count = 1;
    
    private String getState(String id) {
        String state = statesIds.get(id);
        if(state == null){
            state = "s" + count;
            count++;
        }
        statesIds.put(id, state);
        return state;
    }

    /**
     * Return the model inferred from the given traces. This method should be
     * called once all the traces have been added to the {@link Bigram}
     * instance.
     *
     * @return The inferred model.
     */
    public UsageModel getModel() {
        return this.factory.build();
    }

}

package be.yami.ngram;

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
import be.yami.Sequence;
import be.yami.SequenceEntry;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements methods to generate a {@link UsageModel} from a set of
 * sequences. The model is generated using the bigram method, meaning that the
 * probability for each next state only depends on the previous state. Usage of
 * the class should be:
 * <ul>
 * <li>Creation of the object by providing a key generator used to generate the
 * key of each trace's element</li>
 * <li>Adding traces to infer the model using addTrace(List&lt;T&gt;) method.</li>
 * <li>Getting he {@link UsageModel} using getModel() method.</li>
 * </ul>
 *
 * @author Xavier Devroey - xavier.devroey@unamur.be
 *
 * @param <T> The object type passed to the ObjectKeyGenerator provided at
 * construction time of Bigram object.
 */
public class Bigram<T extends SequenceEntry> implements NGram<T>{

    public static final String START_STATE_ID = "s0";
    public static final String END_STATE_ID = "sX";

    private static final Logger LOG = LoggerFactory.getLogger(Bigram.class);

    private final String name;
    private final ObjectKeyGenerator<T> keyGen;
    private int nbrTraces, nbrEntries;
    private final BigramUsageModelFactory factory;
    private final Map<String, String> statesIds;

    //private boolean computed = false;
    
    /**
     * Create a new {@link Bigram} object.
     *
     * @param name The name of the ngram.
     * @param keyGen The key generator used to generate the key corresponding to
     * each trace entry.
     */
    public Bigram(String name, ObjectKeyGenerator<T> keyGen) {
        this.name = name;
        this.keyGen = keyGen;
        this.factory = new BigramUsageModelFactory(START_STATE_ID);
        this.statesIds = new HashMap<>();
        factory.addState(END_STATE_ID);
        factory.addTransition(END_STATE_ID, Action.EPSILON_ACTION, START_STATE_ID);
    }

    @Override
    public void addTrace(Sequence<T> seq) {
        LOG.debug("Adding trace {} to model", seq);
        //computed = false;
        String state = START_STATE_ID;
        String next;
        String key;
        T request;
        Iterator<T> trace = seq.iterator();
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
        if (state == null) {
            state = "s" + count;
            count++;
        }
        statesIds.put(id, state);
        return state;
    }

    @Override
    public UsageModel getModel() {
        return this.factory.build();
    }

    @Override
    public String getName() {
        return this.name;
    }
    
}

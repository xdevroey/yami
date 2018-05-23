package be.yami.ngram;

import be.vibes.ts.DefaultUsageModel;
import be.vibes.ts.UsageModelFactory;
import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;

/**
 * A usage model factory to be used by the {@link Bigram} class. This implementation
 * retains the number of occurrences of the transitions to define their
 * probabilities.
 *
 * @author Xavier Devroey - xavier.devroey@gmail.com
 */
public class BigramUsageModelFactory extends UsageModelFactory {

    // INV: <source, target, action, count>
    private final Map<String, Map<String, Map<String, Integer>>> transitions;

    private final Map<String, Integer> outgoingCount;

    public BigramUsageModelFactory(String initialState) {
        super(initialState);
        this.transitions = new HashMap<>();
        this.outgoingCount = new HashMap<>();
    }

    @Override
    public void addTransition(String source, String action, String target) {
        Preconditions.checkNotNull(source, "Source may not be null!");
        Preconditions.checkNotNull(action, "Action may not be null!");
        Preconditions.checkNotNull(target, "Target may not be null!");
        // Increment outgoing count
        Integer count = outgoingCount.get(source);
        if (count == null) {
            count = 0;
        }
        count++;
        outgoingCount.put(source, count);
        // get source state transitions
        Map<String, Map<String, Integer>> targetMap = transitions.get(source);
        if (targetMap == null) {
            targetMap = new HashMap<>();
            transitions.put(source, targetMap);
        }
        // get transitions that goes to target
        Map<String, Integer> actionMap = targetMap.get(target);
        if (actionMap == null) {
            actionMap = new HashMap<>();
            targetMap.put(target, actionMap);
        }
        // get count for the given action
        count = actionMap.get(action);
        if (count == null) {
            count = 0;
        }
        // increment count
        count++;
        actionMap.put(action, count);
    }

    @Override
    public DefaultUsageModel build() {
        transitions.entrySet().forEach((tr) -> { // For each starting state
            String source = tr.getKey();
            int sourceCount = outgoingCount.get(source);
            tr.getValue().entrySet().forEach((dest) -> { // for each target state
                String target = tr.getKey();
                dest.getValue().entrySet().forEach((act) -> { // for each action
                    String action = act.getKey();
                    int count = act.getValue();
                    addTransition(source, action, (1.0 * count) / (1.0 * sourceCount), target);
                });
            });
        });
        return super.build();
    }

}

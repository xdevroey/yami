package be.unamur.inference.ngram;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.Transition;
import be.unamur.transitionsystem.usagemodel.UsageModel;

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
 *            construction time of Bigram object.
 */
public class Bigram<T> {

	public static final String START_STATE_ID = "s0";
	public static final String END_STATE_ID = "sX";

	private static final Logger logger = LoggerFactory.getLogger(Bigram.class);

	private ObjectKeyGenerator<T> keyGen;
	private int nbrTraces, nbrEntries;
	private BigramUsageModel model;
	private State end;

	//private boolean computed = false;

	/**
	 * Create a new {@link Bigram} object.
	 * 
	 * @param keyGen The key generator used to generate the key corresponding to
	 *            each trace entry.
	 */
	public Bigram(ObjectKeyGenerator<T> keyGen) {
		this.keyGen = keyGen;
		this.model = new BigramUsageModel();
		State start = model.addState(START_STATE_ID);
		model.setInitialState(start);
		this.end = model.addState(END_STATE_ID);
		model.addTransition(end, start, model.addAction("end")).incrementOccurrence();
	}

	/**
	 * Add a trace to the bigram. This trace is used to enrich the model.
	 * 
	 * @param trace The trace to add.
	 */
	public void addTrace(Iterator<T> trace) {
		//computed = false;
		State state = model.getInitialState();
		State next = null;
		String key;
		OccurrenceCounTransition trans;
		T request;
		while(trace.hasNext()){
			request = trace.next();
			key = this.keyGen.generateKey(request);
			next = getState(key);
			trans = getTransition(state, next);
			trans.incrementOccurrence();
			state = next;
			this.nbrEntries++;
		}
		// Final state has a transition to end state
		trans = getTransition(state, this.end);
		trans.incrementOccurrence();
		this.nbrTraces++;
	}

	private State getState(String id) {
		State state = model.addState(id);
		return state;
	}

	/**
	 * Searches for transition in state to next or create a new one.
	 */
	private OccurrenceCounTransition getTransition(State state, State next) {
		Transition tr = null;
		Iterator<Transition> it = state.outgoingTransitions();
		while (it.hasNext() && tr == null) {
			tr = it.next();
			if (!tr.getTo().equals(next)) {
				tr = null;
			}
		}
		if (tr == null) {
			logger.debug("Creating transition from {} to {}", state.getName(),
					next.getName());
			tr = model.addTransition(state, next,
					model.addAction("clic(" + next.getName() + ")"));
		}
		return (OccurrenceCounTransition) tr;
	}

	/**
	 * Return the model inferred from the given traces. This method should be
	 * called once all the traces have been added to the {@link Bigram}
	 * instance.
	 * 
	 * @return The inferred model.
	 */
	public UsageModel getModel() {
		return this.model;
	}

}

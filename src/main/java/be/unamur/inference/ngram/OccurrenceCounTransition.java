package be.unamur.inference.ngram;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.usagemodel.UsageModel;
import be.unamur.transitionsystem.usagemodel.UsageModelTransition;

/**
 * A {@link UsageModelTransition} used during generation of {@link UsageModel}.
 * 
 * @author Xavier Devroey - xavier.devroey@unamur.be
 * @see UsageModelTransition
 * 
 */
public class OccurrenceCounTransition extends UsageModelTransition {

	/**
	 * The number of occurences of this transition.
	 */
	private int nbrOccurrences = 0;

	protected OccurrenceCounTransition() {
		super();
	}

	protected OccurrenceCounTransition(State from, State to, Action action) {
		super(from, to, action);
	}

	int getNbrOccurrences() {
		return nbrOccurrences;
	}

	void setNbrOccurrences(int nbrOccurrences) {
		this.nbrOccurrences = nbrOccurrences;
	}

	/**
	 * Increment the number of occurrences of this transition.
	 */
	void incrementOccurrence() {
		this.nbrOccurrences++;
	}

	@Override
	public double getProbability() {
		setProbability(((double) nbrOccurrences) / getFrom().outgoingSize());
		return super.getProbability();
	}

}

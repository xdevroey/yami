package be.unamur.inference.ngram;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.usagemodel.UsageModel;

/**
 * This class represent a {@link UsageModel} build using the {@link Bigram}
 * class.
 * 
 * @author Xavier Devroey - xavier.devroey@unamur.be
 * 
 */
public class BigramUsageModel extends UsageModel {

	/**
	 * Creates a new {@link BigramUsageModel}.
	 */
	public BigramUsageModel() {
		super();
		setFactory(new BigramElementFactory());
	}

	@Override
	public OccurrenceCounTransition addTransition(State from, State to, Action action) {
		return (OccurrenceCounTransition) super.addTransition(from, to, action);
	}

	@Override
	public OccurrenceCounTransition addTransition(State from, State to, Action action,
			double probability) {
		return (OccurrenceCounTransition) super.addTransition(from, to, action,
				probability);
	}

}

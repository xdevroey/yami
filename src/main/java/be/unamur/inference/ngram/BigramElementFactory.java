package be.unamur.inference.ngram;

import be.unamur.transitionsystem.Action;
import be.unamur.transitionsystem.State;
import be.unamur.transitionsystem.usagemodel.UsageModelElementFactory;
import be.unamur.transitionsystem.usagemodel.UsageModelTransition;

/**
 * Factory used by the {@link BigramUsageModel} class.
 * 
 * @author Xavier Devroey - xavier.devroey@unamur.be
 * 
 */
class BigramElementFactory extends UsageModelElementFactory {

	@Override
	public UsageModelTransition buildTransition() {
		return new OccurrenceCounTransition();
	}

	@Override
	public UsageModelTransition buildTransition(State from, State to, Action action) {
		return new OccurrenceCounTransition(from, to, action);
	}

}

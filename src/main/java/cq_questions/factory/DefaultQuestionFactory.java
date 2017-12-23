package cq_questions.factory;

import java.util.Map;
import java.util.Random;

import cq_questions.model.Option;
import cq_questions.model.Selectable;
import cq_questions.model.Tipable;

public class DefaultQuestionFactory implements IQuestionFactory {
	private static final Tipable DEFAULT_TIP = new Tipable("cq", "DEFAULT_TIP", "no tips", 1);

	private static final Selectable DEFAULT_QUESTION = new Selectable("cq", "DEFAULT_QUESTION",
			"no questions", new Option[] { new Option("$", true), new Option("%", false),
					new Option("/", false), new Option("#", false) });

	private final Random generator;

	private final Map<String, Tipable> tips;

	private final Map<String, Selectable> selectables;

	public DefaultQuestionFactory(
			final Map<String, Tipable> tips,
			final Map<String, Selectable> selectables) {
		this.tips = tips;
		this.selectables = selectables;
		this.generator = new Random();
	}

	@Override
	public Selectable getSelectable() {
		final Selectable selectable = this.getRandomElement(this.selectables);
		return selectable != null ? selectable : DEFAULT_QUESTION;
	}

	@Override
	public Tipable getTip() {
		final Tipable tip = this.getRandomElement(this.tips);
		return tip != null ? tip : DEFAULT_TIP;
	}

	private <K, V> V getRandomElement(Map<K, V> map) {
		final int size = map.size();
		if (size > 0) {
			int id = this.generator.nextInt(size);
			for (final Map.Entry<K, V> entry : map.entrySet()) {
				if (id == 0)
					return entry.getValue();
				id--;
			}
		}
		return null;
	}
}

package cq_questions;

import java.util.List;
import java.util.Random;

public class QuestionFactory implements IQuestionFactory {
	static final RawTip DEFAULT_TIP = new RawTip("no tips", 1);

	static final RawSelectable DEFAULT_QUESTION = new RawSelectable("no questions", new String[] { "$", "%", "/", "#" }, 1);

	private final List<RawSelectable> questions;

	private final List<RawTip> tips;

	private final Random generator;

	public QuestionFactory(final List<RawSelectable> rawQuestions, final List<RawTip> rawTips) {
		this.questions = rawQuestions;
		this.tips = rawTips;
		this.generator = new Random();
	}

	@Override
	public RawSelectable getSelectable() {
		if (this.questions.isEmpty())
			return QuestionFactory.DEFAULT_QUESTION;
		else
			return this.questions.get(this.generator.nextInt(this.questions.size()));
	}

	@Override
	public RawTip getTip() {
		if (this.tips.isEmpty())
			return QuestionFactory.DEFAULT_TIP;
		else
			return this.tips.get(this.generator.nextInt(this.tips.size()));
	}
}

package cq_questions;

import java.util.Arrays;

public final class RawSelectable {
	private final String question;

	private final String[] options;

	private final int trueAnswer;

	public RawSelectable() {
		this("", new String[] {}, 0);
	}

	public RawSelectable(final String question, final String[] options, final int trueAnswer) {
		super();
		this.question = question;
		this.options = options;
		this.trueAnswer = trueAnswer;
	}

	public String[] getOptions() {
		return this.options;
	}

	public String getQuestion() {
		return this.question;
	}

	public int getTrueAnswer() {
		return this.trueAnswer;
	}

	@Override
	public String toString() {
		return "RawQuestion [question=" + this.question + ", options=" + Arrays.toString(this.options) + ", trueAnswer="
				+ this.trueAnswer + "]";
	}
}

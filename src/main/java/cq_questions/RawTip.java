package cq_questions;

public final class RawTip {
	private final String question;

	private final int answer;

	public RawTip() {
		this("", 0);
	}

	public RawTip(final String question, final int answer) {
		super();
		this.question = question;
		this.answer = answer;
	}

	public int getAnswer() {
		return this.answer;
	}

	public String getQuestion() {
		return this.question;
	}

	@Override
	public String toString() {
		return "RawTip [question=" + this.question + ", answer=" + this.answer + "]";
	}
}

package cq_questions.model;

public final class Tipable implements Comparable<Tipable> {
	private final String question;

	private final int answer;

	private final String id;

	private final String type;

	public Tipable(String type, final String id, final String question, final int answer) {
		this.type = type;
		this.id = id;
		this.question = question;
		this.answer = answer;
	}

	public String getType() {
		return this.type;
	}

	public int getAnswer() {
		return this.answer;
	}

	public String getId() {
		return this.id;
	}

	public String getQuestion() {
		return this.question;
	}

	@Override
	public String toString() {
		return String.format("cq|%s|%s|%s|", this.id, this.question, this.answer);
	}

	@Override
	public int compareTo(Tipable o) {
		return Integer.compare(Integer.valueOf(this.id), Integer.valueOf(o.id));
	}
}

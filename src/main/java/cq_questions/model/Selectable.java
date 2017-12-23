package cq_questions.model;

public final class Selectable implements Comparable<Selectable> {
	private String id;

	private String theme;

	private String question;

	private Option[] options;

	public Selectable() {
		// TODO Auto-generated constructor stub
	}

	public Selectable(
			final String theme,
			final String id,
			final String question,
			final Option[] options) {
		this.theme = theme;
		this.id = id;
		this.question = question;
		this.options = options;
	}

	public String getId() {
		return this.id;
	}

	public String getTheme() {
		return this.theme;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Option[] getOptions() {
		return this.options;
	}

	public String getQuestion() {
		return this.question;
	}

	public void setTheme(final String theme) {
		this.theme = theme;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		final Selectable other = (Selectable) obj;
		if (this.question == null) {
			if (other.question != null)
				return false;
		} else if (!this.question.equals(other.question))
			return false;
		return true;
	}

	@Override
	public String toString() {
		final String question = this.getQuestion();
		final Option[] options = this.getOptions();
		final Option[] copyOptions = new Option[4];
		System.arraycopy(options, 0, copyOptions, 0, 4);
		for (int i = 0; i < copyOptions.length; i++) {
			if (copyOptions[i].isCorrect()) {
				final Option first = copyOptions[0];
				copyOptions[0] = copyOptions[i];
				copyOptions[i] = first;
			}
		}
		return String.format("%s|%s|%s|%s|%s|%s|%s|", this.theme, this.id, question,
				copyOptions[0].getText(), copyOptions[1].getText(), copyOptions[2].getText(),
				copyOptions[3].getText());
	}

	@Override
	public int compareTo(Selectable o) {
		return Integer.compare(Integer.valueOf(this.id), Integer.valueOf(o.id));
	}
}

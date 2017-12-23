package cq_questions.model;

public class Option {
	private String text;

	private boolean isCorrect;

	public Option() {
		// TODO Auto-generated constructor stub
	}

	public Option(final String text, final boolean isCorrect) {
		this.text = text;
		this.isCorrect = isCorrect;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		final Option other = (Option) obj;
		if (this.text == null) {
			if (other.text != null)
				return false;
		} else if (!this.text.equals(other.text))
			return false;
		return true;
	}

	public String getText() {
		return this.text;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.text == null) ? 0 : this.text.hashCode());
		return result;
	}

	public boolean isCorrect() {
		return this.isCorrect;
	}

	public void setCorrect(final boolean isCorrect) {
		this.isCorrect = isCorrect;
	}

	public void setText(final String text) {
		this.text = text;
	}
}

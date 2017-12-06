package cq_questions;

import java.util.List;

public class Questions {

	private final List<RawTip> tips;

	private final List<RawSelectable> rawq;

	public Questions(final List<RawSelectable> rawq, final List<RawTip> tips) {
		this.rawq = rawq;
		this.tips = tips;
	}

	public List<RawTip> getTips() {
		return this.tips;
	}

	public List<RawSelectable> getRawQuestions() {
		return this.rawq;
	}

}

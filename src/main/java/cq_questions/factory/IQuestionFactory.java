package cq_questions.factory;

import cq_questions.model.Selectable;
import cq_questions.model.Tipable;

public interface IQuestionFactory {
	Selectable getSelectable();

	Tipable getTip();
}
package cq_questions;

public interface IQuestionFactory {
	RawSelectable getSelectable();

	RawTip getTip();
}
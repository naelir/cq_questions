package cq_questions.validator;

public interface IValidator<T> {
	boolean isValid(T t);
}

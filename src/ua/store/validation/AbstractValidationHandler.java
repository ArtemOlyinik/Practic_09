package ua.store.validation;

public abstract class AbstractValidationHandler implements ValidationHandler {

    protected ValidationHandler nextHandler;

    @Override
    public ValidationHandler setNext(ValidationHandler next) {
        this.nextHandler = next;
        return next;
    }

    @Override
    public ValidationResult validate(String fieldName, String value) {
        ValidationResult result = new ValidationResult();
        performValidation(fieldName, value, result);
        return result;
    }

    @Override
    public ValidationResult handle(String fieldName, String value) {
        ValidationResult result = new ValidationResult();

        performValidation(fieldName, value, result);

        if (nextHandler != null) {
            ValidationResult nextResult = nextHandler.handle(fieldName, value);
            result.addMessages(nextResult.getMessages());
        }

        return result;
    }

    protected abstract void performValidation(String fieldName, String value, ValidationResult result);

    protected void addError(ValidationResult result, String message) {
        result.addMessage(message);
    }
}
package ua.store.validation;

public class NotEmptyValidator extends AbstractValidationHandler {

    private final String customMessage;

    public NotEmptyValidator() {
        this("Поле не може бути порожнім");
    }

    public NotEmptyValidator(String customMessage) {
        this.customMessage = customMessage;
    }

    @Override
    protected void performValidation(String fieldName, String value, ValidationResult result) {
        if (value == null || value.isBlank()) {
            addError(result, customMessage);
        }
    }
}
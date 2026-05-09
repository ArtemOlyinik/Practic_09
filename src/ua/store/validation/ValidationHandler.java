package ua.store.validation;

public interface ValidationHandler {

    ValidationHandler setNext(ValidationHandler next);

    ValidationResult validate(String fieldName, String value);

    default ValidationResult handle(String fieldName, String value) {
        ValidationResult result = validate(fieldName, value);
        return result;
    }
}
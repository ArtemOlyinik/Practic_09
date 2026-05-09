package ua.store.validation;

import java.util.ArrayList;
import java.util.List;

public class LoginValidator extends AbstractValidationHandler {

    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 20;
    private static final String ALLOWED_CHARS_PATTERN = "^[a-zA-Z][a-zA-Z0-9_]*$";

    @Override
    protected void performValidation(String fieldName, String value, ValidationResult result) {
        if (value == null || value.isBlank()) {
            return;
        }

        List<String> errors = new ArrayList<>();

        if (!value.matches(ALLOWED_CHARS_PATTERN)) {
            if (!Character.isLetter(value.charAt(0))) {
                errors.add("Логін повинен починатися з латинської літери");
            }
            errors.add("Логін повинен містити лише латинські літери, цифри та _");
        }

        if (value.length() < MIN_LENGTH) {
            errors.add("Логін повинен містити мінімум " + MIN_LENGTH + " символи");
        }

        if (value.length() > MAX_LENGTH) {
            errors.add("Логін повинен містити максимум " + MAX_LENGTH + " символів");
        }

        for (String error : errors) {
            addError(result, error);
        }
    }
}
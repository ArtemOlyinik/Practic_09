package ua.store.validation;

import java.util.ArrayList;
import java.util.List;

public class PasswordValidator extends AbstractValidationHandler {

    private static final int MIN_LENGTH = 8;
    private static final String HAS_DIGIT = ".*\\d.*";
    private static final String HAS_SPECIAL = ".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*";
    private static final String HAS_UPPERCASE = ".*[A-Z].*";

    @Override
    protected void performValidation(String fieldName, String value, ValidationResult result) {
        if (value == null || value.isBlank()) {
            return;
        }

        List<String> errors = new ArrayList<>();

        if (value.length() < MIN_LENGTH) {
            errors.add("Пароль повинен містити мінімум " + MIN_LENGTH + " символів");
        }

        if (!value.matches(HAS_DIGIT)) {
            errors.add("Пароль повинен містити хоча б одну цифру");
        }

        if (!value.matches(HAS_UPPERCASE)) {
            errors.add("Пароль повинен містити хоча б одну велику літеру");
        }

        if (!value.matches(HAS_SPECIAL)) {
            errors.add("Пароль повинен містити хоча б один спеціальний символ (!@#$%^&* тощо)");
        }

        for (String error : errors) {
            addError(result, error);
        }
    }
}
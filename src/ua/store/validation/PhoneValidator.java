package ua.store.validation;

import java.util.ArrayList;
import java.util.List;

public class PhoneValidator extends AbstractValidationHandler {

    private static final String PHONE_PATTERN = "^\\+?[0-9]{10,15}$";
    private static final String HAS_PLUS = "^\\+";
    private static final String HAS_COUNTRY_CODE = "^\\+\\d{1,3}";
    private static final String ONLY_DIGITS_AND_PLUS = "^[0-9+]+$";

    @Override
    protected void performValidation(String fieldName, String value, ValidationResult result) {
        if (value == null || value.isBlank()) {
            return;
        }

        List<String> errors = new ArrayList<>();

        String cleaned = value.replaceAll("\\s", "").replaceAll("-", "").replaceAll("\\(", "").replaceAll("\\)", "");

        if (!cleaned.matches(ONLY_DIGITS_AND_PLUS)) {
            errors.add("Телефон повинен містити лише цифри та +");
        }

        if (!value.matches(PHONE_PATTERN)) {
            if (errors.isEmpty()) {
                if (cleaned.length() < 10) {
                    errors.add("Занадто короткий номер телефону");
                } else if (cleaned.length() > 15) {
                    errors.add("Занадто довгий номер телефону");
                } else {
                    errors.add("Некоректний формат телефону");
                }
            }
        }

        for (String error : errors) {
            addError(result, error);
        }
    }
}
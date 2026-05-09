package ua.store.validation;

import java.util.ArrayList;
import java.util.List;

public class EmailValidator extends AbstractValidationHandler {

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String HAS_AT_SYMBOL = "^[^@]*@[^@]*$";
    private static final String HAS_DOMAIN = ".*\\.[a-zA-Z]{2,}$";
    private static final String LATIN_ONLY = "^[a-zA-Z0-9@._%-]+$";

    @Override
    protected void performValidation(String fieldName, String value, ValidationResult result) {
        if (value == null || value.isBlank()) {
            return;
        }

        List<String> errors = new ArrayList<>();

        if (!value.matches(LATIN_ONLY)) {
            errors.add("Email повинен містити лише латинські літери");
        }

        if (!value.contains("@")) {
            errors.add("Відсутній символ '@'");
        } else {
            String[] parts = value.split("@");
            if (parts.length == 2) {
                if (parts[0].isEmpty()) {
                    errors.add("Відсутня локальна частина (до @)");
                }
                if (parts[1].isEmpty()) {
                    errors.add("Відсутня доменна частина (після @)");
                } else if (!parts[1].contains(".")) {
                    errors.add("Відсутній домен поштового сервісу (повинна бути крапка)");
                }
            }
        }

        if (!value.matches(EMAIL_PATTERN)) {
            if (errors.isEmpty()) {
                errors.add("Некоректний формат email");
            }
        }

        for (String error : errors) {
            addError(result, error);
        }
    }
}
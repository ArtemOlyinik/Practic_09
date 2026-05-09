package ua.store.validation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ValidationResult {

    private final List<String> messages;

    public ValidationResult() {
        this.messages = new ArrayList<>();
    }

    public void addMessage(String message) {
        if (message != null && !message.isBlank()) {
            messages.add(message);
        }
    }

    public void addMessages(List<String> messages) {
        if (messages != null) {
            messages.stream()
                    .filter(m -> m != null && !m.isBlank())
                    .forEach(this.messages::add);
        }
    }

    public List<String> getMessages() {
        return Collections.unmodifiableList(new ArrayList<>(messages));
    }

    public boolean hasErrors() {
        return !messages.isEmpty();
    }

    public boolean hasMultipleErrors() {
        return messages.size() >= 2;
    }

    public int getErrorCount() {
        return messages.size();
    }

    public static ValidationResult success() {
        return new ValidationResult();
    }

    public static ValidationResult error(String message) {
        ValidationResult result = new ValidationResult();
        result.addMessage(message);
        return result;
    }
}
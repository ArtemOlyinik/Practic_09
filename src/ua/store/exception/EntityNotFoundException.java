package ua.store.exception;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String entityName, int id) {
        super(entityName + " with id=" + id + " not found");
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

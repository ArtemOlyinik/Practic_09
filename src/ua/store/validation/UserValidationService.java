package ua.store.validation;

import ua.store.model.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserValidationService {

    private final ValidationHandler loginChain;
    private final ValidationHandler emailChain;
    private final ValidationHandler passwordChain;
    private final ValidationHandler phoneChain;

    public UserValidationService() {
        this.loginChain = buildLoginChain();
        this.emailChain = buildEmailChain();
        this.passwordChain = buildPasswordChain();
        this.phoneChain = buildPhoneChain();
    }

    private ValidationHandler buildLoginChain() {
        NotEmptyValidator notEmpty = new NotEmptyValidator("Логін не може бути порожнім");
        LoginValidator loginValidator = new LoginValidator();
        notEmpty.setNext(loginValidator);
        return notEmpty;
    }

    private ValidationHandler buildEmailChain() {
        NotEmptyValidator notEmpty = new NotEmptyValidator("Email не може бути порожнім");
        EmailValidator emailValidator = new EmailValidator();
        notEmpty.setNext(emailValidator);
        return notEmpty;
    }

    private ValidationHandler buildPasswordChain() {
        NotEmptyValidator notEmpty = new NotEmptyValidator("Пароль не може бути порожнім");
        PasswordValidator passwordValidator = new PasswordValidator();
        notEmpty.setNext(passwordValidator);
        return notEmpty;
    }

    private ValidationHandler buildPhoneChain() {
        NotEmptyValidator notEmpty = new NotEmptyValidator("Телефон не може бути порожнім");
        PhoneValidator phoneValidator = new PhoneValidator();
        notEmpty.setNext(phoneValidator);
        return notEmpty;
    }

    public ValidationResult validate(User user) {
        ValidationResult result = new ValidationResult();

        result.addMessages(loginChain.handle("login", user.getLogin()).getMessages());
        result.addMessages(emailChain.handle("email", user.getEmail()).getMessages());
        result.addMessages(passwordChain.handle("password", user.getPassword()).getMessages());
        result.addMessages(phoneChain.handle("phone", user.getPhone()).getMessages());

        return result;
    }

    public ValidationResult validateLogin(String login) {
        return loginChain.handle("login", login);
    }

    public ValidationResult validateEmail(String email) {
        return emailChain.handle("email", email);
    }

    public ValidationResult validatePassword(String password) {
        return passwordChain.handle("password", password);
    }

    public ValidationResult validatePhone(String phone) {
        return phoneChain.handle("phone", phone);
    }

    public List<String> getAllErrors(User user) {
        return validate(user).getMessages();
    }

    public boolean isValid(User user) {
        return !validate(user).hasErrors();
    }

    public boolean isValid(String fieldName, String value) {
        switch (fieldName) {
            case "login" -> { return !validateLogin(value).hasErrors(); }
            case "email" -> { return !validateEmail(value).hasErrors(); }
            case "password" -> { return !validatePassword(value).hasErrors(); }
            case "phone" -> { return !validatePhone(value).hasErrors(); }
            default -> throw new IllegalArgumentException("Unknown field: " + fieldName);
        }
    }
}
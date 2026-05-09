package ua.store.validation;

import ua.store.model.User;

import java.util.ArrayList;
import java.util.List;

public class ValidatorChain {

    public static ValidationResult validateUser(User user) {
        ValidationResult result = new ValidationResult();

        result.addMessages(validateLogin(user.getLogin()).getMessages());
        result.addMessages(validateEmail(user.getEmail()).getMessages());
        result.addMessages(validatePassword(user.getPassword()).getMessages());
        result.addMessages(validatePhone(user.getPhone()).getMessages());

        return result;
    }

    public static ValidationResult validateLogin(String login) {
        ValidationHandler notEmpty = new NotEmptyValidator("Логін не може бути порожнім");
        ValidationHandler loginValidator = new LoginValidator();

        notEmpty.setNext(loginValidator);

        return notEmpty.handle("login", login);
    }

    public static ValidationResult validateEmail(String email) {
        ValidationHandler notEmpty = new NotEmptyValidator("Email не може бути порожнім");
        ValidationHandler emailValidator = new EmailValidator();

        notEmpty.setNext(emailValidator);

        return notEmpty.handle("email", email);
    }

    public static ValidationResult validatePassword(String password) {
        ValidationHandler notEmpty = new NotEmptyValidator("Пароль не може бути порожнім");
        ValidationHandler passwordValidator = new PasswordValidator();

        notEmpty.setNext(passwordValidator);

        return notEmpty.handle("password", password);
    }

    public static ValidationResult validatePhone(String phone) {
        ValidationHandler notEmpty = new NotEmptyValidator("Телефон не може бути порожнім");
        ValidationHandler phoneValidator = new PhoneValidator();

        notEmpty.setNext(phoneValidator);

        return notEmpty.handle("phone", phone);
    }

    public static List<String> collectAllErrors(User user) {
        List<String> allErrors = new ArrayList<>();

        ValidationResult loginResult = validateLogin(user.getLogin());
        allErrors.addAll(loginResult.getMessages());

        ValidationResult emailResult = validateEmail(user.getEmail());
        allErrors.addAll(emailResult.getMessages());

        ValidationResult passwordResult = validatePassword(user.getPassword());
        allErrors.addAll(passwordResult.getMessages());

        ValidationResult phoneResult = validatePhone(user.getPhone());
        allErrors.addAll(phoneResult.getMessages());

        return allErrors;
    }
}
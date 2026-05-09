package ua.store.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class User {

    private final int id;
    private final String login;
    private final String email;
    private final String password;
    private final String phone;
    private final List<String> validationMessages;

    private User(Builder builder) {
        this.id = builder.id;
        this.login = builder.login;
        this.email = builder.email;
        this.password = builder.password;
        this.phone = builder.phone;
        this.validationMessages = builder.validationMessages != null
            ? Collections.unmodifiableList(new ArrayList<>(builder.validationMessages))
            : Collections.emptyList();
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public List<String> getValidationMessages() {
        return validationMessages;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(User user) {
        return new Builder()
                .id(user.id)
                .login(user.login)
                .email(user.email)
                .password(user.password)
                .phone(user.phone)
                .validationMessages(user.validationMessages);
    }

    public static final class Builder implements UserBuilder {
        private int id;
        private String login;
        private String email;
        private String password;
        private String phone;
        private List<String> validationMessages;

        @Override
        public Builder id(int id) {
            this.id = id;
            return this;
        }

        @Override
        public Builder login(String login) {
            this.login = login;
            return this;
        }

        @Override
        public Builder email(String email) {
            this.email = email;
            return this;
        }

        @Override
        public Builder password(String password) {
            this.password = password;
            return this;
        }

        @Override
        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        @Override
        public Builder validationMessages(List<String> validationMessages) {
            this.validationMessages = validationMessages;
            return this;
        }

        @Override
        public User build() {
            return new User(this);
        }
    }

    public interface UserBuilder {
        UserBuilder id(int id);
        UserBuilder login(String login);
        UserBuilder email(String email);
        UserBuilder password(String password);
        UserBuilder phone(String phone);
        UserBuilder validationMessages(List<String> validationMessages);
        User build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", validationMessages=" + validationMessages +
                '}';
    }
}
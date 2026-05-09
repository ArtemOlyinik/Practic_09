package ua.store;

import ua.store.dao.ConnectionPool;
import ua.store.exception.EntityNotFoundException;
import ua.store.model.Category;
import ua.store.model.Product;
import ua.store.model.User;
import ua.store.service.StoreService;
import ua.store.validation.UserValidationService;
import ua.store.validation.ValidationResult;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Tech Store Demo (Practica 08) ===\n");
        System.out.println("Patterns: Data Mapper + Singleton + Factory + Builder + Object Pool\n");

        StoreService service = new StoreService();

        System.out.println("Available connections in pool: " + ConnectionPool.getInstance().getAvailableCount());

        System.out.println("\n--- 1. Creating Categories ---");
        service.addCategory("Laptops", "Portable computers for work and gaming");
        service.addCategory("Smartphones", "Mobile phones and accessories");

        System.out.println("\n--- 2. All Categories ---") ;
        List<Category> categories = service.getAllCategories();
        categories.forEach(System.out::println);

        System.out.println("\n--- 3. Creating Products ---");
        if (!categories.isEmpty()) {
            int laptopCatId = categories.get(0).getId();
            int phoneCatId = categories.get(1).getId();

            service.addProduct("MacBook Pro 14", 1999.99, laptopCatId);
            service.addProduct("Dell XPS 15", 1499.99, laptopCatId);
            service.addProduct("Lenovo ThinkPad", 1299.99, laptopCatId);
            service.addProduct("iPhone 15 Pro", 999.99, phoneCatId);
            service.addProduct("Samsung Galaxy S24", 899.99, phoneCatId);
        }

        System.out.println("\n--- 4. All Products ---");
        List<Product> products = service.getAllProducts();
        products.forEach(System.out::println);
        System.out.println("Total products: " + service.getProductCount());

        System.out.println("\n--- 5. Category with Products (Complex Mapping) ---");
        List<Category> categoriesWithProducts = service.getAllCategoriesWithProducts();
        for (Category cat : categoriesWithProducts) {
            System.out.println("\n" + cat.getName() + ":");
            for (Product p : cat.getProducts()) {
                System.out.println("  - " + p.getName() + " ($" + p.getPrice() + ")");
            }
        }

        System.out.println("\n--- 6. Updating Product ---");
        if (!products.isEmpty()) {
            Product p = products.get(0);
            service.updateProduct(p.getId(), p.getName() + " (Updated)", p.getPrice() + 100, p.getCategoryId());
            System.out.println("Updated: " + service.getProductById(p.getId()));
        }

        System.out.println("\n--- 7. Deleting Product ---");
        if (products.size() > 1) {
            int deleteId = products.get(1).getId();
            service.deleteProduct(deleteId);
            System.out.println("Deleted product with id: " + deleteId);
        }

        System.out.println("\n--- 8. Final Product List ---");
        service.getAllProducts().forEach(System.out::println);

        System.out.println("\n--- 9. Testing Custom Exceptions ---");
        System.out.println("Trying to get non-existent category (id=999)...");
        try {
            service.getCategoryById(999);
        } catch (EntityNotFoundException e) {
            System.out.println("Exception caught: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Exception caught: " + e.getMessage());
        }

        System.out.println("\n--- 10. Builder Pattern Demo ---");
        Product newProduct = Product.builder()
                .name("Custom Built PC")
                .price(2500.00)
                .categoryId(1)
                .build();
        System.out.println("Created with Builder: " + newProduct);

        System.out.println("\n" + "=".repeat(60));
        System.out.println("=== VALIDATION DEMO (Practica 09) ===");
        System.out.println("Patterns: Chain of Responsibility + Builder + Singleton\n");

        UserValidationService validationService = new UserValidationService();

        System.out.println("--- 11. Valid User ---");
        User validUser = User.builder()
                .login("johndoe")
                .email("john.doe@gmail.com")
                .password("Password123!")
                .phone("+380991234567")
                .build();
        ValidationResult validResult = validationService.validate(validUser);
        System.out.println("User: " + validUser.getLogin());
        if (validResult.hasErrors()) {
            System.out.println("Errors: " + validResult.getMessages());
        } else {
            System.out.println("Validation: PASSED - No errors");
        }

        System.out.println("\n--- 12. Invalid User (Multiple Errors) ---");
        User invalidUser = User.builder()
                .login("моє_ім'я")
                .email("моя%пошта@gmailcom")
                .password("weak")
                .phone("abc123")
                .build();
        ValidationResult invalidResult = validationService.validate(invalidUser);
        System.out.println("User: " + invalidUser.getLogin());
        System.out.println("Email: " + invalidUser.getEmail());

        if (invalidResult.hasErrors()) {
            System.out.println("\nErrors found: " + invalidResult.getErrorCount());
            System.out.println("\nValidation hints:");
            invalidResult.getMessages().forEach(msg -> System.out.println("  - " + msg));
        }

        System.out.println("\n--- 13. Individual Field Validation ---");
        System.out.println("\nLogin 'ab': " + validationService.validateLogin("ab").getMessages());
        System.out.println("Email 'test@': " + validationService.validateEmail("test@").getMessages());
        System.out.println("Password 'short': " + validationService.validatePassword("short").getMessages());
        System.out.println("Phone '123': " + validationService.validatePhone("123").getMessages());

        System.out.println("\n--- 14. Builder Pattern with ValidationMessages ---");
        User userWithMessages = User.builder()
                .login("user123")
                .email("user@example.com")
                .password("SecurePass1!")
                .phone("+380501234567")
                .build();
        System.out.println("User with messages field: " + userWithMessages);
        System.out.println("Validation messages: " + userWithMessages.getValidationMessages());

        System.out.println("\n=== Demo Complete ===");
        System.out.println("Available connections: " + ConnectionPool.getInstance().getAvailableCount());
    }
}

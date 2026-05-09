package ua.store.service;

import ua.store.dao.CategoryDAO;
import ua.store.dao.DAOFactory;
import ua.store.dao.ProductDAO;
import ua.store.exception.EntityNotFoundException;
import ua.store.model.Category;
import ua.store.model.Product;

import java.util.List;

public class StoreService {

    private final CategoryDAO categoryDAO;
    private final ProductDAO productDAO;

    public StoreService() {
        DAOFactory factory = DAOFactory.getInstance();
        this.categoryDAO = factory.createCategoryDAO();
        this.productDAO = factory.createProductDAO();
    }

    public void addCategory(String name, String description) {
        Category category = Category.builder()
                .name(name)
                .description(description)
                .build();
        categoryDAO.add(category);
        System.out.println("Category added: " + name);
    }

    public List<Category> getAllCategories() {
        return categoryDAO.getAll();
    }

    public Category getCategoryById(int id) {
        return categoryDAO.getById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category", id));
    }

    public Category getCategoryWithProducts(int id) {
        return categoryDAO.getByIdWithProducts(id)
                .orElseThrow(() -> new EntityNotFoundException("Category", id));
    }

    public List<Category> getAllCategoriesWithProducts() {
        return categoryDAO.getAllWithProducts();
    }

    public void updateCategory(int id, String name, String description) {
        Category category = Category.builder()
                .id(id)
                .name(name)
                .description(description)
                .build();
        categoryDAO.update(category);
        System.out.println("Category updated: " + id);
    }

    public void deleteCategory(int id) {
        categoryDAO.delete(id);
        System.out.println("Category deleted: " + id);
    }

    public void addProduct(String name, double price, int categoryId) {
        Product product = Product.builder()
                .name(name)
                .price(price)
                .categoryId(categoryId)
                .build();
        productDAO.add(product);
        System.out.println("Product added: " + name);
    }

    public List<Product> getAllProducts() {
        return productDAO.getAll();
    }

    public Product getProductById(int id) {
        return productDAO.getById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product", id));
    }

    public List<Product> getProductsByCategory(int categoryId) {
        return productDAO.getByCategoryId(categoryId);
    }

    public void updateProduct(int id, String name, double price, int categoryId) {
        Product product = Product.builder()
                .id(id)
                .name(name)
                .price(price)
                .categoryId(categoryId)
                .build();
        productDAO.update(product);
        System.out.println("Product updated: " + id);
    }

    public void deleteProduct(int id) {
        productDAO.delete(id);
        System.out.println("Product deleted: " + id);
    }

    public int getProductCount() {
        return productDAO.count();
    }
}

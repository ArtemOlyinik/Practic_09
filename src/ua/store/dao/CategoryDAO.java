package ua.store.dao;

import ua.store.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryDAO extends GenericDAO<Category> {
    Optional<Category> getByIdWithProducts(int id);
    List<Category> getAllWithProducts();
}

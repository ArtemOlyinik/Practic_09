package ua.store.dao;

import ua.store.model.Product;

import java.util.List;

public interface ProductDAO extends GenericDAO<Product> {
    List<Product> getByCategoryId(int categoryId);
    int count();
}

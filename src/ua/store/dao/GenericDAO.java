package ua.store.dao;

import java.util.List;
import java.util.Optional;

public interface GenericDAO<T> {
    void add(T entity);
    Optional<T> getById(int id);
    List<T> getAll();
    void update(T entity);
    void delete(int id);
}

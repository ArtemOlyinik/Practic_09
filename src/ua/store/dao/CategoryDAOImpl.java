package ua.store.dao;

import ua.store.exception.DatabaseException;
import ua.store.exception.EntityNotFoundException;
import ua.store.model.Category;
import ua.store.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryDAOImpl implements CategoryDAO {

    private static volatile CategoryDAOImpl instance;

    private CategoryDAOImpl() {
    }

    public static CategoryDAOImpl getInstance() {
        if (instance == null) {
            synchronized (CategoryDAOImpl.class) {
                if (instance == null) {
                    instance = new CategoryDAOImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public void add(Category category) {
        String sql = "INSERT INTO categories(name, description) VALUES(?, ?)";
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, category.getName());
                pstmt.setString(2, category.getDescription());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to add category", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    @Override
    public Optional<Category> getById(int id) {
        String sql = "SELECT * FROM categories WHERE id = ?";
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(mapResultSetToCategory(rs));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to get category by id", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Category> getByIdWithProducts(int id) {
        String sql = "SELECT * FROM categories WHERE id = ?";
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        Category category = mapResultSetToCategory(rs);
                        List<Product> products = getProductsByCategoryId(connection, id);
                        return Optional.of(Category.builder(category).withProducts(products).build());
                    }
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to get category with products", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
        return Optional.empty();
    }

    @Override
    public List<Category> getAll() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories";
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    categories.add(mapResultSetToCategory(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to get all categories", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
        return categories;
    }

    @Override
    public List<Category> getAllWithProducts() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM categories";
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    int categoryId = rs.getInt("id");
                    Category category = mapResultSetToCategory(rs);
                    List<Product> products = getProductsByCategoryId(connection, categoryId);
                    categories.add(Category.builder(category).withProducts(products).build());
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to get all categories with products", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
        return categories;
    }

    @Override
    public void update(Category category) {
        String sql = "UPDATE categories SET name = ?, description = ? WHERE id = ?";
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, category.getName());
                pstmt.setString(2, category.getDescription());
                pstmt.setInt(3, category.getId());

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new EntityNotFoundException("Category", category.getId());
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update category", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM categories WHERE id = ?";
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new EntityNotFoundException("Category", id);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete category", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    private Category mapResultSetToCategory(ResultSet rs) throws SQLException {
        return Category.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .build();
    }

    private List<Product> getProductsByCategoryId(Connection connection, int categoryId) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE category_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, categoryId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    products.add(Product.builder()
                            .id(rs.getInt("id"))
                            .name(rs.getString("name"))
                            .price(rs.getDouble("price"))
                            .categoryId(rs.getInt("category_id"))
                            .build());
                }
            }
        }
        return products;
    }
}

package ua.store.dao;

import ua.store.exception.DatabaseException;
import ua.store.exception.EntityNotFoundException;
import ua.store.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDAOImpl implements ProductDAO {

    private static volatile ProductDAOImpl instance;

    private ProductDAOImpl() {
    }

    public static ProductDAOImpl getInstance() {
        if (instance == null) {
            synchronized (ProductDAOImpl.class) {
                if (instance == null) {
                    instance = new ProductDAOImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public void add(Product product) {
        String sql = "INSERT INTO products(name, price, category_id) VALUES(?, ?, ?)";
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, product.getName());
                pstmt.setDouble(2, product.getPrice());
                pstmt.setInt(3, product.getCategoryId());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to add product", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    @Override
    public Optional<Product> getById(int id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        return Optional.of(mapResultSetToProduct(rs));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to get product by id", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
        return Optional.empty();
    }

    @Override
    public List<Product> getAll() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to get all products", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
        return products;
    }

    @Override
    public void update(Product product) {
        String sql = "UPDATE products SET name = ?, price = ?, category_id = ? WHERE id = ?";
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setString(1, product.getName());
                pstmt.setDouble(2, product.getPrice());
                pstmt.setInt(3, product.getCategoryId());
                pstmt.setInt(4, product.getId());

                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new EntityNotFoundException("Product", product.getId());
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to update product", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM products WHERE id = ?";
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, id);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new EntityNotFoundException("Product", id);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete product", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
    }

    @Override
    public List<Product> getByCategoryId(int categoryId) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE category_id = ?";
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, categoryId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        products.add(mapResultSetToProduct(rs));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to get products by category", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
        return products;
    }

    @Override
    public int count() {
        String sql = "SELECT COUNT(*) FROM products";
        Connection connection = null;
        try {
            connection = ConnectionPool.getInstance().getConnection();
            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to count products", e);
        } finally {
            ConnectionPool.getInstance().releaseConnection(connection);
        }
        return 0;
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        return Product.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .price(rs.getDouble("price"))
                .categoryId(rs.getInt("category_id"))
                .build();
    }
}

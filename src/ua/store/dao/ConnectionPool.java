package ua.store.dao;

import ua.store.exception.DatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ConnectionPool {

    private static final int POOL_SIZE = 5;
    private static final String URL = "jdbc:sqlite:store.db";
    private static volatile ConnectionPool instance;

    private final List<Connection> availableConnections;
    private final List<Connection> usedConnections;

    private ConnectionPool() {
        availableConnections = new ArrayList<>();
        usedConnections = new ArrayList<>();
        initializePool();
    }

    public static ConnectionPool getInstance() {
        if (instance == null) {
            synchronized (ConnectionPool.class) {
                if (instance == null) {
                    instance = new ConnectionPool();
                }
            }
        }
        return instance;
    }

    private void initializePool() {
        try {
            Class.forName("org.sqlite.JDBC");
            for (int i = 0; i < POOL_SIZE; i++) {
                Connection connection = DriverManager.getConnection(URL);
                availableConnections.add(connection);
            }
            createTables();
        } catch (ClassNotFoundException | SQLException e) {
            throw new DatabaseException("Failed to initialize connection pool", e);
        }
    }

    private void createTables() {
        String createCategories = """
            CREATE TABLE IF NOT EXISTS categories (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                description TEXT
            )
            """;

        String createProducts = """
            CREATE TABLE IF NOT EXISTS products (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                price REAL NOT NULL,
                category_id INTEGER,
                FOREIGN KEY (category_id) REFERENCES categories(id)
            )
            """;

        try (Statement stmt = availableConnections.get(0).createStatement()) {
            stmt.execute(createCategories);
            stmt.execute(createProducts);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to create tables", e);
        }
    }

    public synchronized Connection getConnection() {
        if (availableConnections.isEmpty()) {
            throw new DatabaseException("No available connections in pool");
        }
        Connection connection = availableConnections.remove(availableConnections.size() - 1);
        usedConnections.add(connection);
        return connection;
    }

    public synchronized void releaseConnection(Connection connection) {
        if (connection == null) {
            return;
        }
        if (usedConnections.remove(connection)) {
            availableConnections.add(connection);
        }
    }

    public synchronized void shutdown() {
        closeConnections(availableConnections);
        closeConnections(usedConnections);
    }

    private void closeConnections(List<Connection> connections) {
        for (Connection conn : connections) {
            try {
                conn.close();
            } catch (SQLException ignored) {
            }
        }
    }

    public synchronized int getAvailableCount() {
        return availableConnections.size();
    }
}

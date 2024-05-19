package org.example.services;

import org.example.dto.Client;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientService.class);
    private DataSource dataSource;

    public ClientService(DataSource dataSource) {
        this.dataSource = dataSource;
        checkTableExistence();
    }

    private void validateName(String name) {
        if (name == null || name.length() < 2 || name.length() > 100) {
            LOGGER.error("Invalid client name: \"{}\". Name must be between 2 and 100 characters.", name);
            throw new IllegalArgumentException("Client name must be between 2 and 100 characters.");
        }
    }

    private void checkTableExistence() {
        try (Connection connection = dataSource.getConnection();
             ResultSet rs = connection.getMetaData().getTables(null, null, "CLIENT", new String[] {"TABLE"})) {
            if (!rs.next()) {
                LOGGER.error("Table 'client' does not exist in the database. Please check your database setup.");
                throw new SQLException("Table 'client' does not exist.");
            } else {
                LOGGER.info("Confirmed that the table 'client' exists in the database.");
            }
        } catch (SQLException e) {
            LOGGER.error("Failed to check for table existence: ", e);
            throw new RuntimeException("Database check failed", e);
        }
    }

    public long create(String name) throws SQLException {
        validateName(name);
        String sql = "INSERT INTO client (name) VALUES (?)";
        Connection connection = dataSource.getConnection();
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                LOGGER.info("Connected to database, preparing to execute INSERT: {}", sql);
                statement.setString(1, name);
                int affectedRows = statement.executeUpdate();
                LOGGER.info("Executed INSERT command, affected rows: {}", affectedRows);
                if (affectedRows == 0) {
                    connection.rollback();
                    throw new SQLException("Creating client failed, no rows affected.");
                }
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        long id = generatedKeys.getLong(1);
                        connection.commit();
                        LOGGER.info("Generated new client ID: {}", id);
                        return id;
                    } else {
                        connection.rollback();
                        throw new SQLException("Creating client failed, no ID obtained.");
                    }
                }
            }
        } catch (Exception e) {
            connection.rollback();
            LOGGER.error("Transaction failed, rolled back.", e);
            throw e;
        } finally {
            connection.close();
        }
    }

    public String getById(long id) throws SQLException {
        String sql = "SELECT name FROM client WHERE id = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    return name;
                } else {
                    throw new SQLException("Client with ID " + id + " does not exist.");
                }
            }
        }
    }

    public void setName(long id, String name) throws SQLException {
        validateName(name);
        String sql = "UPDATE client SET name = ? WHERE id = ?";
        Connection connection = dataSource.getConnection();
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                LOGGER.info("Connected to database, preparing to execute UPDATE: {}", sql);
                statement.setString(1, name);
                statement.setLong(2, id);
                int affectedRows = statement.executeUpdate();
                LOGGER.info("Executed UPDATE command, affected rows: {}", affectedRows);
                if (affectedRows == 0) {
                    connection.rollback();
                    throw new SQLException("Updating client failed, no rows affected.");
                }
                connection.commit();
            }
        } catch (Exception e) {
            connection.rollback();
            LOGGER.error("Transaction failed, rolled back.", e);
            throw e;
        } finally {
            connection.close();
        }
    }

    public void deleteById(long id) throws SQLException {
        String sql = "DELETE FROM client WHERE id = ?";
        Connection connection = dataSource.getConnection();
        try {
            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, id);
                int affectedRows = statement.executeUpdate();
                LOGGER.info("Executed DELETE command, affected rows: {}", affectedRows);
                if (affectedRows == 0) {
                    connection.rollback();
                    throw new SQLException("Deleting client failed, no rows affected.");
                }
                connection.commit();
            }
        } catch (Exception e) {
            connection.rollback();
            LOGGER.error("Transaction failed, rolled back.", e);
            throw e;
        } finally {
            connection.close();
        }
    }

    public List<Client> listAll() throws SQLException {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT id, name FROM client";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                clients.add(new Client(id, name));
            }
        }
        return clients;
    }
}

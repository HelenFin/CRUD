package org.example;

import org.example.services.*;
import org.example.util.Database;
import org.example.util.FlywayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Database database = Database.getInstance();
        FlywayService flywayService = new FlywayService(database);

        // Set to true to enable database cleaning, false to disable
        boolean enableClean = false;

        if (enableClean) {
            LOGGER.info("Cleaning and migrating the database to ensure a fresh start.");
            flywayService.cleanAndMigrateDatabase();
        } else {
            LOGGER.info("Skipping database cleaning. Only migrating.");
            flywayService.migrateDatabase(); // You may need to implement this method
        }

        try (Connection testConn = database.getDataSource().getConnection()) {
            if (testConn != null) {
                LOGGER.info("Successfully connected to the database.");
            } else {
                LOGGER.error("Failed to connect to the database.");
                return;
            }
        } catch (SQLException ex) {
            LOGGER.error("Failed to connect to the database.", ex);
            return;
        }

        ClientService clientService = new ClientService(database.getDataSource());

        try {
            // Create a new client
            long clientId = clientService.create("New Client");
            LOGGER.info("Created new client with ID: {}", clientId);

            // Retrieve client by ID
            String clientName = clientService.getById(clientId);
            LOGGER.info("Retrieved client name by ID {}: {}", clientId, clientName);

            // Update client name
            clientService.setName(clientId, "Updated Client Name");
            LOGGER.info("Updated client name to 'Updated Client Name'");

            // List all clients
            clientService.listAll().forEach(client -> LOGGER.info("Client ID: {}, Name: {}", client.getId(), client.getName()));

            // Delete client by ID
            clientService.deleteById(clientId);
            LOGGER.info("Deleted client with ID {}", clientId);

        } catch (SQLException e) {
            LOGGER.error("Database error occurred", e);
        } catch (Exception e) {
            LOGGER.error("An error occurred", e);
        }
    }
}

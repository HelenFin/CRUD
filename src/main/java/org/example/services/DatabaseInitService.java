package org.example.services;

import org.example.util.Database;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseInitService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseInitService.class);

    public static void main(String[] args) {
        Database database = Database.getInstance();
        Flyway flyway = Flyway.configure()
                .dataSource(database.getDataSource())
                .locations("classpath:db/migration")
                .load();

        try {
            flyway.migrate();
            LOGGER.info("Database initialized successfully using Flyway.");
        } catch (Exception e) {
            LOGGER.error("Error initializing database using Flyway", e);
        }

        MigrationInfo[] migrationInfos = flyway.info().all();
        for (MigrationInfo migrationInfo : migrationInfos) {
            LOGGER.info("Migration version: {}, description: {}, status: {}",
                    migrationInfo.getVersion().toString(),
                    migrationInfo.getDescription(),
                    migrationInfo.getState().name());
        }
    }
}

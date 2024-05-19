package org.example.util;

import org.example.util.Database;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class FlywayService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FlywayService.class);
    private final Database database;

    public FlywayService(Database database) {
        this.database = database;
    }

    public void cleanAndMigrateDatabase() {
        Flyway flyway = Flyway.configure()
                .dataSource(database.getDataSource())
                .locations("classpath:db/migration")
                .cleanDisabled(false)
                .load();
        try {
            flyway.clean();
            flyway.migrate();
            LOGGER.info("Database cleaned and migrations applied successfully.");
        } catch (Exception e) {
            LOGGER.error("Error during the cleaning and migration process", e);
        }
    }

    public void migrateDatabase() {
        Flyway flyway = Flyway.configure()
                .dataSource(database.getDataSource())
                .locations("classpath:db/migration")
                .load();
        try {
            flyway.migrate();
            LOGGER.info("Database migrations applied successfully.");
        } catch (Exception e) {
            LOGGER.error("Error applying database migrations", e);
        }
    }
}




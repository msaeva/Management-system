package com.example.management_system.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.ejb.EJBException;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.flywaydb.core.Flyway;

import javax.sql.DataSource;

//@Singleton
@WebListener
public class FlywayConfig implements ServletContextListener {

    @Resource(lookup = "jdbc/persistence")
    private DataSource dataSource;

    @PostConstruct
    public void migrateDatabase() {
        try {
            Flyway flyway = Flyway.configure()
                    .dataSource(dataSource)
                    .locations("classpath:db/migrations")
                    .load();

            flyway.migrate();
        } catch (Exception e) {
            throw new EJBException("Error migrating database with Flyway", e);
        }
    }
}
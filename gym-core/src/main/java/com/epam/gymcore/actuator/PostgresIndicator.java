package com.epam.gymcore.actuator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Component
public class PostgresIndicator implements HealthIndicator {
    private final DataSource dataSource;

    public PostgresIndicator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Health health() {
        try (Connection connection = dataSource.getConnection()){
            if(connection.isValid(1)){
                return Health.up()
                        .withDetail("database",connection.getMetaData().getDatabaseProductName())
                        .withDetail("url",connection.getMetaData().getURL())
                        .build();
            }else
                return Health.down().build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

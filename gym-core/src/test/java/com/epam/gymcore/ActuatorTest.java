package com.epam.gymcore;

import com.epam.gymcore.actuator.PostgresIndicator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Health;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ActuatorTest {
    private DataSource dataSource;
    private Connection connection;
    private DatabaseMetaData databaseMetaData;

    @BeforeEach
    void initData(){
        dataSource = mock(DataSource.class);
        connection = mock(Connection.class);
        databaseMetaData = mock(DatabaseMetaData.class);
    }

    @Test
    void healthUp_connectionValid() throws Exception{
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.getMetaData()).thenReturn(databaseMetaData);
        when(connection.isValid(1)).thenReturn(true);
        when(databaseMetaData.getDatabaseProductName()).thenReturn("PostgreSQL");
        when(databaseMetaData.getURL()).thenReturn("jdbc:postgresql://localhost:5430/gym_crm");

        Health health = new PostgresIndicator(dataSource).health();

        assertEquals("UP",health.getStatus().getCode());
        assertEquals(databaseMetaData.getDatabaseProductName(),health.getDetails().get("database"));
        assertEquals(databaseMetaData.getURL(),health.getDetails().get("url"));
    }

    @Test
    void healthDown_connectionNotValid() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        when(connection.isValid(1)).thenReturn(false);

        Health health = new PostgresIndicator(dataSource).health();

        assertEquals("DOWN", health.getStatus().getCode());
    }
}

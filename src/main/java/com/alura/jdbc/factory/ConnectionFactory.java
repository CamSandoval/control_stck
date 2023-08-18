package com.alura.jdbc.factory;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;

public class ConnectionFactory {
    private DataSource dataSource;

    public ConnectionFactory(){
        var pooledDataSource = new ComboPooledDataSource();
        pooledDataSource.setJdbcUrl("jdbc:mysql://localhost/control_de_stock?useTimeZone=true&serverTimeZone=UTC");
        pooledDataSource.setUser("juank");
        pooledDataSource.setPassword("Mysql123*");
        pooledDataSource.setMaxPoolSize(10);

        this.dataSource = pooledDataSource;
    }
    public Connection recuperarConexion() throws SQLException {
        return this.dataSource.getConnection();
    }
}

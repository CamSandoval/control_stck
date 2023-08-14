package com.alura.jdbc.view;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CreaConexion {
    public static Connection recuperarConexion() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost/control_de_stock?useTimeZone=true&serverTimeZone=UTC",
                "juank",
                "Mysql123*");
    }
}

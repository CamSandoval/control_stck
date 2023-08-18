package com.alura.jdbc.pruebas;

import com.alura.jdbc.factory.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class PruebaDelete {
    public static void main(String[] args) throws SQLException {
        Connection con = new ConnectionFactory().recuperarConexion();

        Statement statement = con.createStatement();
        statement.execute("DELETE FROM producto WHERE id=99");
        System.out.println(statement.getUpdateCount());
    }
}

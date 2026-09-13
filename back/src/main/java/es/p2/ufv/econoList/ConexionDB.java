package es.p2.ufv.econoList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB
{
    private static final String URL = System.getenv().getOrDefault(
            "SPRING_DATASOURCE_URL", "jdbc:postgresql://localhost:5432/user_db");
    private static final String USUARIO = System.getenv().getOrDefault(
            "SPRING_DATASOURCE_USERNAME", "postgres");
    private static final String CONTRASENA = System.getenv().getOrDefault(
            "SPRING_DATASOURCE_PASSWORD", "change-me");

    public static Connection getConnection() throws SQLException
    {
        return DriverManager.getConnection(URL, USUARIO, CONTRASENA);
    }
}

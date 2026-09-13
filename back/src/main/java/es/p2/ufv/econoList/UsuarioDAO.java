package es.p2.ufv.econoList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO // Clase para manejar la base de datos de usuarios
{
    // Método para registrar un nuevo usuario
    public boolean registrarUsuario(Usuario usuario)
    {
        // Ahora usamos "username" y "password" en lugar de "nombre" y "contrasena"
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, usuario.getUsername());  // Cambié a username
            stmt.setString(2, usuario.getPassword());  // Cambié a password
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    // Método para autenticar a un usuario (verificar login)
    public boolean autenticarUsuario(String username, String password)
    {
        // Ahora usamos "username" y "password" en lugar de "correo" y "contrasena"
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, username);  // Cambié a username
            stmt.setString(2, password);  // Cambié a password
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Si existe un usuario con el username y password
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public boolean usuarioExiste(String username) // Método para verificar si un usuario ya existe
    {
        String sql = "SELECT 1 FROM users WHERE username = ?";
        try (Connection conn = ConexionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql))
        {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Devuelve true si existe
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
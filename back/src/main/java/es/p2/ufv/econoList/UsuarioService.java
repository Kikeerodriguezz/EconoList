package es.p2.ufv.econoList;

import org.springframework.stereotype.Service;

@Service
public class UsuarioService // Clase de servicio para manejar la lógica de negocio relacionada con los usuarios
{
    private UsuarioDAO usuarioDAO;

    public UsuarioService()
    {
        this.usuarioDAO = new UsuarioDAO();
    }

    // Método para registrar un usuario (cambiado para aceptar dos parámetros)
    public boolean registrarUsuario(String username, String password)
    {
        Usuario usuario = new Usuario(username, password);
        return usuarioDAO.registrarUsuario(usuario);
    }

    // Método para autenticar un usuario
    public boolean autenticarUsuario(String username, String password)
    {
        return usuarioDAO.autenticarUsuario(username, password);
    }

    public boolean usuarioExiste(String username)
    {
        return usuarioDAO.usuarioExiste(username);
    }
}
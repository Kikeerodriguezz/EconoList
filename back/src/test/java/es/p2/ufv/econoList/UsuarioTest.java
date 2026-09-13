package es.p2.ufv.econoList;

import es.p2.ufv.econoList.Usuario;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UsuarioTest
{
    @Test
    public void testConstructorYGetters()
    {
        // Datos de prueba
        String expectedUsername = "testUser";
        String expectedPassword = "testPassword";

        // Crear usuario
        Usuario usuario = new Usuario(expectedUsername, expectedPassword);

        // Verificar que los getters devuelvan lo que esperamos
        assertEquals(expectedUsername, usuario.getUsername());
        assertEquals(expectedPassword, usuario.getPassword());
    }

    @Test
    public void testSetters()
    {
        // Crear un usuario vacío
        Usuario usuario = new Usuario("", "");

        // Usar setters
        usuario.setUsername("newUser");
        usuario.setPassword("newPassword");

        // Verificar que los setters cambian los valores correctamente
        assertEquals("newUser", usuario.getUsername());
        assertEquals("newPassword", usuario.getPassword());
    }
}

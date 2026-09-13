package es.p2.ufv.econoList;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    private Usuario usuario;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        usuario = new Usuario("testUser", "testPassword");
    }

    @Test
    public void testRegistrarUsuario_Exito() throws Exception {
        when(usuarioService.registrarUsuario(usuario.getUsername(), usuario.getPassword())).thenReturn(true);

        mockMvc.perform(post("/api/usuarios/registro")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk())
                .andExpect(content().string("Usuario registrado correctamente."));

        verify(usuarioService, times(1)).registrarUsuario(usuario.getUsername(), usuario.getPassword());
    }

    @Test
    public void testRegistrarUsuario_Error() throws Exception {
        when(usuarioService.registrarUsuario(usuario.getUsername(), usuario.getPassword())).thenReturn(false);

        mockMvc.perform(post("/api/usuarios/registro")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk())
                .andExpect(content().string("Error al registrar el usuario."));

        verify(usuarioService, times(1)).registrarUsuario(usuario.getUsername(), usuario.getPassword());
    }

    @Test
    public void testAutenticarUsuario_Exito() throws Exception {
        when(usuarioService.autenticarUsuario(usuario.getUsername(), usuario.getPassword())).thenReturn(true);

        mockMvc.perform(post("/api/usuarios/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk())
                .andExpect(content().string("Login exitoso."));

        verify(usuarioService, times(1)).autenticarUsuario(usuario.getUsername(), usuario.getPassword());
    }

    @Test
    public void testAutenticarUsuario_Error() throws Exception {
        when(usuarioService.autenticarUsuario(usuario.getUsername(), usuario.getPassword())).thenReturn(false);

        mockMvc.perform(post("/api/usuarios/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isUnauthorized()) // Aquí sí cambia
                .andExpect(content().string("Error de autenticación."));

        verify(usuarioService, times(1)).autenticarUsuario(usuario.getUsername(), usuario.getPassword());
    }
}
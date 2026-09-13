package es.p2.ufv.econoList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios") // Ruta base del controlador
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // Endpoint raíz del backend (para evitar el Whitelabel Error Page)
    @GetMapping("/")
    public String home() {
        return "Este es el backend de econoList. Usa /api/usuarios";
    }

    // Registro de usuario
    @PostMapping("/registro")
    public String registrarUsuario(@RequestBody Usuario usuario) {
        System.out.println("Intentando registrar usuario: " + usuario.getUsername());
        boolean registrado = usuarioService.registrarUsuario(usuario.getUsername(), usuario.getPassword());
        return registrado ? "Usuario registrado correctamente." : "Error al registrar el usuario.";
    }

    // Login de usuario
    @PostMapping("/login")
    public ResponseEntity<String> autenticarUsuario(@RequestBody Usuario usuario) {
        boolean autenticado = usuarioService.autenticarUsuario(usuario.getUsername(), usuario.getPassword());
        if (autenticado) {
            return ResponseEntity.ok("Login exitoso.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error de autenticación.");
        }
    }

    // Comprobación de existencia de usuario
    @GetMapping("/existe/{username}")
    public boolean usuarioExiste(@PathVariable String username) {
        return usuarioService.usuarioExiste(username);
    }
}
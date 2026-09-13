package es.p2.ufv.front.Register;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class UserService
{
    private final String BASE_URL = System.getenv().getOrDefault("BACKEND_URL", "http://localhost:8089")
            + "/api/usuarios";
    private final RestTemplate restTemplate = new RestTemplate();

    // Registro de usuario
    public boolean saveUser(User user)
    {
        try
        {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<User> request = new HttpEntity<>(user, headers);
            ResponseEntity<String> response = restTemplate.exchange(BASE_URL + "/registro", HttpMethod.POST, request, String.class);

            return response.getStatusCode() == HttpStatus.OK;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    // Validar usuario (login)
    public boolean validateUser(String username, String password) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            User user = new User(username, password);
            HttpEntity<User> request = new HttpEntity<>(user, headers);

            ResponseEntity<String> response = restTemplate.exchange(BASE_URL + "/login", HttpMethod.POST, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                String body = response.getBody();
                return body != null && body.contains("Login exitoso");
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    // Verificar si el usuario existe
    public boolean userExists(String username)
    {
        try
        {
            String url = BASE_URL + "/existe/" + username;
            ResponseEntity<Boolean> response = restTemplate.getForEntity(url, Boolean.class);
            return response.getBody() != null && response.getBody();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
}

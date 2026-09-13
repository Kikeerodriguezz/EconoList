package es.p2.ufv.front;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class CompareService
{
    private final RestTemplate restTemplate;

    public CompareService()
    {
        this.restTemplate = new RestTemplate();
    }

    /**
     * Llama al endpoint /find/compare?term=... y devuelve la respuesta como Map<String,Object>.
     */
    public Map<String, Object> comparePrices(String term)
    {
        try
        {
            // Codifica el término (por si contiene espacios o caracteres especiales)
            String encodedTerm = URLEncoder.encode(term, StandardCharsets.UTF_8);
            String backendUrl = System.getenv().getOrDefault("BACKEND_URL", "http://localhost:8089");
            String url = backendUrl + "/find/compare?term=" + encodedTerm;
            return restTemplate.getForObject(url, Map.class);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}

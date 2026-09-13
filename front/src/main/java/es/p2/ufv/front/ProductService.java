package es.p2.ufv.front;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;

@Service
public class ProductService
{
    private static final String BASE_URL = System.getenv().getOrDefault("BACKEND_URL", "http://localhost:8089")
            + "/find?term=";

    public List<Product> searchProducts(String term)
    {
        RestTemplate restTemplate = new RestTemplate();
        Product[] products = restTemplate.getForObject(BASE_URL + term, Product[].class);
        return Arrays.asList(products);
    }

    public Product findProductByNameAndSupermarket(String productName, String supermarket)
    {
        // 1. Llamamos a searchProducts para obtener todos los productos que coinciden con productName
        List<Product> products = searchProducts(productName);

        // 2. Filtramos la lista para buscar el primer producto que tenga el supermercado indicado
        return products.stream()
                .filter(p -> p.getSupermarket() != null
                        && p.getSupermarket().equalsIgnoreCase(supermarket))
                .findFirst()
                .orElse(null);
    }
}

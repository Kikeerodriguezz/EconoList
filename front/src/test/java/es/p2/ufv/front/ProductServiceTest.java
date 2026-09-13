package es.p2.ufv.front;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    private ProductService productService;

    @BeforeEach
    public void setUp() {
        productService = Mockito.spy(new ProductService());
    }

    @Test
    public void testFindProductByNameAndSupermarketReturnsCorrectProduct() {
        Product p1 = new Product();
        p1.setName("Atún");
        p1.setSupermarket("Mercadona");
        p1.setPrice(1.50);

        Product p2 = new Product();
        p2.setName("Atún");
        p2.setSupermarket("Carrefour");
        p2.setPrice(1.40);

        List<Product> mockList = Arrays.asList(p1, p2);
        doReturn(mockList).when(productService).searchProducts("atun");

        Product result = productService.findProductByNameAndSupermarket("atun", "Carrefour");

        assertNotNull(result);
        assertEquals("Carrefour", result.getSupermarket());
        assertEquals(1.40, result.getPrice());
    }

    @Test
    public void testFindProductByNameAndSupermarketReturnsNullIfNotFound() {
        Product p1 = new Product();
        p1.setName("Atún");
        p1.setSupermarket("Mercadona");
        p1.setPrice(1.50);

        List<Product> mockList = Arrays.asList(p1);
        doReturn(mockList).when(productService).searchProducts("atun");

        Product result = productService.findProductByNameAndSupermarket("atun", "Dia");

        assertNull(result);
    }

    @Test
    public void testFindProductByNameAndSupermarketHandlesNullSupermarket() {
        Product p1 = new Product();
        p1.setName("Atún");
        p1.setSupermarket(null);
        p1.setPrice(1.50);

        List<Product> mockList = Arrays.asList(p1);
        doReturn(mockList).when(productService).searchProducts("atun");

        Product result = productService.findProductByNameAndSupermarket("atun", "Mercadona");

        assertNull(result);  // no coincide porque el supermercado es null
    }
}

package es.p2.ufv.front;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OptimizationServiceTest {

    private OptimizationService optimizationService;
    private ProductService productServiceMock;

    @BeforeEach
    public void setUp() {
        productServiceMock = mock(ProductService.class);
        optimizationService = new OptimizationService(productServiceMock);
    }

    @Test
    public void testFindCheapestSupermarketReturnsCorrectOne() {
        Product patatas = new Product();
        patatas.setName("Patatas");
        patatas.setQuantity(2);

        List<Product> shoppingList = List.of(patatas);
        List<String> supermarkets = List.of("Mercadona", "Carrefour");

        // Simulamos precios por supermercado
        Product p1 = new Product(); p1.setName("Patatas"); p1.setPrice(1.00);
        Product p2 = new Product(); p2.setName("Patatas"); p2.setPrice(0.75);

        when(productServiceMock.findProductByNameAndSupermarket("Patatas", "Mercadona")).thenReturn(p1);
        when(productServiceMock.findProductByNameAndSupermarket("Patatas", "Carrefour")).thenReturn(p2);

        String result = optimizationService.findCheapestSupermarket(shoppingList, supermarkets);

        assertEquals("Carrefour", result);
    }

    @Test
    public void testFindCheapestSupermarketReturnsNullIfUnavailable() {
        Product huevos = new Product();
        huevos.setName("Huevos");
        huevos.setQuantity(1);

        List<Product> shoppingList = List.of(huevos);
        List<String> supermarkets = List.of("Dia");

        when(productServiceMock.findProductByNameAndSupermarket("Huevos", "Dia")).thenReturn(null);

        String result = optimizationService.findCheapestSupermarket(shoppingList, supermarkets);

        assertNull(result);
    }

    @Test
    public void testCalculateOptimizedTotalPrice() {
        Product arroz = new Product();
        arroz.setName("Arroz");
        arroz.setQuantity(2);

        Product leche = new Product();
        leche.setName("Leche");
        leche.setQuantity(1);

        List<Product> shoppingList = Arrays.asList(arroz, leche);
        List<String> supermarkets = Arrays.asList("Mercadona", "Carrefour");

        Product arrozBarato = new Product(); arrozBarato.setName("Arroz"); arrozBarato.setPrice(0.80);
        Product lecheBarata = new Product(); lecheBarata.setName("Leche"); lecheBarata.setPrice(1.00);

        when(productServiceMock.findProductByNameAndSupermarket("Arroz", "Mercadona")).thenReturn(arrozBarato);
        when(productServiceMock.findProductByNameAndSupermarket("Arroz", "Carrefour")).thenReturn(null);
        when(productServiceMock.findProductByNameAndSupermarket("Leche", "Mercadona")).thenReturn(null);
        when(productServiceMock.findProductByNameAndSupermarket("Leche", "Carrefour")).thenReturn(lecheBarata);

        double result = optimizationService.calculateOptimizedTotalPrice(shoppingList, supermarkets);

        // 2 * 0.80 + 1.00 = 2.60
        assertEquals(2.60, result, 0.01);
    }

    @Test
    public void testCalculateOptimizedTotalPriceWithUnavailableProduct() {
        Product cafe = new Product();
        cafe.setName("Café");
        cafe.setQuantity(1);

        List<Product> shoppingList = List.of(cafe);
        List<String> supermarkets = List.of("Gadis");

        when(productServiceMock.findProductByNameAndSupermarket("Café", "Gadis")).thenReturn(null);

        double result = optimizationService.calculateOptimizedTotalPrice(shoppingList, supermarkets);

        assertEquals(9999, result, 0.01);
    }
}

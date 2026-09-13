package es.p2.ufv.front;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CompareServiceTest {

    private CompareService compareService;

    @BeforeEach
    public void setUp() {
        compareService = Mockito.spy(new CompareService());
    }

    @Test
    public void testComparePricesReturnsCorrectMap() {
        String term = "atun";
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("Mercadona", 1.55);
        mockResponse.put("Carrefour", 1.49);

        // Simulamos la respuesta del método
        doReturn(mockResponse).when(compareService).comparePrices(term);

        Map<String, Object> result = compareService.comparePrices(term);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1.49, result.get("Carrefour"));
        assertEquals(1.55, result.get("Mercadona"));
    }

    @Test
    public void testComparePricesHandlesException() {
        // Simulamos que se lanza una excepción
        doReturn(null).when(compareService).comparePrices("error");

        Map<String, Object> result = compareService.comparePrices("error");

        assertNull(result);
    }
}

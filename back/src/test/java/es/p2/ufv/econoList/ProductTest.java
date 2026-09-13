package es.p2.ufv.econoList;

import es.p2.ufv.econoList.Modelo.Market;
import es.p2.ufv.econoList.Modelo.Product;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest
{
    @Test
    void testProductGettersAndSetters()
    {
        Product product = new Product();

        product.setMarket(Market.CARREFOUR);
        product.setBrand("Coca-Cola");
        product.setName("Coca-Cola Zero");
        product.setPrice(1.5f);
        product.setPriceUnitOrKg("€/L");
        product.setImage("image.jpg");
        product.setQuantity(3);

        assertEquals(Market.CARREFOUR, product.getMarket());
        assertEquals("Coca-Cola", product.getBrand());
        assertEquals("Coca-Cola Zero", product.getName());
        assertEquals(1.5f, product.getPrice());
        assertEquals("€/L", product.getPriceUnitOrKg());
        assertEquals("image.jpg", product.getImage());
        assertEquals(3, product.getQuantity());
    }

    @Test
    void testProductPriceFormat() {
        Product product = new Product();
        product.setPrice(2.3456f);

        assertEquals("2,35 €", product.getProductPrice());
    }

    @Test
    void testSupermarketNameFromMarketEnum() {
        Product product = new Product();
        product.setMarket(Market.MERCADONA);

        assertEquals("Mercadona", product.getSupermarket());
    }

    @Test
    void testSupermarketNull() {
        Product product = new Product();
        product.setMarket(null);

        assertNull(product.getSupermarket());
    }
}

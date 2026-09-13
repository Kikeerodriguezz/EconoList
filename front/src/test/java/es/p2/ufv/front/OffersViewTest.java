package es.p2.ufv.front;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OffersViewTest {



    @Test
    public void testRenderizaProductosAgrupados() {
        try (MockedConstruction<RestTemplate> restTemplateMock = Mockito.mockConstruction(RestTemplate.class,
                (mock, context) -> {
                    Product p1 = mock(Product.class);
                    when(p1.getName()).thenReturn("Leche");
                    when(p1.getBrand()).thenReturn("Pascual");
                    when(p1.getPrice()).thenReturn(1.20);
                    when(p1.getImage()).thenReturn("img/leche.jpg");
                    when(p1.getSupermarket()).thenReturn("Mercadona");

                    Product p2 = mock(Product.class);
                    when(p2.getName()).thenReturn("Huevos");
                    when(p2.getBrand()).thenReturn("Campero");
                    when(p2.getPrice()).thenReturn(2.50);
                    when(p2.getImage()).thenReturn("img/huevos.jpg");
                    when(p2.getSupermarket()).thenReturn("Mercadona");

                    when(mock.getForObject(anyString(), eq(Product[].class)))
                            .thenReturn(new Product[]{p1, p2});
                })) {

            OffersView view = new OffersView();

            boolean hayLayoutDeProductos = view.getChildren()
                    .anyMatch(c -> c instanceof FlexLayout);

            assertTrue(hayLayoutDeProductos);
        }
    }

    @Test
    public void testMensajeSinOfertas() {
        try (MockedConstruction<RestTemplate> restTemplateMock = Mockito.mockConstruction(RestTemplate.class,
                (mock, context) -> {
                    when(mock.getForObject(anyString(), eq(Product[].class)))
                            .thenReturn(new Product[0]);
                })) {

            OffersView view = new OffersView();

            boolean mensajePresente = view.getChildren()
                    .anyMatch(c -> c.getElement().getText().contains("No hay ofertas disponibles"));

            assertTrue(mensajePresente);
        }
    }
}

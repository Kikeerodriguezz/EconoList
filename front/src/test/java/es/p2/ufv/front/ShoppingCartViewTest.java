package es.p2.ufv.front;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.VaadinSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ShoppingCartViewTest {

    private ShoppingCartView cartView;
    private VaadinSession mockSession;
    private MockedStatic<VaadinSession> vaadinSessionMock;

    @BeforeEach
    public void setUp() {
        // Crear productos con setters
        Product leche = new Product();
        leche.setName("Leche");
        leche.setSupermarket("Carrefour");
        leche.setPrice(1.35);
        leche.setImage("leche.png");

        Product pan = new Product();
        pan.setName("Pan");
        pan.setSupermarket("Carrefour");
        pan.setPrice(0.85);
        pan.setImage("pan.png");

        List<Product> shoppingList = new ArrayList<>();
        shoppingList.add(leche);
        shoppingList.add(pan);

        // Mock de sesión
        mockSession = mock(VaadinSession.class);
        when(mockSession.getAttribute("shoppingList")).thenReturn(shoppingList);

        vaadinSessionMock = Mockito.mockStatic(VaadinSession.class);
        vaadinSessionMock.when(VaadinSession::getCurrent).thenReturn(mockSession);

        // Mock UI para navegación
        UI mockUI = mock(UI.class);
        UI.setCurrent(mockUI);

        cartView = new ShoppingCartView();
    }

    @Test
    public void testComponentesBasicosExisten() {
        assertTrue(cartView.getChildren().anyMatch(c -> c instanceof VerticalLayout));
    }

    @Test
    public void testTotalCalculadoCorrectamente() {
        Paragraph totalParagraph = cartView.getChildren()
                .flatMap(c -> {
                    if (c instanceof VerticalLayout) {
                        return ((VerticalLayout) c).getChildren();
                    }
                    return java.util.stream.Stream.empty();
                })
                .filter(c -> c instanceof Paragraph)
                .map(c -> (Paragraph) c)
                .filter(p -> p.getText().toLowerCase().contains("total"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Total no encontrado"));

        String totalText = totalParagraph.getText().replace(",", ".").replace("€", "").trim();

        double totalValue;
        try {
            // Buscar el número en el texto con expresión regular
            java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("([0-9]+(\\.[0-9]+)?)").matcher(totalText);
            if (matcher.find()) {
                totalValue = Double.parseDouble(matcher.group(1));
            } else {
                throw new AssertionError("No se encontró un número en el texto del total: " + totalText);
            }
        } catch (Exception e) {
            throw new AssertionError("Error al extraer el valor del total: " + totalText, e);
        }

        assertEquals(2.20, totalValue, 0.01);
    }








    @org.junit.jupiter.api.AfterEach
    public void tearDown() {
        if (vaadinSessionMock != null) {
            vaadinSessionMock.close();
        }
    }
}

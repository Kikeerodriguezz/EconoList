package es.p2.ufv.front;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.IFrame;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.VaadinSession;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ContactViewTest {

    private MockedStatic<VaadinSession> vaadinSessionMock;

    @BeforeAll
    public void setup() {
        VaadinSession session = Mockito.mock(VaadinSession.class);
        Mockito.when(session.getAttribute("loggedUser")).thenReturn(null);

        vaadinSessionMock = Mockito.mockStatic(VaadinSession.class);
        vaadinSessionMock.when(VaadinSession::getCurrent).thenReturn(session);
    }

    @AfterAll
    public void tearDown() {
        vaadinSessionMock.close();
    }

    @Test
    public void testInfoPanelContieneDatosContacto() {
        ContactView view = new ContactView();

        // El segundo hijo del ContactView es el layout que contiene los paneles
        HorizontalLayout contentLayout = (HorizontalLayout) view.getChildren().skip(1).findFirst().orElseThrow();

        VerticalLayout infoPanel = (VerticalLayout) contentLayout.getComponentAt(0);

        boolean contieneDireccion = infoPanel.getChildren().anyMatch(c -> c instanceof Paragraph && ((Paragraph) c).getText().contains("Francisco de Vitoria"));
        boolean contieneEmail = infoPanel.getChildren().anyMatch(c -> c instanceof Paragraph && ((Paragraph) c).getText().contains("info@econolist.com"));
        boolean contieneTelefono = infoPanel.getChildren().anyMatch(c -> c instanceof Paragraph && ((Paragraph) c).getText().contains("Teléfono"));

        assertTrue(contieneDireccion);
        assertTrue(contieneEmail);
        assertTrue(contieneTelefono);
    }

    @Test
    public void testInfoPanelTieneIconosSociales() {
        ContactView view = new ContactView();

        HorizontalLayout contentLayout = (HorizontalLayout) view.getChildren().skip(1).findFirst().orElseThrow();
        VerticalLayout infoPanel = (VerticalLayout) contentLayout.getComponentAt(0);

        // Buscar layout que contiene las imágenes
        long imagenes = infoPanel.getChildren()
                .filter(child -> child instanceof HorizontalLayout)
                .flatMap(child -> ((HorizontalLayout) child).getChildren())
                .filter(child -> child instanceof Image)
                .count();

        assertEquals(3, imagenes);
    }

    @Test
    public void testMapaVisible() {
        ContactView view = new ContactView();

        HorizontalLayout contentLayout = (HorizontalLayout) view.getChildren().skip(1).findFirst().orElseThrow();
        VerticalLayout mapaPanel = (VerticalLayout) contentLayout.getComponentAt(1);

        boolean contieneMapa = mapaPanel.getChildren().anyMatch(c -> c instanceof IFrame);
        assertTrue(contieneMapa);
    }
}

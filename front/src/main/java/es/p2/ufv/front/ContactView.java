package es.p2.ufv.front;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.util.Arrays;

@Route("contact")
public class ContactView extends VerticalLayout
{
    public ContactView()
    {
        // Estilo general
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        setAlignItems(Alignment.CENTER);

        // Fondo igual que en MainView
        getStyle().set("background-image", "url('images/fondoPagPrincipal.png')");
        getStyle().set("background-size", "cover");
        getStyle().set("background-position", "center");

        // NAVBAR igual que en MainView
        HorizontalLayout navbar = createNavbar();
        navbar.setWidthFull();
        navbar.getStyle().set("background", "white").set("padding", "10px");
        add(navbar);

        // PANEL con la información de contacto
        VerticalLayout infoPanel = new VerticalLayout();
        infoPanel.getStyle()
                .set("background-color", "white")
                .set("border-radius", "20px")
                .set("box-shadow", "0 4px 15px rgba(0,0,0,0.1)")
                .set("padding", "30px")
                .set("width", "350px");
        infoPanel.setSpacing(true);
        infoPanel.setAlignItems(Alignment.START);

        Paragraph address = new Paragraph("📍 Universidad Francisco de Vitoria, Madrid");
        Paragraph email = new Paragraph("✉️ Contacto: info@econolist.com");
        Paragraph phone = new Paragraph("📞 Teléfono: +34 123 456 789");

        Image facebook = new Image("images/facebook.png", "Facebook");
        Image twitter = new Image("images/twitter.png", "Twitter");
        Image instagram = new Image("images/instagram.png", "Instagram");

        for (Image icon : new Image[]{facebook, twitter, instagram})
        {
            icon.setWidth("40px");
            icon.setHeight("40px");
        }

        HorizontalLayout socialMedia = new HorizontalLayout(facebook, twitter, instagram);
        socialMedia.setSpacing(true);

        infoPanel.add(address, email, phone, socialMedia);

        // PANEL con el mapa
        VerticalLayout mapPanel = new VerticalLayout();
        mapPanel.getStyle()
                .set("background-color", "white")
                .set("border-radius", "20px")
                .set("box-shadow", "0 4px 15px rgba(0,0,0,0.1)")
                .set("padding", "20px")
                .set("width", "600px");

        IFrame map = new IFrame("https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d3036.519049431812!2d-3.832998524253339!3d40.44896227142171!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0xd4185c36118a46b%3A0x1294dd72d1e62c70!2sUniversidad%20Francisco%20de%20Vitoria!5e0!3m2!1ses!2ses!4v1710714389747!5m2!1ses!2ses");
        map.setWidth("100%");
        map.setHeight("400px");
        map.getStyle().set("border", "0");

        mapPanel.add(map);

        // Distribución horizontal de los dos paneles
        HorizontalLayout contentLayout = new HorizontalLayout(infoPanel, mapPanel);
        contentLayout.setSpacing(true);
        contentLayout.setAlignItems(Alignment.START);
        contentLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        contentLayout.setWidthFull();
        contentLayout.getStyle().set("margin-top", "40px");

        add(contentLayout);
    }

    private HorizontalLayout createNavbar()
    {
        String loggedUser = (String) VaadinSession.getCurrent().getAttribute("loggedUser");
        Button menuButton = new Button(VaadinIcon.MENU.create(), e -> openMenu());
        menuButton.getStyle().set("margin-left", "20px");

        Image logo = new Image("images/LogoEL.png", "EconoList Logo");
        logo.setHeight("60px");

        HorizontalLayout left = new HorizontalLayout(menuButton);
        HorizontalLayout center = new HorizontalLayout(logo);

        HorizontalLayout right = new HorizontalLayout();
        if (loggedUser != null)
        {
            Span greetingLabel = new Span("Hola, " + loggedUser + "!");
            greetingLabel.getStyle().set("font-size", "20px").set("color", "#333");
            greetingLabel.getStyle().set("margin-top", "auto").set("margin-bottom", "0");

            Button logoutButton = new Button("Cerrar sesión", event -> {
                VaadinSession.getCurrent().setAttribute("loggedUser", null);
                getUI().ifPresent(ui -> ui.navigate("login"));
            });
            logoutButton.getStyle().set("font-size", "12px").set("background-color", "#dc3545").set("color", "white");

            Button cartButton = new Button(VaadinIcon.CART.create(), e -> getUI().ifPresent(ui -> ui.navigate("cart")));
            cartButton.getStyle().set("font-size", "16px");

            right.add(greetingLabel, logoutButton, cartButton);
        }
        else
        {
            Button registerButton = new Button("Registrarse", e -> getUI().ifPresent(ui -> ui.navigate("register")));
            registerButton.getStyle().set("background-color", "#83C28F").set("color", "white").set("font-weight", "bold");

            Button cartButton = new Button(VaadinIcon.CART.create(), e -> getUI().ifPresent(ui -> ui.navigate("cart")));
            cartButton.getStyle().set("margin-right", "20px");

            right.add(registerButton, cartButton);
        }

        left.setWidth("33%");
        center.setWidth("34%");
        center.setJustifyContentMode(JustifyContentMode.CENTER);
        right.setWidth("33%");
        right.setJustifyContentMode(JustifyContentMode.END);

        HorizontalLayout navbar = new HorizontalLayout(left, center, right);
        navbar.setWidthFull();
        navbar.setPadding(true);
        navbar.getStyle().set("background-color", "white").set("box-shadow", "0 4px 10px rgba(0,0,0,0.3)");
        navbar.setAlignItems(Alignment.CENTER);

        return navbar;
    }

    private void openMenu()
    {
        Dialog menuDialog = new Dialog();
        menuDialog.setModal(true);
        menuDialog.setDraggable(false);
        menuDialog.setResizable(false);
        menuDialog.setCloseOnOutsideClick(true);
        menuDialog.setWidth("auto");
        menuDialog.setHeight("100vh");

        menuDialog.getElement().getStyle()
                .set("padding", "0")
                .set("margin", "0")
                .set("background", "transparent")
                .set("box-shadow", "none")
                .set("overflow", "hidden");

        VerticalLayout menuLayout = new VerticalLayout();
        menuLayout.setPadding(true);
        menuLayout.setSpacing(true);
        menuLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        menuLayout.setHeightFull();
        menuLayout.setWidth("250px");
        menuLayout.getStyle()
                .set("background-color", "white")
                .set("box-shadow", "4px 0 12px rgba(0,0,0,0.2)")
                .set("overflow", "hidden");

        Button inicioBtn = new Button("Inicio", e -> navigateAndClose(menuDialog, ""));
        Button compararBtn = new Button("Comparador", e -> navigateAndClose(menuDialog, "compare"));
        Button ofertasBtn = new Button("Ofertas", e -> navigateAndClose(menuDialog, "offers"));
        Button contactoBtn = new Button("Contacto", e -> navigateAndClose(menuDialog, "contact"));

        for (Button btn : Arrays.asList(inicioBtn, compararBtn, ofertasBtn, contactoBtn))
        {
            btn.getStyle()
                    .set("background-color", "#83C28F")
                    .set("color", "white")
                    .set("font-weight", "bold")
                    .set("width", "100%")
                    .set("border-radius", "8px");
        }

        menuLayout.add(inicioBtn, compararBtn, ofertasBtn, contactoBtn);
        menuDialog.add(menuLayout);

        menuDialog.getElement().executeJs("""
        const overlay = this.$.overlay;
        overlay.style.position = 'fixed';
        overlay.style.top = '0';
        overlay.style.left = '0';
        overlay.style.height = '100vh';
        overlay.style.width = '250px';
        overlay.style.maxWidth = '250px';
        overlay.style.borderRadius = '0';
        overlay.style.margin = '0';
        overlay.style.padding = '0';
        overlay.style.background = 'transparent';
        overlay.style.boxShadow = 'none';
        overlay.style.overflow = 'hidden';
        """);

        menuDialog.open();
    }

    private void navigateAndClose(Dialog dialog, String route)
    {
        dialog.close();
        getUI().ifPresent(ui -> ui.navigate(route));
    }
}

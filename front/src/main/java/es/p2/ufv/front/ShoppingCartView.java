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
import java.util.Map;
import java.util.stream.Collectors;


import java.util.ArrayList;
import java.util.List;

@Route("cart")
public class ShoppingCartView extends VerticalLayout
{
    private List<Product> shoppingList;
    private VerticalLayout productListLayout;
    private Paragraph totalPriceLabel;

    public ShoppingCartView()
    {
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        getStyle().set("background-image", "url('images/fondoPagPrincipal.png')")
                .set("background-size", "cover")
                .set("background-position", "center")
                .set("min-height", "100vh");

        // NAVBAR - fijo arriba
        HorizontalLayout navbar = createNavbar();
        navbar.getStyle()
                .set("position", "fixed")
                .set("top", "0")
                .set("left", "0")
                .set("right", "0")
                .set("z-index", "1000");

        // CONTENEDOR DE LA CESTA
        VerticalLayout cartContent = createCartContent();
        cartContent.getStyle()
                .set("margin-top", "190px"); // espacio debajo del navbar

        // Añadimos navbar y contenido centrado
        add(navbar, cartContent);
        setHorizontalComponentAlignment(Alignment.CENTER, cartContent);
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
            registerButton.addClassName("register-button");

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
        navbar.setPadding(false); // ← más compacto
        navbar.setAlignItems(Alignment.CENTER);
        navbar.getStyle()
                .set("background-color", "white")
                .set("box-shadow", "0 4px 10px rgba(0,0,0,0.3)")
                .set("height", "80px") // ← altura fija igual al inicio
                .set("padding", "10px 20px"); // ← padding horizontal solo, como en MainView

        return navbar;
    }

    private void openMenu()
    {
        Dialog menuDialog = new Dialog();
        VerticalLayout menuLayout = new VerticalLayout(
                new Button("Inicio", e -> getUI().ifPresent(ui -> ui.navigate(""))),
                new Button("Comparador", e -> getUI().ifPresent(ui -> ui.navigate("compare"))),
                new Button("Ofertas", e -> getUI().ifPresent(ui -> ui.navigate("offers"))),
                new Button("Contacto", e -> getUI().ifPresent(ui -> ui.navigate("contact")))
        );
        menuDialog.add(menuLayout);
        menuDialog.setWidth("250px");
        menuDialog.open();
    }

    private VerticalLayout createCartContent()
    {
        VerticalLayout container = new VerticalLayout();
        container.setWidth("60%");
        container.setMaxWidth("600px");
        container.setAlignItems(Alignment.CENTER);
        container.getStyle()
                .set("background-color", "#ffffff")
                .set("border-radius", "20px") // bordes más redondeados
                .set("padding", "30px")
                .set("box-shadow", "0px 8px 16px rgba(0, 0, 0, 0.1)"); // sombra suave

        H2 title = new H2("Tu cesta de la compra");
        title.getStyle()
                .set("text-align", "center")
                .set("width", "100%")
                .set("color", "#83C28F"); // título verde

        shoppingList = (List<Product>) VaadinSession.getCurrent().getAttribute("shoppingList");
        if (shoppingList == null)
        {
            shoppingList = new ArrayList<>();
            VaadinSession.getCurrent().setAttribute("shoppingList", shoppingList);
        }

        productListLayout = new VerticalLayout();
        productListLayout.setSpacing(true);
        productListLayout.setPadding(true);
        productListLayout.setWidthFull();
        productListLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        if (shoppingList.isEmpty())
        {
            Paragraph emptyMsg = new Paragraph("Tu cesta está vacía.");
            emptyMsg.getStyle().set("color", "#333333"); // gris oscuro
            productListLayout.add(emptyMsg);
        }
        else
        {
            updateShoppingCart();
        }

        totalPriceLabel = new Paragraph();
        totalPriceLabel.getStyle()
                .set("font-weight", "bold")
                .set("font-size", "18px")
                .set("color", "#333333"); // gris oscuro
        updateTotalPrice();

        Button clearCartButton = new Button("Vaciar Cesta", e -> clearCart());
        clearCartButton.getStyle()
                .set("background-color", "#83C28F")
                .set("color", "white");

        Button backButton = new Button("Volver", e -> getUI().ifPresent(ui -> ui.navigate("")));
        backButton.getStyle()
                .set("background-color", "#83C28F")
                .set("color", "white");

        HorizontalLayout buttonLayout = new HorizontalLayout(clearCartButton, backButton);
        buttonLayout.setSpacing(true);

        container.add(title, productListLayout, totalPriceLabel, buttonLayout);
        return container;
    }

    private void updateShoppingCart()
    {
        productListLayout.removeAll();

        // Agrupar productos por supermercado
        Map<String, List<Product>> productosPorSuper = shoppingList.stream()
                .collect(Collectors.groupingBy(Product::getSupermarket));

        for (String supermercado : productosPorSuper.keySet())
        {
            List<Product> productos = productosPorSuper.get(supermercado);

            H3 supermercadoHeader = new H3("🛒 " + supermercado);
            supermercadoHeader.getStyle()
                    .set("color", "#83C28F")
                    .set("margin-top", "20px")
                    .set("margin-bottom", "10px");
            productListLayout.add(supermercadoHeader);

            for (Product product : productos)
            {
                HorizontalLayout itemLayout = new HorizontalLayout();
                itemLayout.setWidth("100%");
                itemLayout.setAlignItems(FlexComponent.Alignment.CENTER);
                itemLayout.getStyle()
                        .set("border", "1px solid #ddd")
                        .set("border-radius", "10px")
                        .set("padding", "10px")
                        .set("margin-bottom", "5px")
                        .set("background-color", "#f9f9f9");

                Image img = new Image(product.getImage(), "Imagen del producto");
                img.setHeight("60px");
                img.getStyle().set("border-radius", "5px");

                VerticalLayout infoLayout = new VerticalLayout();
                infoLayout.setSpacing(false);
                infoLayout.add(
                        new Paragraph(product.getName()),
                        new Paragraph("💰 " + product.getPrice() + "€")
                );
                infoLayout.setWidth("70%");

                Button removeButton = new Button("Eliminar", e -> {
                    shoppingList.remove(product);
                    VaadinSession.getCurrent().setAttribute("shoppingList", shoppingList);
                    updateShoppingCart();
                    updateTotalPrice();
                });
                removeButton.getStyle()
                        .set("color", "white")
                        .set("background-color", "#ff4d4d");

                itemLayout.add(img, infoLayout, removeButton);
                productListLayout.add(itemLayout);
            }
        }
    }


    private void updateTotalPrice() {
        double total = shoppingList.stream()
                .mapToDouble(Product::getPrice)
                .sum();
        totalPriceLabel.setText("Total: " + String.format("%.2f", total) + "€");
    }

    private void clearCart() {
        shoppingList.clear();
        VaadinSession.getCurrent().setAttribute("shoppingList", shoppingList);
        updateShoppingCart();
        updateTotalPrice();
    }
}

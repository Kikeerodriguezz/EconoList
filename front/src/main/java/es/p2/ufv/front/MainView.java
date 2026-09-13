package es.p2.ufv.front;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Route("")
public class MainView extends VerticalLayout
{
    private final ProductService productService;

    @Autowired
    private OptimizationService optimizationService;

    private FlexLayout productContainer;
    private TextField searchField;
    private List<Product> shoppingList;
    private Dialog shoppingCartDialog;
    private VerticalLayout shoppingCartLayout;
    private VerticalLayout centerContent;


    @Autowired
    public MainView(ProductService productService)
    {
        this.productService = productService;
        this.shoppingList = new ArrayList<>();

        setSizeFull();
        setPadding(false);
        setSpacing(false);
        setAlignItems(Alignment.CENTER);

        getStyle().set("background-image", "url('images/fondoPagPrincipal.png')");
        getStyle().set("background-size", "cover");
        getStyle().set("background-position", "center");

        HorizontalLayout navbar = createNavbar();
        navbar.setWidthFull();
        navbar.getStyle().set("background", "white").set("padding", "10px");

        VerticalLayout searchSection = createSearchSection();

        productContainer = new FlexLayout();
        productContainer.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        productContainer.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        productContainer.setAlignItems(FlexComponent.Alignment.START);
        productContainer.setWidthFull();
        productContainer.getStyle()
                .set("padding", "20px")
                .set("gap", "20px");

        productContainer.setVisible(false);


        createShoppingCartDialog();

        Div backgroundWrapper = new Div();
        backgroundWrapper.addClassName("main-view-background");
        backgroundWrapper.setWidthFull();
        backgroundWrapper.getStyle()
                .set("padding-top", "120px");

        centerContent = new VerticalLayout(searchSection);
        centerContent.addClassName("centered-section");
        centerContent.setWidthFull();
        centerContent.getStyle()
                .set("display", "flex")
                .set("flex-direction", "column")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("height", "80vh");
// Para ocupar casi toda la pantalla
        backgroundWrapper.add(centerContent, productContainer);
        add(navbar, backgroundWrapper);
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

            Button cartButton = new Button(VaadinIcon.CART.create(), e -> {
                getUI().ifPresent(ui -> ui.navigate("cart"));
            });

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

        // Eliminar márgenes y fondo internos del Dialog
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

        // Botones
        Button inicioBtn = new Button("Inicio", e -> navigateAndClose(menuDialog, ""));
        Button compararBtn = new Button("Comparador", e -> navigateAndClose(menuDialog, "compare"));
        Button ofertasBtn = new Button("Ofertas", e -> navigateAndClose(menuDialog, "offers"));
        Button contactoBtn = new Button("Contacto", e -> navigateAndClose(menuDialog, "contact"));
        Button listaBtn = new Button("Lista de Compra", e -> navigateAndClose(menuDialog, "shoplist"));

        for (Button btn : Arrays.asList(inicioBtn, compararBtn, ofertasBtn, contactoBtn))
        {
            btn.getStyle()
                    .set("background-color", "#83C28F")
                    .set("color", "white")
                    .set("font-weight", "bold")
                    .set("width", "100%")
                    .set("border-radius", "8px");
        }

        menuLayout.add(inicioBtn, compararBtn, listaBtn, ofertasBtn, contactoBtn);
        menuDialog.add(menuLayout);

        // Ajustar el overlay para que no cree marco alrededor del panel
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

    private void createShoppingCartDialog()
    {
        shoppingCartDialog = new Dialog();
        shoppingCartDialog.setWidth("400px");
        shoppingCartDialog.setCloseOnOutsideClick(true);

        shoppingCartLayout = new VerticalLayout();
        shoppingCartLayout.setPadding(true);
        shoppingCartLayout.setSpacing(true);

        Button closeButton = new Button("Cerrar", e -> shoppingCartDialog.close());
        shoppingCartLayout.add(closeButton);

        shoppingCartDialog.add(shoppingCartLayout);
    }

    private void updateShoppingCart()
    {
        shoppingCartLayout.removeAll();
        Button closeButton = new Button("Cerrar", e -> shoppingCartDialog.close());
        shoppingCartLayout.add(closeButton);

        for (Product product : shoppingList)
        {
            HorizontalLayout itemLayout = new HorizontalLayout();

            Image img = new Image(product.getImage() != null ? product.getImage() : "default.png", "Imagen");
            img.setWidth("50px");
            img.setHeight("50px");

            VerticalLayout info = new VerticalLayout(
                    new Paragraph(product.getName()),
                    new Paragraph("💰 " + product.getProductPrice()),
                    new Paragraph("🏬 " + product.getSupermarket())
            );
            info.setSpacing(false);

            NumberField quantityField = new NumberField();
            quantityField.setLabel("Cantidad");
            quantityField.setMin(1);
            quantityField.setStep(1);
            quantityField.setValue((double) product.getQuantity());

            quantityField.addValueChangeListener(ev -> product.setQuantity(ev.getValue().intValue()));

            Button removeButton = new Button("❌", e -> {
                shoppingList.remove(product);
                updateShoppingCart();
            });

            itemLayout.add(img, info, quantityField, removeButton);
            shoppingCartLayout.add(itemLayout);
        }
    }

    private VerticalLayout createSearchSection()
    {
        H2 slogan = new H2("¡Más ahorro, menos drama!");
        slogan.addClassName("slogan-text");
        searchField = new TextField();
        searchField.setPlaceholder("Buscar productos...");
        searchField.setWidthFull();
        searchField.setClassName("styled-search-field");

        Button searchButton = new Button(VaadinIcon.SEARCH.create());
        searchButton.setClassName("styled-search-button");
        searchButton.addClickListener(e -> searchProducts());

        HorizontalLayout searchWrapper = new HorizontalLayout(searchField, searchButton);
        searchWrapper.setClassName("search-wrapper");
        searchWrapper.setWidth("400px");
        searchWrapper.setAlignItems(Alignment.CENTER);

        VerticalLayout searchSection = new VerticalLayout(slogan, searchWrapper);
        searchSection.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);
        return searchSection;
    }

    private void searchProducts()
    {
        String term = searchField.getValue();
        if (!term.isEmpty())
        {
            List<Product> products = productService.searchProducts(term);
            productContainer.removeAll();

            if (!products.isEmpty())
            {
                // Si hay productos, hacemos que centerContent tenga altura pequeña y no centrado
                centerContent.getStyle()
                        .set("height", "auto")
                        .set("justify-content", "start")
                        .set("margin-top", "40px");
            }

            for (Product product : products)
            {
                productContainer.add(createProductCard(product));
            }
            productContainer.setVisible(true);
        }
    }


    private VerticalLayout createProductCard(Product product)
    {
        Image img = new Image(product.getImage() != null ? product.getImage() : "default.png", "Imagen del producto");
        img.setHeight("120px");
        img.getStyle().set("object-fit", "contain");

        Paragraph name = new Paragraph(product.getName());
        name.getStyle()
                .set("font-weight", "bold")
                .set("text-align", "center")
                .set("margin", "0")
                .set("max-height", "3.5em")
                .set("overflow", "hidden")
                .set("white-space", "normal")
                .set("text-overflow", "ellipsis");

        Paragraph price = new Paragraph("💰 " + String.format("%.2f€", product.getPrice()));
        Paragraph supermarket = new Paragraph("🏬 " + product.getSupermarket());

        Button addToCartButton = new Button("Añadir a la cesta", e -> {
            List<Product> sessionShoppingList = (List<Product>) VaadinSession.getCurrent().getAttribute("shoppingList");
            if (sessionShoppingList == null) sessionShoppingList = new ArrayList<>();

            product.setQuantity(1);
            sessionShoppingList.add(product);
            VaadinSession.getCurrent().setAttribute("shoppingList", sessionShoppingList);

            shoppingList = sessionShoppingList;
            updateShoppingCart();

            // 🔥 NUEVO: mostrar notificación
            Notification.show("✅ Producto añadido correctamente", 3000, Notification.Position.TOP_CENTER);
        });
        addToCartButton.setWidthFull();
        addToCartButton.getStyle()
                .set("background-color", "#83C28F")
                .set("color", "white")
                .set("font-weight", "bold")
                .set("margin-top", "auto");

        VerticalLayout card = new VerticalLayout(img, name, price, supermarket, addToCartButton);
        card.setPadding(true);
        card.setSpacing(false);
        card.setAlignItems(Alignment.CENTER);
        card.setWidth("220px");
        card.setHeight("400px");
        card.getStyle()
                .set("background-color", "white")
                .set("border", "1px solid #ddd")
                .set("border-radius", "10px")
                .set("box-shadow", "0 4px 8px rgba(0, 0, 0, 0.1)")
                .set("display", "flex")
                .set("flex-direction", "column")
                .set("justify-content", "space-between")
                .set("overflow", "hidden");

        return card;
    }
}
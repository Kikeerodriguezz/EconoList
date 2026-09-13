package es.p2.ufv.front;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Route("offers")
@PermitAll
public class OffersView extends VerticalLayout
{
    private final String backendUrl = System.getenv().getOrDefault("BACKEND_URL", "http://localhost:8089")
            + "/api/offers/all";

    public OffersView()
    {
        // Estilo general
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setSizeFull();
        setSpacing(true);
        setPadding(true);

        getStyle().set("background-image", "url('images/fondoPagPrincipal.png')")
                .set("background-size", "auto")
                .set("background-repeat", "repeat")
                .set("background-position", "top center")
                .set("background-attachment", "fixed");  // Opcional: efecto parallax si quieres

        // Cabecera dentro de una tarjeta blanca semitransparente
        Div headerCard = new Div();
        headerCard.getStyle()
                .set("background", "rgba(255, 255, 255, 0.8)")  // Fondo blanco translúcido
                .set("padding", "30px")
                .set("border-radius", "15px")
                .set("box-shadow", "0 8px 20px rgba(0,0,0,0.2)")
                .set("text-align", "center")
                .set("max-width", "600px")
                .set("margin", "30px auto");

        // Título
        H1 title = new H1("🛒 Ofertas del Día");
        title.getStyle()
                .set("font-size", "2.8rem")
                .set("color", "#4CAF50")  // Verde principal (#83C28F, o este #4CAF50)
                .set("margin", "0 0 10px 0");

        // Descripción
        Paragraph description = new Paragraph("¡Descubre las mejores gangas frescas y oportunidades en tu supermercado favorito!");
        description.getStyle()
                .set("font-size", "1.2rem")
                .set("color", "#333")
                .set("margin", "10px 0 20px 0");

        // Botón de volver
        Button backButton = new Button("⬅️ Volver a Inicio", e -> getUI().ifPresent(ui -> ui.navigate("")));
        backButton.getStyle()
                .set("background-color", "#83C28F")
                .set("color", "white")
                .set("font-weight", "bold")
                .set("font-size", "1rem")
                .set("padding", "10px 20px")
                .set("border-radius", "8px");

        headerCard.add(title, description, backButton);

        // Añadir la cabecera al layout
        add(headerCard);

        // Obtener productos
        List<Product> offers = fetchOffersFromBackend();

        if (offers == null || offers.isEmpty())
        {
            Paragraph emptyMsg = new Paragraph("🚫 No hay ofertas disponibles en este momento.");
            emptyMsg.getStyle().set("color", "#b71c1c");
            emptyMsg.getStyle().set("font-weight", "bold");
            add(emptyMsg);
        }
        else
        {
            // Agrupar por supermercado
            Map<String, List<Product>> groupedBySupermarket = offers.stream()
                    .collect(Collectors.groupingBy(Product::getSupermarket));

            Random rand = new Random();

            for (Map.Entry<String, List<Product>> entry : groupedBySupermarket.entrySet()) {
                String supermarket = entry.getKey();
                List<Product> products = entry.getValue();

                // Elegir entre 5 y 10 productos aleatorios para simular descuentos
                int offerCount = Math.min(products.size(), rand.nextInt(6) + 5); // 5-10 productos
                Collections.shuffle(products);
                List<Product> discounted = products.subList(0, offerCount);

                for (Product p : discounted)
                {
                    double original = p.getPrice();
                    double discountPercent = 0.1 + (0.3 * rand.nextDouble()); // 10%-40%
                    double newPrice = Math.round((original * (1 - discountPercent)) * 100.0) / 100.0;

                    p.setOriginalPrice(original);
                    p.setPrice(newPrice);
                }

                // Título del supermercado
                H3 supermarketTitle = new H3("🛍️ Ofertas en " + supermarket.toUpperCase());
                supermarketTitle.getStyle().set("color", "#1976d2");
                supermarketTitle.getStyle().set("margin-top", "30px");
                add(supermarketTitle);

                // Contenedor de tarjetas
                FlexLayout productLayout = new FlexLayout();
                productLayout.setWidthFull();
                productLayout.setJustifyContentMode(FlexLayout.JustifyContentMode.CENTER);
                productLayout.setFlexWrap(FlexLayout.FlexWrap.WRAP);

                for (Product product : discounted)
                {
                    VerticalLayout card = new VerticalLayout();
                    card.setWidth("260px");
                    card.setPadding(false);
                    card.setSpacing(false);
                    card.getStyle().set("margin", "10px");
                    card.getStyle().set("border-radius", "12px");
                    card.getStyle().set("box-shadow", "0 4px 12px rgba(0,0,0,0.1)");
                    card.getStyle().set("background", "white");
                    card.getStyle().set("overflow", "hidden");

                    Image image = new Image(product.getImage(), product.getName());
                    image.setWidth("100%");
                    image.setHeight("180px");
                    image.getStyle().set("object-fit", "cover");

                    Div content = new Div();
                    content.getStyle().set("padding", "1em");

                    H4 name = new H4(product.getName());
                    name.getStyle().set("margin", "0");

                    Paragraph brand = new Paragraph("Marca: " + (product.getBrand() != null ? product.getBrand() : "-"));
                    brand.getStyle().set("margin", "0.2em 0");
                    brand.getStyle().set("font-size", "0.9rem");
                    brand.getStyle().set("color", "#666");

                    Paragraph oldPrice = new Paragraph("Antes: " + String.format("%.2f €", product.getOriginalPrice()));
                    oldPrice.getStyle().set("text-decoration", "line-through");
                    oldPrice.getStyle().set("color", "#999");
                    oldPrice.getStyle().set("margin", "0.2em 0");

                    Paragraph price = new Paragraph("💶 " + product.getPrice() + " €");
                    price.getStyle().set("font-weight", "bold");
                    price.getStyle().set("color", "#388e3c");
                    price.getStyle().set("font-size", "1.1rem");
                    price.getStyle().set("margin", "0.5em 0");

                    content.add(name, brand, oldPrice, price);
                    card.add(image, content);
                    productLayout.add(card);
                }
                add(productLayout);
            }
        }
    }

    private List<Product> fetchOffersFromBackend()
    {
        try
        {
            RestTemplate restTemplate = new RestTemplate();
            Product[] products = restTemplate.getForObject(backendUrl, Product[].class);
            return Arrays.asList(products);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}

package es.p2.ufv.front;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;
import java.util.stream.Collectors;
import com.vaadin.flow.component.orderedlayout.FlexComponent;


import java.util.Arrays;
import java.util.List;

@Route("compare")
public class CompareView extends VerticalLayout
{
    @Autowired
    private ProductService productService;

    public CompareView()
    {
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        setAlignItems(Alignment.CENTER);

        getStyle()
                .set("background-image", "url('images/fondoPagPrincipal.png')")
                .set("background-repeat", "repeat")
                .set("background-position", "top center")
                .set("background-size", "auto");

        VerticalLayout contentBox = new VerticalLayout();
        contentBox.setWidth("600px");
        contentBox.setPadding(true);
        contentBox.setSpacing(true);
        contentBox.setAlignItems(Alignment.CENTER);
        contentBox.getStyle()
                .set("background-color", "white")
                .set("border-radius", "16px")
                .set("box-shadow", "0 4px 16px rgba(0,0,0,0.1)")
                .set("margin-top", "60px")
                .set("margin-bottom", "60px")
                .set("padding", "40px");

        H1 title = new H1("Comparador de Precios");
        title.getStyle()
                .set("color", "#83C28F")
                .set("margin-bottom", "10px")
                .set("font-weight", "bold");

        Paragraph description = new Paragraph("Selecciona un producto para ver en qué supermercado está más barato.");
        description.getStyle().set("text-align", "center").set("color", "#555");

        VerticalLayout productGrid = createProductGrid();

        Button backButton = new Button("Volver", e -> getUI().ifPresent(ui -> ui.navigate("")));
        backButton.getStyle()
                .set("background-color", "#dc3545")
                .set("color", "white")
                .set("font-weight", "bold")
                .set("border-radius", "8px");

        contentBox.add(title, description, productGrid, backButton);
        add(contentBox);
    }

    private VerticalLayout createProductGrid()
    {
        List<String> products = Arrays.asList("Patatas", "Huevos", "Leche", "Atún", "Pollo", "Arroz", "Pasta", "Yogur", "Maiz", "Aceite", "Harina", "Cerveza", "Azucar", "Detergente", "Gel", "Champu", "Dentífrico", "Cereales", "Cafe", "Zumo", "Pan", "Chocolate", "Lejia", "Miel");
        VerticalLayout gridLayout = new VerticalLayout();
        gridLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        gridLayout.setSpacing(true);

        HorizontalLayout row = new HorizontalLayout();
        row.setSpacing(true);

        for (String product : products)
        {
            // Icono PNG con el nombre del producto en minúsculas
            Image icon = new Image("images/" + product.toLowerCase() + ".png", product);
            icon.setWidth("50px");
            icon.setHeight("50px");
            icon.getStyle().set("filter", "invert(1)");

            Span label = new Span(product);
            label.getStyle().set("font-weight", "bold").set("font-size", "14px");

            VerticalLayout buttonContent = new VerticalLayout(icon, label);
            buttonContent.setSpacing(false);
            buttonContent.setPadding(false);
            buttonContent.setAlignItems(Alignment.CENTER);

            Button productButton = new Button(buttonContent);
            productButton.setWidth("150px");
            productButton.setHeight("120px");
            productButton.getStyle()
                    .set("background-color", "#83C28F")
                    .set("color", "white")
                    .set("border-radius", "12px")
                    .set("font-weight", "bold")
                    .set("box-shadow", "0 4px 8px rgba(0,0,0,0.1)");

            productButton.addClickListener(e -> {
                List<Product> result = productService.searchProducts(product);
                if (result == null || result.isEmpty()) {
                    Notification.show("No se encontraron resultados para: " + product);
                    return;
                }
                mostrarResultados(product, result);
            });

            row.add(productButton);

            if (row.getComponentCount() == 3)
            {
                gridLayout.add(row);
                row = new HorizontalLayout();
                row.setSpacing(true);
            }
        }

        if (!row.getChildren().toList().isEmpty())
        {
            gridLayout.add(row);
        }
        return gridLayout;
    }


    private void mostrarResultados(String productName, List<Product> productos)
    {
        Dialog dialog = new Dialog();
        dialog.setWidth("90%");
        dialog.setHeight("90%");
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(true);

        VerticalLayout dialogContent = new VerticalLayout();
        dialogContent.setSizeFull();
        dialogContent.setSpacing(true);
        dialogContent.setPadding(true);

        H2 title = new H2("Resultados para: " + productName);
        title.getStyle().set("color", "#83C28F");

        // Agrupar por supermercado
        Map<String, List<Product>> grouped = productos.stream()
                .sorted((p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice()))
                .collect(Collectors.groupingBy(Product::getSupermarket));


        Div supermercadoRow = new Div();
        supermercadoRow.getStyle()
                .set("display", "flex")
                .set("flex-direction", "row")
                .set("justify-content", "center")
                .set("flex-wrap", "wrap")
                .set("gap", "20px")
                .set("padding", "10px")
                .set("max-width", "100%");

        for (String market : grouped.keySet())
        {
            VerticalLayout column = new VerticalLayout();
            column.setWidth("250px");
            column.setSpacing(true);
            column.setPadding(false);
            column.setAlignItems(Alignment.CENTER);  // Opcional, mejora el centrado
            column.getStyle()
                    .set("background-color", "#f8f9fa")
                    .set("border-radius", "12px")
                    .set("padding", "15px")
                    .set("box-shadow", "0 4px 8px rgba(0,0,0,0.1)")
                    .set("display", "flex")
                    .set("flex-direction", "column")
                    .set("flex-grow", "1")    // ⬅️ permite que crezca verticalmente
                    .set("height", "auto");   // ⬅️ asegura que se ajuste al contenido


            H3 marketTitle = new H3(market);
            marketTitle.getStyle().set("color", "#28a745");

            column.add(marketTitle);

            for (Product product : grouped.get(market))
            {
                VerticalLayout card = new VerticalLayout();
                card.setSpacing(false);
                card.setPadding(false);
                card.setWidthFull();
                card.getStyle()
                        .set("background-color", "white")
                        .set("border-radius", "10px")
                        .set("border", "1px solid #ddd")
                        .set("box-shadow", "0 2px 6px rgba(0,0,0,0.05)")
                        .set("padding", "10px");

                Image img = new Image(product.getImage() != null ? product.getImage() : "default.png", "Imagen");
                img.setWidth("100%");
                img.setHeight("120px");
                img.getStyle().set("object-fit", "contain");

                Paragraph name = new Paragraph(product.getName());
                name.getStyle().set("font-weight", "bold").set("font-size", "14px");

                Paragraph price = new Paragraph("💰 " + String.format("%.2f", product.getPrice()) + "€");

                card.add(img, name, price);
                column.add(card);
            }
            supermercadoRow.add(column);
        }

        Button closeButton = new Button("Cerrar", e -> dialog.close());
        closeButton.getStyle()
                .set("background-color", "#83C28F")
                .set("color", "white")
                .set("font-weight", "bold")
                .set("border-radius", "8px");

        dialogContent.add(title, supermercadoRow, closeButton);
        dialog.add(dialogContent);
        dialog.open();
    }
}
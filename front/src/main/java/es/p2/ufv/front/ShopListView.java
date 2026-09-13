package es.p2.ufv.front;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

@Route("shoplist")
public class ShopListView extends VerticalLayout
{
    @Autowired
    private ProductService productService;

    private final List<String> availableItems = Arrays.asList("Patatas", "Huevos", "Leche", "Atún", "Pollo", "Arroz", "Pasta", "Yogur", "Maiz", "Aceite", "Harina", "Cerveza", "Azucar", "Detergente", "Gel", "Champu", "Dentífrico", "Cereales", "Cafe", "Zumo", "Pan", "Chocolate", "Lejia", "Miel");
    private final Set<String> selectedItems = new HashSet<>();
    private final FlexLayout selectedListLayout = new FlexLayout();

    public ShopListView()
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
        contentBox.setWidth("700px");
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

        H1 title = new H1("Tu Lista de la Compra");
        title.getStyle().set("color", "#83C28F").set("font-weight", "bold");

        Paragraph description = new Paragraph("Selecciona productos para tu lista. Calcularemos en qué supermercado sale más barato comprarla.");
        description.getStyle().set("text-align", "center").set("color", "#555");

        FlexLayout options = new FlexLayout();
        options.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        options.setJustifyContentMode(JustifyContentMode.CENTER);
        options.getStyle().set("gap", "10px");

        for (String item : availableItems)
        {
            VerticalLayout addButton = new VerticalLayout();
            addButton.setWidth("140px");
            addButton.setHeight("140px");
            addButton.getStyle()
                    .set("background-color", "#83C28F")
                    .set("color", "white")
                    .set("font-weight", "bold")
                    .set("border-radius", "16px")
                    .set("display", "flex")
                    .set("flex-direction", "column")
                    .set("align-items", "center")
                    .set("justify-content", "center")
                    .set("cursor", "pointer");  // Para que parezca clicable

            Image icon = new Image("images/" + item.toLowerCase().replace("ñ", "n") + ".png", item);
            icon.setHeight("40px");
            icon.getStyle().set("filter", "invert(1)").set("margin-bottom", "5px");

            Span text = new Span(item);
            text.getStyle().set("font-weight", "bold");

            addButton.add(icon, text);

            addButton.addClickListener(e -> {
                if (selectedItems.add(item)) {
                    updateSelectedList();
                }
            });
            options.add(addButton);
        }

        selectedListLayout.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        selectedListLayout.getStyle().set("gap", "10px");
        updateSelectedList();

        Button calculateButton = new Button("Calcular mejor supermercado", e -> calcularMejorSupermercado());
        calculateButton.getStyle().set("background-color", "#28a745").set("color", "white").set("font-weight", "bold");

        Button backButton = new Button("Volver", e -> getUI().ifPresent(ui -> ui.navigate("")));
        backButton.getStyle().set("background-color", "#dc3545").set("color", "white").set("font-weight", "bold");

        contentBox.add(title, description, options, new Hr(), new H4("Productos seleccionados:"), selectedListLayout, calculateButton, backButton);
        add(contentBox);
    }

    private void updateSelectedList()
    {
        selectedListLayout.removeAll();
        for (String item : selectedItems)
        {
            VerticalLayout chip = new VerticalLayout();
            chip.setAlignItems(Alignment.CENTER);
            chip.setSpacing(false);
            chip.setPadding(false);
            chip.setWidth("120px");
            chip.setHeight("120px");
            chip.getStyle()
                    .set("background-color", "#51a461")
                    .set("border-radius", "16px")
                    .set("color", "white")
                    .set("justify-content", "center")
                    .set("align-items", "center")
                    .set("display", "flex")
                    .set("flex-direction", "column")
                    .set("text-align", "center");

            Image icon = new Image("images/" + item.toLowerCase().replace("ñ", "n") + ".png", item);
            icon.setHeight("30px");
            icon.getStyle().set("filter", "invert(1)").set("margin-bottom", "5px");

            Span name = new Span(item);
            name.getStyle().set("font-weight", "bold").set("margin-bottom", "5px");

            Image iconoX = new Image("images/x-blanca.png", "Eliminar");
            iconoX.setHeight("16px"); // Ajusta según tu diseño
            iconoX.getStyle().set("margin", "0").set("padding", "0");

            Button remove = new Button(iconoX, e -> {
                selectedItems.remove(item);
                updateSelectedList();
            });
            remove.getStyle()
                    .set("background-color", "#51a461")
                    .set("border", "none")
                    .set("padding", "4px")
                    .set("border-radius", "8px");



            chip.add(icon, name, remove);
            selectedListLayout.add(chip);
        }
    }

    private void calcularMejorSupermercado()
    {
        Map<String, List<Product>> productosPorSuper = new HashMap<>();

        for (String item : selectedItems)
        {
            List<Product> productos = productService.searchProducts(item);
            Map<String, Optional<Product>> minPorSuper = productos.stream()
                    .collect(Collectors.groupingBy(Product::getSupermarket, Collectors.minBy(Comparator.comparing(Product::getPrice))));

            for (Map.Entry<String, Optional<Product>> entry : minPorSuper.entrySet())
            {
                entry.getValue().ifPresent(product -> {
                    productosPorSuper.computeIfAbsent(entry.getKey(), k -> new ArrayList<>()).add(product);
                });
            }
        }

        if (productosPorSuper.isEmpty())
        {
            Notification.show("No se encontraron productos suficientes en los supermercados seleccionados.");
            return;
        }

        Dialog resultDialog = new Dialog();
        VerticalLayout resultContent = new VerticalLayout();
        resultContent.setPadding(true);
        resultContent.setSpacing(true);

        H3 title = new H3("Coste total por supermercado");
        title.getStyle().set("color", "#83C28F");
        resultContent.add(title);

        productosPorSuper.entrySet().stream()
                .sorted(Comparator.comparingDouble(e -> e.getValue().stream().mapToDouble(Product::getPrice).sum()))
                .forEach(entry -> {
                    String supermercado = entry.getKey();
                    List<Product> productos = entry.getValue();
                    double total = productos.stream().mapToDouble(Product::getPrice).sum();

                    VerticalLayout card = new VerticalLayout();
                    card.setSpacing(true);
                    card.setPadding(true);
                    card.getStyle()
                            .set("border", "1px solid #ddd")
                            .set("border-radius", "10px")
                            .set("background-color", "#f9f9f9")
                            .set("padding", "10px");

                    H4 nombreSuper = new H4(supermercado + ": " + String.format("%.2f€", total));
                    nombreSuper.getStyle().set("color", "#28a745");
                    card.add(nombreSuper);

                    for (Product p : productos)
                    {
                        HorizontalLayout itemRow = new HorizontalLayout();
                        itemRow.setAlignItems(Alignment.CENTER);

                        Image icon = new Image(p.getImage() != null ? p.getImage() : "images/default.png", p.getName());
                        icon.setHeight("30px");
                        icon.getStyle().set("margin-right", "10px");

                        Paragraph name = new Paragraph(p.getName() + " - " + String.format("%.2f€", p.getPrice()));
                        name.getStyle().set("margin", "0");

                        itemRow.add(icon, name);
                        card.add(itemRow);
                    }

                    resultContent.add(card, new Hr());
                });

        Button close = new Button("Cerrar", e -> resultDialog.close());
        close.getStyle().set("background-color", "#83C28F").set("color", "white");
        resultContent.add(close);

        resultDialog.add(resultContent);
        resultDialog.open();
    }
}

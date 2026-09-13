package es.p2.ufv.front.Register;

import es.p2.ufv.front.MainView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route("login")
public class LoginView extends VerticalLayout
{
    private final UserService userService;

    public LoginView()
    {
        this.userService = new UserService();
        // Fondo igual que en el inicio
        getStyle()
                .set("background-image", "url('/images/fondoPagPrincipal.png')")
                .set("background-size", "cover")
                .set("background-position", "center")
                .set("min-height", "100vh")
                .set("display", "flex")
                .set("justify-content", "center")
                .set("align-items", "center");

        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        VerticalLayout formLayout = new VerticalLayout();
        formLayout.setWidth("350px");
        formLayout.setPadding(true);
        formLayout.setSpacing(true);
        formLayout.getStyle()
                .set("border-radius", "20px")
                .set("box-shadow", "0 8px 20px rgba(0, 0, 0, 0.15)")
                .set("padding", "30px")
                .set("background", "white");

        H2 title = new H2("¡Inicia sesión aquí!");
        title.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.FontWeight.BOLD);
        title.getStyle().set("color", "#83C28F");

        TextField usernameField = new TextField("Nombre de Usuario");
        usernameField.setWidthFull();

        PasswordField passwordField = new PasswordField("Contraseña");
        passwordField.setWidthFull();

        Button loginButton = new Button("Ingresar", event -> {
            String username = usernameField.getValue();
            String password = passwordField.getValue();

            boolean success = userService.validateUser(username, password);

            if (success)
            {
                VaadinSession.getCurrent().setAttribute("loggedUser", username);
                Notification.show("Inicio de sesión exitoso", 3000, Notification.Position.BOTTOM_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);

                System.out.println("🎯 Redirigiendo a página principal");
                UI.getCurrent().navigate(MainView.class);
            }
            else
            {
                Notification.show("Usuario o contraseña incorrectos", 3000, Notification.Position.BOTTOM_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        loginButton.setWidthFull();
        loginButton.getStyle()
                .set("background-color", "#83C28F")
                .set("color", "white");

        // Botón para volver al inicio
        Button backToHome = new Button("Volver al Inicio", e -> UI.getCurrent().navigate(""));
        backToHome.setWidthFull();
        backToHome.getStyle()
                .set("margin-top", "10px")
                .set("background-color", "white")
                .set("color", "#83C28F")
                .set("border", "1px solid #83C28F");

        formLayout.add(title, usernameField, passwordField, loginButton, backToHome);
        add(formLayout);
    }
}
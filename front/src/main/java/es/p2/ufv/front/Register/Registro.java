package es.p2.ufv.front.Register;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Route("register")
public class Registro extends VerticalLayout
{
    private final UserService userService;

    public Registro()
    {
        this.userService = new UserService();

        // Fondo como el de la página principal
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
        formLayout.setWidth("400px");
        formLayout.setPadding(true);
        formLayout.setSpacing(true);
        formLayout.getStyle()
                .set("border-radius", "20px")
                .set("box-shadow", "0 8px 20px rgba(0, 0, 0, 0.15)")
                .set("padding", "30px")
                .set("background", "white");

        H2 title = new H2("¡Regístrate aquí!");
        title.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.FontWeight.BOLD);
        title.getStyle().set("color", "#83C28F");

        TextField usernameField = new TextField("Nombre de Usuario");
        usernameField.setWidthFull();
        PasswordField passwordField = new PasswordField("Contraseña");
        passwordField.setWidthFull();
        PasswordField confirmPasswordField = new PasswordField("Confirmar Contraseña");
        confirmPasswordField.setWidthFull();

        Button registerButton = new Button("Registrarse", event -> {
            String username = usernameField.getValue();
            String password = passwordField.getValue();
            String confirmPassword = confirmPasswordField.getValue();

            if (username.isEmpty() || password.isEmpty())
            {
                Notification.show("Todos los campos son obligatorios", 3000, Notification.Position.BOTTOM_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            if (!password.equals(confirmPassword))
            {
                Notification.show("Las contraseñas no coinciden", 3000, Notification.Position.BOTTOM_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            if (userService.userExists(username))
            {
                Notification.show("El nombre de usuario ya está registrado", 3000, Notification.Position.BOTTOM_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            boolean success = userService.saveUser(new User(username, password));

            if (success)
            {
                Notification.show("Registro exitoso", 3000, Notification.Position.BOTTOM_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                getUI().ifPresent(ui -> ui.navigate("login"));
            }
            else
            {
                Notification.show("Error en el registro", 3000, Notification.Position.BOTTOM_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        registerButton.setWidthFull();
        registerButton.getStyle()
                .set("background-color", "#83C28F")
                .set("color", "white");

        Anchor loginAnchor = new Anchor("login", "¿Ya tienes cuenta? Inicia sesión");
        loginAnchor.getStyle()
                .set("text-align", "center")
                .set("display", "block")
                // .set("margin-top", "0px")
                .set("color", "#83C28F")
                .set("text-decoration", "none");

        // Botón que reemplaza al enlace de volver al inicio
        Button backToHome = new Button("Volver al Inicio", e -> getUI().ifPresent(ui -> ui.navigate("")));
        backToHome.setWidthFull();
        backToHome.getStyle()
                .set("margin-top", "10px")
                .set("background-color", "white")
                .set("color", "#83C28F")
                .set("border", "1px solid #83C28F");

        formLayout.add(title, usernameField, passwordField, confirmPasswordField, registerButton, loginAnchor, backToHome);
        add(formLayout);
    }
}

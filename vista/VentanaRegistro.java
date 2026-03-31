package vista;

import controlador.ControladorUsuarios;
import controlador.UsuarioFactory;
import modelo.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.regex.Pattern;

/**
 * Ventana de Registro de nuevos usuarios.
 * Extiende VentanaBase (Herencia) y usa la Factory para crear usuarios.
 */
public class VentanaRegistro extends VentanaBase {

    // Referencia al login para volver a mostrarlo al salir
    private final VentanaLogin ventanaLogin;

    // Componentes del formulario
    private JTextField campUsername;
    private JTextField campNombre;
    private JTextField campApellido;
    private JTextField campTelefono;
    private JTextField campEmail;
    private JPasswordField campPassword;
    private JPasswordField campConfirmar;

    private JButton btnRegistrar;
    private JButton btnCancelar;

    public VentanaRegistro(VentanaLogin ventanaLogin) {
        super("Sistema de Usuarios – Registro");
        this.ventanaLogin = ventanaLogin;
        inicializarComponentes();
        setSize(480, 620);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        // Si cierra la ventana con la X, volver al login
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                cancelar();
            }
        });
    }

    @Override
    protected void inicializarComponentes() {

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(COLOR_FONDO);

        // ── Encabezado ────────────────────────────────────────────────────
        JPanel panelEncabezado = new JPanel(new BorderLayout());
        panelEncabezado.setBackground(COLOR_SECUNDARIO);
        panelEncabezado.setBorder(new EmptyBorder(18, 20, 18, 20));

        JLabel lblTitulo = new JLabel("Crear Cuenta", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);
        panelEncabezado.add(lblTitulo);

        // ── Panel de formulario con scroll ────────────────────────────────
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(COLOR_FONDO);
        panelForm.setBorder(new EmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        // Campos del formulario
        campUsername = crearCampoTexto(20);
        campNombre   = crearCampoTexto(20);
        campApellido = crearCampoTexto(20);
        campTelefono = crearCampoTexto(20);
        campEmail    = crearCampoTexto(20);
        campPassword = crearCampoPassword(20);
        campConfirmar= crearCampoPassword(20);

        // Agregar filas (etiqueta + campo)
        agregarFila(panelForm, gbc, 0,  "Nombre de usuario *", campUsername);
        agregarFila(panelForm, gbc, 2,  "Nombre *",            campNombre);
        agregarFila(panelForm, gbc, 4,  "Apellido *",          campApellido);
        agregarFila(panelForm, gbc, 6,  "Teléfono *",          campTelefono);
        agregarFila(panelForm, gbc, 8,  "Correo electrónico *",campEmail);
        agregarFila(panelForm, gbc, 10, "Contraseña *",        campPassword);
        agregarFila(panelForm, gbc, 12, "Confirmar contraseña *", campConfirmar);

        // ── Panel de botones ──────────────────────────────────────────────
        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 10, 0));
        panelBotones.setBackground(COLOR_FONDO);
        panelBotones.setBorder(new EmptyBorder(10, 40, 20, 40));

        btnRegistrar = crearBotonPrimario("Registrarme");
        btnCancelar  = crearBotonSecundario("Cancelar");

        panelBotones.add(btnRegistrar);
        panelBotones.add(btnCancelar);

        // ── Ensamblado ────────────────────────────────────────────────────
        JScrollPane scroll = new JScrollPane(panelForm);
        scroll.setBorder(null);
        scroll.setBackground(COLOR_FONDO);

        panelPrincipal.add(panelEncabezado, BorderLayout.NORTH);
        panelPrincipal.add(scroll, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        setContentPane(panelPrincipal);

        // ── Listeners ────────────────────────────────────────────────────
        btnRegistrar.addActionListener(e -> registrar());
        btnCancelar.addActionListener(e -> cancelar());
    }

    /**
     * Agrega una fila (etiqueta + campo) al panel usando GridBagLayout.
     */
    private void agregarFila(JPanel panel, GridBagConstraints gbc,
                              int fila, String etiqueta, JComponent campo) {
        gbc.gridy = fila;
        panel.add(crearEtiqueta(etiqueta), gbc);
        gbc.gridy = fila + 1;
        panel.add(campo, gbc);
    }

    /**
     * Valida todos los campos y registra el nuevo usuario si todo es correcto.
     */
    private void registrar() {
        String username   = campUsername.getText().trim();
        String nombre     = campNombre.getText().trim();
        String apellido   = campApellido.getText().trim();
        String telefono   = campTelefono.getText().trim();
        String email      = campEmail.getText().trim();
        String password   = new String(campPassword.getPassword());
        String confirmar  = new String(campConfirmar.getPassword());

        // ── Validación: campos obligatorios ───────────────────────────────
        if (username.isEmpty())  { mostrarError("El campo 'Nombre de usuario' es obligatorio."); campUsername.requestFocus();  return; }
        if (nombre.isEmpty())    { mostrarError("El campo 'Nombre' es obligatorio.");            campNombre.requestFocus();    return; }
        if (apellido.isEmpty())  { mostrarError("El campo 'Apellido' es obligatorio.");          campApellido.requestFocus();  return; }
        if (telefono.isEmpty())  { mostrarError("El campo 'Teléfono' es obligatorio.");          campTelefono.requestFocus();  return; }
        if (email.isEmpty())     { mostrarError("El campo 'Correo electrónico' es obligatorio.");campEmail.requestFocus();     return; }
        if (password.isEmpty())  { mostrarError("El campo 'Contraseña' es obligatorio.");        campPassword.requestFocus();  return; }
        if (confirmar.isEmpty()) { mostrarError("Debe confirmar la contraseña.");                campConfirmar.requestFocus(); return; }

        // ── Validación: formato de email ──────────────────────────────────
        if (!esEmailValido(email)) {
            mostrarError("El correo electrónico no tiene un formato válido.");
            campEmail.requestFocus();
            return;
        }

        // ── Validación: coincidencia de contraseñas ───────────────────────
        if (!password.equals(confirmar)) {
            mostrarError("Las contraseñas no coinciden.");
            campPassword.setText("");
            campConfirmar.setText("");
            campPassword.requestFocus();
            return;
        }

        // ── Validación: longitud mínima de contraseña ─────────────────────
        if (password.length() < 6) {
            mostrarError("La contraseña debe tener al menos 6 caracteres.");
            campPassword.requestFocus();
            return;
        }

        ControladorUsuarios ctrl = ControladorUsuarios.getInstancia();

        // ── Validación: username único ────────────────────────────────────
        if (ctrl.buscar(username) != null) {
            mostrarError("El nombre de usuario '" + username + "' ya está en uso.");
            campUsername.requestFocus();
            return;
        }

        // ── Validación: email único ───────────────────────────────────────
        if (ctrl.emailExiste(email)) {
            mostrarError("El correo '" + email + "' ya está registrado.");
            campEmail.requestFocus();
            return;
        }

        // ── Crear y agregar el usuario (patrón Factory) ───────────────────
        Usuario nuevoUsuario = UsuarioFactory.crearUsuario(
                username, nombre, apellido, telefono, email, password);
        ctrl.agregar(nuevoUsuario);

        mostrarExito("¡Usuario registrado exitosamente!\nPuede iniciar sesión ahora.");
        cancelar(); // volver al login
    }

    /**
     * Cierra el registro y vuelve a mostrar la pantalla de login.
     */
    private void cancelar() {
        ventanaLogin.setVisible(true);
        dispose();
    }

    /**
     * Valida el formato básico de un email con expresión regular.
     */
    private boolean esEmailValido(String email) {
        String regex = "^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$";
        return Pattern.matches(regex, email);
    }
}
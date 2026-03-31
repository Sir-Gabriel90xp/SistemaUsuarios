package vista;

import controlador.ControladorUsuarios;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Ventana de Login del sistema.
 * Extiende VentanaBase (Herencia) y define su propia UI.
 */
public class VentanaLogin extends VentanaBase {

    // Componentes de la ventana
    private JTextField campUsername;
    private JPasswordField campPassword;
    private JButton btnEntrar;
    private JButton btnRegistrarse;

    public VentanaLogin() {
        super("Sistema de Usuarios – Login");
        inicializarComponentes();
        setSize(420, 480);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Construye y distribuye todos los componentes visuales del login.
     */
    @Override
    protected void inicializarComponentes() {

        // Panel principal con padding
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(COLOR_FONDO);
        panelPrincipal.setBorder(new EmptyBorder(30, 50, 30, 50));

        // ── Encabezado ────────────────────────────────────────────────────
        JPanel panelEncabezado = new JPanel(new BorderLayout());
        panelEncabezado.setBackground(COLOR_PRIMARIO);
        panelEncabezado.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("Bienvenido", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSubtitulo = new JLabel("Sistema de Gestión de Usuarios", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitulo.setForeground(new Color(180, 200, 255));

        panelEncabezado.add(lblTitulo, BorderLayout.CENTER);
        panelEncabezado.add(lblSubtitulo, BorderLayout.SOUTH);

        // ── Panel de formulario ───────────────────────────────────────────
        JPanel panelForm = new JPanel();
        panelForm.setLayout(new BoxLayout(panelForm, BoxLayout.Y_AXIS));
        panelForm.setBackground(COLOR_FONDO);
        panelForm.setBorder(new EmptyBorder(30, 0, 20, 0));

        // Campo username
        JLabel lblUser = crearEtiqueta("Usuario");
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);
        campUsername = crearCampoTexto(20);
        campUsername.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        campUsername.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Campo password
        JLabel lblPass = crearEtiqueta("Contraseña");
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);
        campPassword = crearCampoPassword(20);
        campPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        campPassword.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Botones
        btnEntrar = crearBotonPrimario("Entrar");
        btnEntrar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnEntrar.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnRegistrarse = crearBotonSecundario("Registrarse");
        btnRegistrarse.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btnRegistrarse.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Ensamblado del formulario
        panelForm.add(lblUser);
        panelForm.add(Box.createVerticalStrut(5));
        panelForm.add(campUsername);
        panelForm.add(Box.createVerticalStrut(15));
        panelForm.add(lblPass);
        panelForm.add(Box.createVerticalStrut(5));
        panelForm.add(campPassword);
        panelForm.add(Box.createVerticalStrut(25));
        panelForm.add(btnEntrar);
        panelForm.add(Box.createVerticalStrut(10));
        panelForm.add(btnRegistrarse);

        // ── Nota informativa ─────────────────────────────────────────────
        JLabel lblNota = new JLabel("Usuario demo: admin / admin123", SwingConstants.CENTER);
        lblNota.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblNota.setForeground(Color.GRAY);

        // ── Ensamblado principal ──────────────────────────────────────────
        panelPrincipal.add(panelEncabezado, BorderLayout.NORTH);
        panelPrincipal.add(panelForm, BorderLayout.CENTER);
        panelPrincipal.add(lblNota, BorderLayout.SOUTH);
        setContentPane(panelPrincipal);

        // ── Listeners ────────────────────────────────────────────────────
        btnEntrar.addActionListener(e -> iniciarSesion());
        btnRegistrarse.addActionListener(e -> abrirRegistro());

        // Permitir Enter en el campo password para iniciar sesión
        campPassword.addActionListener(e -> iniciarSesion());
    }

    /**
     * Lógica de inicio de sesión: valida campos y credenciales.
     */
    private void iniciarSesion() {
        String username = campUsername.getText().trim();
        String password = new String(campPassword.getPassword()).trim();

        // Validar campos vacíos
        if (username.isEmpty() || password.isEmpty()) {
            mostrarError("Debe ingresar su usuario y contraseña.\n" +
                         "Si no está registrado debe registrarse.");
            return;
        }

        // Validar credenciales
        ControladorUsuarios ctrl = ControladorUsuarios.getInstancia();
        if (ctrl.validarCredenciales(username, password)) {
            dispose(); // cerrar la ventana de login
            new VentanaPrincipal(username); // abrir pantalla principal
        } else {
            mostrarError("Usuario o contraseña incorrectos.");
            campPassword.setText("");
            campPassword.requestFocus();
        }
    }

    /**
     * Abre la ventana de registro y oculta el login temporalmente.
     */
    private void abrirRegistro() {
        setVisible(false);
        new VentanaRegistro(this);
    }
}
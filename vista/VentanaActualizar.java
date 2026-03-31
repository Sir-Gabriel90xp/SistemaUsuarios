package vista;

import controlador.ControladorUsuarios;
import controlador.UsuarioFactory;
import modelo.Usuario;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.regex.Pattern;

/**
 * Diálogo modal para actualizar los datos de un usuario existente.
 * Extiende JDialog (composición con VentanaPrincipal como owner).
 * Reutiliza los métodos de estilo de VentanaBase mediante delegación.
 */
public class VentanaActualizar extends JDialog {

    private final VentanaPrincipal ventanaPadre;
    private final Usuario usuarioOriginal;

    // Componentes del formulario
    private JTextField campNombre;
    private JTextField campApellido;
    private JTextField campTelefono;
    private JTextField campEmail;
    private JPasswordField campPassword;
    private JPasswordField campConfirmar;

    // Colores y fuentes (replicados de VentanaBase para este diálogo)
    private static final Color COLOR_PRIMARIO   = VentanaBase.COLOR_PRIMARIO;
    private static final Color COLOR_SECUNDARIO = VentanaBase.COLOR_SECUNDARIO;
    private static final Color COLOR_FONDO      = VentanaBase.COLOR_FONDO;
    private static final Color COLOR_TEXTO      = VentanaBase.COLOR_TEXTO;
    private static final Font  FUENTE_ETIQUETA  = VentanaBase.FUENTE_ETIQUETA;
    private static final Font  FUENTE_CAMPO     = VentanaBase.FUENTE_CAMPO;
    private static final Font  FUENTE_BOTON     = VentanaBase.FUENTE_BOTON;

    public VentanaActualizar(VentanaPrincipal padre, Usuario usuario) {
        super(padre, "Actualizar Usuario: " + usuario.getUsername(), true); // modal
        this.ventanaPadre    = padre;
        this.usuarioOriginal = usuario;

        setSize(440, 540);
        setLocationRelativeTo(padre);
        setResizable(false);
        construirUI();
        setVisible(true);
    }

    private void construirUI() {

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(COLOR_FONDO);

        // ── Encabezado ────────────────────────────────────────────────────
        JPanel panelEncabezado = new JPanel(new BorderLayout());
        panelEncabezado.setBackground(COLOR_PRIMARIO);
        panelEncabezado.setBorder(new EmptyBorder(16, 20, 16, 20));

        JLabel lblTitulo = new JLabel("Actualizar datos de: " + usuarioOriginal.getUsername(),
                SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(Color.WHITE);
        panelEncabezado.add(lblTitulo);

        // ── Formulario ────────────────────────────────────────────────────
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(COLOR_FONDO);
        panelForm.setBorder(new EmptyBorder(20, 35, 10, 35));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 0, 4, 0);
        gbc.gridx = 0;
        gbc.weightx = 1.0;

        // Pre-llenar con datos actuales
        campNombre    = crearCampo(usuarioOriginal.getNombre());
        campApellido  = crearCampo(usuarioOriginal.getApellido());
        campTelefono  = crearCampo(usuarioOriginal.getTelefono());
        campEmail     = crearCampo(usuarioOriginal.getEmail());
        campPassword  = crearCampoPass();
        campConfirmar = crearCampoPass();

        agregarFila(panelForm, gbc, 0,  "Nombre *",               campNombre);
        agregarFila(panelForm, gbc, 2,  "Apellido *",             campApellido);
        agregarFila(panelForm, gbc, 4,  "Teléfono *",             campTelefono);
        agregarFila(panelForm, gbc, 6,  "Email *",                campEmail);
        agregarFila(panelForm, gbc, 8,  "Nueva contraseña",       campPassword);
        agregarFila(panelForm, gbc, 10, "Confirmar contraseña",   campConfirmar);

        // Nota sobre contraseña
        JLabel nota = new JLabel("* Deje la contraseña en blanco para no cambiarla.");
        nota.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        nota.setForeground(Color.GRAY);
        gbc.gridy = 12;
        panelForm.add(nota, gbc);

        // ── Botones ───────────────────────────────────────────────────────
        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 10, 0));
        panelBotones.setBackground(COLOR_FONDO);
        panelBotones.setBorder(new EmptyBorder(5, 35, 18, 35));

        JButton btnGuardar  = crearBoton("Guardar Cambios", COLOR_PRIMARIO, Color.WHITE);
        JButton btnCancelar = crearBoton("Cancelar", VentanaBase.COLOR_ACENTO, COLOR_TEXTO);

        btnGuardar.addActionListener(e -> guardarCambios());
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        // ── Ensamblado ────────────────────────────────────────────────────
        JScrollPane scroll = new JScrollPane(panelForm);
        scroll.setBorder(null);

        panelPrincipal.add(panelEncabezado, BorderLayout.NORTH);
        panelPrincipal.add(scroll,          BorderLayout.CENTER);
        panelPrincipal.add(panelBotones,    BorderLayout.SOUTH);
        setContentPane(panelPrincipal);
    }

    /**
     * Valida y guarda los cambios del usuario.
     */
    private void guardarCambios() {
        String nombre    = campNombre.getText().trim();
        String apellido  = campApellido.getText().trim();
        String telefono  = campTelefono.getText().trim();
        String email     = campEmail.getText().trim();
        String pass      = new String(campPassword.getPassword());
        String confirmar = new String(campConfirmar.getPassword());

        // Validar campos obligatorios
        if (nombre.isEmpty())   { error("El campo 'Nombre' es obligatorio.");   campNombre.requestFocus();   return; }
        if (apellido.isEmpty()) { error("El campo 'Apellido' es obligatorio."); campApellido.requestFocus(); return; }
        if (telefono.isEmpty()) { error("El campo 'Teléfono' es obligatorio."); campTelefono.requestFocus(); return; }
        if (email.isEmpty())    { error("El campo 'Email' es obligatorio.");    campEmail.requestFocus();    return; }

        // Validar email
        if (!Pattern.matches("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$", email)) {
            error("El correo electrónico no tiene un formato válido.");
            campEmail.requestFocus();
            return;
        }

        // Determinar contraseña final
        String passwordFinal = usuarioOriginal.getPassword(); // mantener actual
        if (!pass.isEmpty() || !confirmar.isEmpty()) {
            if (!pass.equals(confirmar)) {
                error("Las contraseñas no coinciden.");
                campPassword.setText(""); campConfirmar.setText("");
                campPassword.requestFocus();
                return;
            }
            if (pass.length() < 6) {
                error("La nueva contraseña debe tener al menos 6 caracteres.");
                campPassword.requestFocus();
                return;
            }
            passwordFinal = pass;
        }

        // Crear usuario actualizado (Factory)
        Usuario actualizado = UsuarioFactory.crearUsuario(
                usuarioOriginal.getUsername(), nombre, apellido,
                telefono, email, passwordFinal);

        boolean ok = ControladorUsuarios.getInstancia()
                .actualizar(usuarioOriginal.getUsername(), actualizado);

        if (ok) {
            JOptionPane.showMessageDialog(this,
                    "Usuario actualizado correctamente.", "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
            ventanaPadre.actualizarTabla();
            dispose();
        } else {
            error("No se pudo actualizar el usuario.");
        }
    }

    // ── Helpers de creación de componentes ───────────────────────────────

    private JTextField crearCampo(String valorInicial) {
        JTextField campo = new JTextField(valorInicial);
        campo.setFont(FUENTE_CAMPO);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 195, 230), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        campo.setPreferredSize(new Dimension(0, 34));
        return campo;
    }

    private JPasswordField crearCampoPass() {
        JPasswordField campo = new JPasswordField();
        campo.setFont(FUENTE_CAMPO);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 195, 230), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        campo.setPreferredSize(new Dimension(0, 34));
        return campo;
    }

    private JButton crearBoton(String texto, Color bg, Color fg) {
        JButton btn = new JButton(texto);
        btn.setFont(FUENTE_BOTON);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(0, 38));
        return btn;
    }

    private void agregarFila(JPanel panel, GridBagConstraints gbc,
                              int fila, String etiqueta, JComponent campo) {
        JLabel lbl = new JLabel(etiqueta);
        lbl.setFont(FUENTE_ETIQUETA);
        lbl.setForeground(COLOR_TEXTO);
        gbc.gridy = fila;
        panel.add(lbl, gbc);
        gbc.gridy = fila + 1;
        panel.add(campo, gbc);
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
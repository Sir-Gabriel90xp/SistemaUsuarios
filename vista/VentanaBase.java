package vista;

import javax.swing.*;
import java.awt.*;

/**
 * Clase abstracta base para todas las ventanas de la aplicación.
 *
 * Aplica HERENCIA: las vistas concretas (Login, Registro, Principal)
 * extienden esta clase y heredan configuración y comportamiento comunes.
 *
 * Aplica ABSTRACCIÓN: define el método inicializarComponentes() que cada
 * subclase debe implementar con su propia lógica de UI.
 */
public abstract class VentanaBase extends JFrame {

    // Paleta de colores de la aplicación (compartida por todas las ventanas)
    protected static final Color COLOR_PRIMARIO    = new Color(25, 42, 86);   // Azul oscuro
    protected static final Color COLOR_SECUNDARIO  = new Color(52, 84, 168);  // Azul medio
    protected static final Color COLOR_ACENTO      = new Color(255, 193, 7);  // Amarillo dorado
    protected static final Color COLOR_FONDO       = new Color(240, 244, 255);// Gris-azul claro
    protected static final Color COLOR_TEXTO       = new Color(30, 30, 50);   // Casi negro
    protected static final Color COLOR_ERROR       = new Color(220, 53, 69);  // Rojo
    protected static final Color COLOR_EXITO       = new Color(40, 167, 69);  // Verde

    // Fuentes compartidas
    protected static final Font FUENTE_TITULO   = new Font("Segoe UI", Font.BOLD, 22);
    protected static final Font FUENTE_ETIQUETA = new Font("Segoe UI", Font.PLAIN, 13);
    protected static final Font FUENTE_CAMPO    = new Font("Segoe UI", Font.PLAIN, 13);
    protected static final Font FUENTE_BOTON    = new Font("Segoe UI", Font.BOLD, 13);

    /**
     * Constructor base: aplica configuración visual común a todas las ventanas.
     * @param titulo Título que aparecerá en la barra de la ventana.
     */
    public VentanaBase(String titulo) {
        super(titulo);
        configurarVentana();
    }

    /** Configuración general de la ventana (tamaño, cierre, look and feel, etc.). */
    private void configurarVentana() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(COLOR_FONDO);

        // Establecer look and feel del sistema operativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
    }

    /**
     * Método abstracto que cada subclase debe implementar para
     * construir y distribuir sus componentes visuales.
     */
    protected abstract void inicializarComponentes();

    // ── Métodos de utilidad compartidos ──────────────────────────────────

    /**
     * Crea un JButton con el estilo primario de la aplicación.
     */
    protected JButton crearBotonPrimario(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(FUENTE_BOTON);
        btn.setBackground(COLOR_PRIMARIO);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 38));

        // Efecto hover
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(COLOR_SECUNDARIO);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(COLOR_PRIMARIO);
            }
        });
        return btn;
    }

    /**
     * Crea un JButton con el estilo secundario (acento) de la aplicación.
     */
    protected JButton crearBotonSecundario(String texto) {
        JButton btn = new JButton(texto);
        btn.setFont(FUENTE_BOTON);
        btn.setBackground(COLOR_ACENTO);
        btn.setForeground(COLOR_TEXTO);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 38));

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(255, 210, 50));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(COLOR_ACENTO);
            }
        });
        return btn;
    }

    /**
     * Crea un JTextField con el estilo estándar de la aplicación.
     */
    protected JTextField crearCampoTexto(int columnas) {
        JTextField campo = new JTextField(columnas);
        campo.setFont(FUENTE_CAMPO);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 195, 230), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        campo.setPreferredSize(new Dimension(0, 36));
        return campo;
    }

    /**
     * Crea un JPasswordField con el estilo estándar de la aplicación.
     */
    protected JPasswordField crearCampoPassword(int columnas) {
        JPasswordField campo = new JPasswordField(columnas);
        campo.setFont(FUENTE_CAMPO);
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 195, 230), 1),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        campo.setPreferredSize(new Dimension(0, 36));
        return campo;
    }

    /**
     * Crea una etiqueta de campo con el estilo estándar.
     */
    protected JLabel crearEtiqueta(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(FUENTE_ETIQUETA);
        lbl.setForeground(COLOR_TEXTO);
        return lbl;
    }

    /**
     * Muestra un mensaje de error usando JOptionPane.
     */
    protected void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Muestra un mensaje de éxito usando JOptionPane.
     */
    protected void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
}
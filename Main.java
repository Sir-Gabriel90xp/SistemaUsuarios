import vista.VentanaLogin;

import javax.swing.*;

/**
 * Punto de entrada principal de la aplicación.
 *
 * Lanza la interfaz en el Event Dispatch Thread (EDT) de Swing,
 * que es el hilo correcto para toda operación de UI en Java.
 */
public class Main {

    public static void main(String[] args) {
        // Establecer el look and feel del sistema antes de crear cualquier ventana
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        // Iniciar la aplicación en el EDT de Swing
        SwingUtilities.invokeLater(VentanaLogin::new);
    }
}
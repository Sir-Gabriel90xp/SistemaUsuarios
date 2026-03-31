package vista;

import controlador.ControladorUsuarios;
import modelo.Usuario;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VentanaPrincipal extends VentanaBase {

    private final String usernameSesion;
    private JTable tabla;

    public VentanaPrincipal(String usernameSesion) {
        super("Sistema de Usuarios – Principal");
        this.usernameSesion = usernameSesion;
        inicializarComponentes();
        setSize(720, 520);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    protected void inicializarComponentes() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBackground(COLOR_FONDO);
        root.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JLabel titulo = new JLabel("Panel de Usuarios (sesión: " + usernameSesion + ")");
        titulo.setFont(FUENTE_TITULO);
        titulo.setForeground(COLOR_TEXTO);
        root.add(titulo, BorderLayout.NORTH);

        tabla = new JTable();
        tabla.setFillsViewportHeight(true);
        tabla.setRowHeight(26);
        root.add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        acciones.setBackground(COLOR_FONDO);

        JButton btnActualizar = crearBotonPrimario("Actualizar");
        JButton btnEliminar = crearBotonSecundario("Eliminar");
        JButton btnCerrarSesion = crearBotonSecundario("Cerrar sesión");

        acciones.add(btnActualizar);
        acciones.add(btnEliminar);
        acciones.add(btnCerrarSesion);
        root.add(acciones, BorderLayout.SOUTH);

        setContentPane(root);

        btnActualizar.addActionListener(e -> abrirActualizarSeleccionado());
        btnEliminar.addActionListener(e -> eliminarSeleccionado());
        btnCerrarSesion.addActionListener(e -> cerrarSesion());

        actualizarTabla();
    }

    public void actualizarTabla() {
        List<Usuario> usuarios = ControladorUsuarios.getInstancia().obtenerTodos();
        String[] columnas = {"Usuario", "Nombre", "Apellido", "Teléfono", "Email"};
        Object[][] data = new Object[usuarios.size()][columnas.length];
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario u = usuarios.get(i);
            data[i][0] = u.getUsername();
            data[i][1] = u.getNombre();
            data[i][2] = u.getApellido();
            data[i][3] = u.getTelefono();
            data[i][4] = u.getEmail();
        }
        tabla.setModel(new javax.swing.table.DefaultTableModel(data, columnas) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        });
    }

    private void abrirActualizarSeleccionado() {
        int row = tabla.getSelectedRow();
        if (row < 0) {
            mostrarError("Seleccione un usuario en la tabla.");
            return;
        }
        String username = String.valueOf(tabla.getValueAt(row, 0));
        Usuario u = ControladorUsuarios.getInstancia().buscar(username);
        if (u == null) {
            mostrarError("El usuario seleccionado ya no existe.");
            actualizarTabla();
            return;
        }
        new VentanaActualizar(this, u);
    }

    private void eliminarSeleccionado() {
        int row = tabla.getSelectedRow();
        if (row < 0) {
            mostrarError("Seleccione un usuario en la tabla.");
            return;
        }
        String username = String.valueOf(tabla.getValueAt(row, 0));
        if (username.equalsIgnoreCase("admin")) {
            mostrarError("No se puede eliminar el usuario 'admin'.");
            return;
        }
        int ok = JOptionPane.showConfirmDialog(
                this,
                "¿Seguro que desea eliminar al usuario '" + username + "'?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );
        if (ok != JOptionPane.YES_OPTION) return;
        ControladorUsuarios.getInstancia().eliminar(username);
        actualizarTabla();
    }

    private void cerrarSesion() {
        dispose();
        new VentanaLogin();
    }
}


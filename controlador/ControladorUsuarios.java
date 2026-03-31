package controlador;

import modelo.Gestionable;
import modelo.Usuario;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador que gestiona la lista de usuarios del sistema.
 *
 * Patrones de diseño implementados:
 *  - Singleton: garantiza una única instancia del controlador en toda la app.
 *  - Implementa la interfaz Gestionable (Polimorfismo / Abstracción).
 */
public class ControladorUsuarios implements Gestionable {

    // ── Singleton ──────────────────────────────────────────────────────────
    private static ControladorUsuarios instancia;

    /** Devuelve la única instancia del controlador (Singleton). */
    public static ControladorUsuarios getInstancia() {
        if (instancia == null) {
            instancia = new ControladorUsuarios();
        }
        return instancia;
    }

    /** Constructor privado para evitar instanciación externa. */
    private ControladorUsuarios() {
        usuarios = new ArrayList<>();
        // Usuario de prueba incluido por defecto
        usuarios.add(UsuarioFactory.crearUsuario(
                "admin", "Administrador", "Sistema",
                "8095550000", "admin@sistema.com", "admin123"));
    }

    // ── Estado ────────────────────────────────────────────────────────────
    private final List<Usuario> usuarios;

    // ── Implementación de Gestionable ─────────────────────────────────────

    /**
     * Agrega un nuevo usuario si el username no está en uso.
     * @return true si se agregó; false si ya existía.
     */
    @Override
    public boolean agregar(Usuario usuario) {
        if (buscar(usuario.getUsername()) != null) {
            return false; // username duplicado
        }
        usuarios.add(usuario);
        return true;
    }

    /**
     * Elimina el usuario con el username indicado.
     * @return true si se eliminó; false si no se encontró.
     */
    @Override
    public boolean eliminar(String username) {
        return usuarios.removeIf(u -> u.getUsername().equalsIgnoreCase(username));
    }

    /**
     * Reemplaza los datos de un usuario existente.
     * @return true si se actualizó; false si no se encontró.
     */
    @Override
    public boolean actualizar(String username, Usuario usuarioActualizado) {
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getUsername().equalsIgnoreCase(username)) {
                usuarios.set(i, usuarioActualizado);
                return true;
            }
        }
        return false;
    }

    /**
     * Busca un usuario por username (case-insensitive).
     * @return el Usuario encontrado o null.
     */
    @Override
    public Usuario buscar(String username) {
        for (Usuario u : usuarios) {
            if (u.getUsername().equalsIgnoreCase(username)) {
                return u;
            }
        }
        return null;
    }

    // ── Métodos adicionales ───────────────────────────────────────────────

    /**
     * Valida credenciales de login.
     * @return true si username y password coinciden.
     */
    public boolean validarCredenciales(String username, String password) {
        Usuario u = buscar(username);
        return u != null && u.getPassword().equals(password);
    }

    /**
     * Devuelve una copia de la lista completa de usuarios.
     */
    public List<Usuario> obtenerTodos() {
        return new ArrayList<>(usuarios);
    }

    /**
     * Indica si un email ya está registrado.
     */
    public boolean emailExiste(String email) {
        return usuarios.stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
    }
}
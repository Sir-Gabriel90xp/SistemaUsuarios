package modelo;

/**
 * Interfaz que define las operaciones básicas de gestión.
 * Aplica Abstracción y permite Polimorfismo en el controlador.
 */
public interface Gestionable {

    /**
     * Agrega un elemento al sistema.
     */
    boolean agregar(Usuario usuario);

    /**
     * Elimina un elemento del sistema por su username.
     */
    boolean eliminar(String username);

    /**
     * Actualiza un elemento existente en el sistema.
     */
    boolean actualizar(String username, Usuario usuarioActualizado);

    /**
     * Busca un elemento por su username.
     */
    Usuario buscar(String username);
}
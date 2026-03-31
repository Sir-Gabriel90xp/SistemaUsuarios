package controlador;

import modelo.Usuario;

public class UsuarioFactory {
    private UsuarioFactory() {}

    public static Usuario crearUsuario(String username, String nombre, String apellido,
                                       String telefono, String email, String password) {
        return new Usuario(
                username.trim(),
                nombre.trim(),
                apellido.trim(),
                telefono.trim(),
                email.trim(),
                password
        );
    }
}


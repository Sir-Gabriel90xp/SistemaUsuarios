package modelo;

/**
 * Clase que representa a un Usuario del sistema.
 */
public class Usuario {
    private String username;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private String password;

    public Usuario(String username, String nombre, String apellido,
                   String telefono, String email, String password) {
        this.username = username;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.email = email;
        this.password = password;
    }

    public String getUsername() { return username; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getTelefono() { return telefono; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }

    public void setUsername(String username) { this.username = username; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }

    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
}



package co.unicauca.workflow.domain.entities;

/**
 *
 * @author User
 */
public class User {
    private int idUsuario;
    private String name;
    private String lastname;
    private String phone;
    private String email;
    private String password;
    private enumRol rol;
    private enumProgram program;

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public enumRol getRol() {
        return rol;
    }

    public void setRol(enumRol rol) {
        this.rol = rol;
    }

    public enumProgram getProgram() {
        return program;
    }

    public void setProgram(enumProgram program) {
        this.program = program;
    }
  //constrcutor para registrar
    public User(String name, String lastname, String phone, String email, String password, enumRol rol, enumProgram program) {
        this.name = name;
        this.lastname = lastname;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.program = program;
    }
//para leer de la BD
    public User(int idUsuario, String name, String lastname, String phone, String email, String password, enumRol rol, enumProgram program) {
        this.idUsuario = idUsuario;
        this.name = name;
        this.lastname = lastname;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.program = program;
    }

    public User() {
    }

    
    
}

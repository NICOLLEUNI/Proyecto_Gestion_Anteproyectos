/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.unicauca.workflow.domain.entities;

import co.unicauca.workflow.domain.exceptions.ValidationException;
import java.util.EnumSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
public class Persona {
    
    private int idUsuario;
    private String name;
    private String lastname;
    private String phone;
    private String email;
    private String password;  
    private EnumSet<enumRol> roles; 

    public Persona(String name, String lastname, String phone, String email, String password) throws ValidationException {
       
        this.name = name;
        this.lastname = lastname;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.roles = EnumSet.noneOf(enumRol.class); 
        
        validarCampos();
    }

    public Persona(int idUsuario, String name, String lastname, String phone, String email, String password) {
        this.idUsuario = idUsuario;
        this.name = name;
        this.lastname = lastname;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.roles = EnumSet.noneOf(enumRol.class);
    }
    // Agregar este constructor a tu clase Persona
public Persona(String name, String lastname, String phone, String email, String password, EnumSet<enumRol> roles) throws ValidationException {
    this.name = name;
    this.lastname = lastname;
    this.phone = phone;
    this.email = email;
    this.password = password;
    this.roles = (roles != null) ? EnumSet.copyOf(roles) : EnumSet.noneOf(enumRol.class);
    validarCampos();
}
    
     /**
     * Método que valida los campos de la entidad
     * @throws ValidationException si encuentra errores
     */ 
    /**private void validarCampos() throws ValidationException {
        List<String> errores = new ArrayList<>();

        // Validaciones básicas
        
        if (name == null || name.trim().isEmpty()) {
            errores.add("El nombre es obligatorio.");
        }
        if (lastname == null || lastname.trim().isEmpty()) {
            errores.add("El apellido es obligatorio.");
        }
        if (phone == null || phone.trim().isEmpty()) {
            errores.add("El teléfono es obligatorio.");
        }
        if (email == null || email.trim().isEmpty()) {
            errores.add("El correo electrónico es obligatorio.");
        }
        if (password == null || password.trim().isEmpty()) {
            errores.add("La contraseña es obligatoria.");
        } 
        if (roles == null || roles.isEmpty()) {
            errores.add("Debe asignarse al menos un rol al usuario.");
        }

        // Si hay errores, lanzamos la excepción
        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }
    }*/


private void validarCampos() throws ValidationException {
    List<String> errores = new ArrayList<>();

    if (name == null || name.trim().isEmpty()) {
        errores.add("El nombre es obligatorio.");
    }
    if (lastname == null || lastname.trim().isEmpty()) {
        errores.add("El apellido es obligatorio.");
    }
    if (email == null || email.trim().isEmpty()) {
        errores.add("El correo electrónico es obligatorio.");
    }
    if (password == null || password.trim().isEmpty()) {
        errores.add("La contraseña es obligatoria.");
    }

    // teléfono opcional: si se provee, validar formato
    if (phone != null && !phone.trim().isEmpty()) {
        if (!phone.matches("\\d+")) {
            errores.add("El teléfono debe contener solo dígitos.");
        }
    }

    // NOTA: validación de roles la hace el Service (no la exigimos aquí)
    if (!errores.isEmpty()) {
        throw new ValidationException(errores);
    }
}


    public Persona() {
    }


    
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
    public void addRol(enumRol rol) {
        roles.add(rol);
    }

    public void removeRol(enumRol rol) {
        roles.remove(rol);
    }

    public boolean tieneRol(enumRol rol) {
        return roles.contains(rol);
    }

    public EnumSet<enumRol> getRoles() {
        return roles;
    }
    public void setRoles(EnumSet<enumRol> roles) {
    if (roles == null) {
        this.roles = EnumSet.noneOf(enumRol.class); // inicializamos vacío si viene null
    } else {
        this.roles = EnumSet.copyOf(roles); // copiamos para evitar referencias externas
    }
}
    
    
}

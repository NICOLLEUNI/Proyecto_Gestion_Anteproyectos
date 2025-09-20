/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.unicauca.workflow.domain.entities;

import co.unicauca.workflow.domain.service.PersonaService;
import java.util.EnumSet;

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

    public Persona(int idUsuario, String name, String lastname, String phone, String email, String password) {
        this.idUsuario = idUsuario;
        this.name = name;
        this.lastname = lastname;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.roles = EnumSet.noneOf(enumRol.class); 
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
    
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.unicauca.workflow.domain.service;

/**
 *
 * @author User
 */
public class PersonaService {
    
    //crear el metodo de cerrar sesion
    
    //RICARDO - implementar los metodos de inicio y registro. 
    //Implementar el inicio de sesion con la clase persona y sus clases hijas 
    
    //implementar el registro de usuarios: docente, estudiante o coordinador
    
    
    public boolean validatePassword(String password) {
    if (password == null) return false;
    // min 6, al menos una minúscula, una mayúscula, un dígito y un caracter especial
    String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d])[A-Za-z\\d\\W_]{6,}$";
    return password.matches(regex);
    }

    
    
    /**
     * Valida que el email pertenezca al dominio institucional.
     */
    public boolean validateEmail(String email) {
        String regex = "^[A-Za-z0-9._%+-]+@unicauca\\.edu\\.co$";
        return email != null && email.matches(regex);
    }
}

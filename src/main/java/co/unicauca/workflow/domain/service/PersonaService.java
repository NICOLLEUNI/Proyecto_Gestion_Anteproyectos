/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.unicauca.workflow.domain.service;

import co.unicauca.workflow.access.IPersonaRepository;
import co.unicauca.workflow.domain.entities.Persona;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author User
 */
public class PersonaService {
    
    
    
    
    
    //crear el metodo de cerrar sesion
    
    //RICARDO - implementar los metodos de inicio y registro. 
    //Implementar el inicio de sesion con la clase persona y sus clases hijas 
    
    //implementar el registro de usuarios: docente, estudiante o coordinador
        private IPersonaRepository repository;

    /**
     * Inyección de dependencias en el constructor. Ya no conviene que el mismo
     * servicio cree un repositorio concreto
     *
     * @param repository una clase hija de IProductRepository
     */
    public PersonaService(IPersonaRepository repository) {
        this.repository = repository;
    }

    
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
    
    public Persona authenticateUser(String email, String password) {
    if (email == null || email.isBlank() || password == null || password.isBlank()) {
        return null;
    }

    // Normalizar email
    String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);

    // Hashear la contraseña ingresada
    String hashedPassword = hashPassword(password);
    if (hashedPassword == null) {
        return null;
    }

    // Obtener todas las personas desde el repositorio
    List<Persona> personas = repository.list();
    for (Persona p : personas) {
        if (p.getEmail() != null
                && p.getEmail().equalsIgnoreCase(normalizedEmail)
                && p.getPassword().equals(hashedPassword)) {
            return p; // 👈 Retorna el objeto completo (puede ser Estudiante, Docente, Coordinador)
        }
    }
    return null;
}

/**
 * Método auxiliar para hashear la contraseña.
 * Puedes usar el mismo algoritmo que en UserService para mantener consistencia.
 */
private String hashPassword(String password) {
    try {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hashedBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    } catch (NoSuchAlgorithmException e) {
        return null;
    }
}

}

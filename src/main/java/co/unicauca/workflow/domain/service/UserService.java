package co.unicauca.workflow.domain.service;

import java.util.Locale; // 
import co.unicauca.workflow.domain.entities.enumRol;
import co.unicauca.workflow.domain.entities.enumProgram;

import co.unicauca.workflow.domain.entities.User;
import co.unicauca.workflow.access.IUserRepository;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class UserService {

    // Dependencia hacia una abstracción, no a una implementación concreta.
    private final IUserRepository userRepository;

    /**
     * Inyección de dependencias en el constructor.
     *
     * @param userRepository Implementación concreta de IUserRepository.
     */
    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Guarda un nuevo usuario en el repositorio.
     */
public boolean saveUser(String nombre, String apellidos, int celular,
                        String email, String password, enumRol rol, enumProgram program) {
    if (nombre == null || nombre.isBlank()
            || apellidos == null || apellidos.isBlank()
            || email == null || email.isBlank()
            || password == null || password.isBlank()) {
        return false;
    }

    String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);
    if (!validateEmail(normalizedEmail)) {
        return false;
    }

    if (!validatePassword(password)) {
        return false;
    }

    String hashedPassword = hashPassword(password);
    if (hashedPassword == null) {
        return false;
    }

    // Crear objeto User
    User user = new User(nombre, apellidos, celular, normalizedEmail, hashedPassword, rol, program);

    return userRepository.save(user);
}


    
    
    /**
     * Autentica a un usuario verificando email y contraseña.
     */
 

    
    
    public User authenticateUser (String email, String password) {
    if (email == null || email.isBlank() || password == null || password.isBlank()) {
        return null;
    }

    String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);
    String hashedPassword = hashPassword(password);
    if (hashedPassword == null) {
        return null;
    }

    List<User> users = userRepository.list();
    for (User u : users) {
        if (u.getEmail() != null
                && u.getEmail().equals(normalizedEmail)
                && u.getPassword().equals(hashedPassword)) {
            return u; // 👈 Retorna el usuario completo
        }
    }
    return null;
}
    /**
     * Encripta una contraseña en SHA-256.
     */
    public String hashPassword(String password) {
        if (password == null) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error: Algoritmo SHA-256 no encontrado.", e);
        }
    }

    /**
     * Valida la fortaleza de la contraseña.
     */
   
public boolean validatePassword(String password) {
    if (password == null) return false;
    // min 6, al menos una minúscula, una mayúscula, un dígito y un caracter especial
    String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$";
    return password.matches(regex);
}

    
    
    /**
     * Valida que el email pertenezca al dominio institucional.
     */
    public boolean validateEmail(String email) {
        String regex = "^[A-Za-z0-9._%+-]+@unicauca\\.edu\\.co$";
        return email != null && email.matches(regex);
    }
    /**
 /**
 * Verifica si ya existe un usuario con el mismo email.
 */
public boolean userExists(String email) {
    if (email == null || email.isBlank()) {
        return false;
    }
    String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);

    List<User> users = userRepository.list();
    for (User u : users) {
        if (u.getEmail() != null && u.getEmail().equals(normalizedEmail)) {
            return true;
        }
    }
    return false;
}

}

    
    
    
 

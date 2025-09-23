package co.unicauca.workflow.domain.service;

import java.util.Locale; // 
import co.unicauca.workflow.domain.entities.enumRol;
import co.unicauca.workflow.domain.entities.enumProgram;
import co.unicauca.workflow.domain.exceptions.ValidationException;
import co.unicauca.workflow.domain.entities.User;
import co.unicauca.workflow.access.IUserRepository;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
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
     * @brief Guarda un nuevo usuario en el repositorio.
     *
     * Este método recibe los datos de un usuario en forma de parámetros, 
     * valida que sean correctos (campos obligatorios no vacíos, formato 
     * de email válido y contraseña fuerte), encripta la contraseña 
     * utilizando SHA-256 y finalmente delega el guardado al 
     * repositorio de usuarios.
     *
     * @param nombre    Nombre del usuario.
     * @param apellidos Apellidos del usuario.
     * @param celular   Teléfono del usuario (puede ser null o vacío).
     * @param email     Correo electrónico del usuario (se valida y se normaliza a minúsculas).
     * @param password  Contraseña en texto plano (se valida y luego se encripta con SHA-256).
     * @param rol       Rol del usuario (ej. ESTUDIANTE, DOCENTE).
     * @param program   Programa académico al que pertenece el usuario.
     *
     * @return true si el usuario fue validado correctamente y almacenado en el repositorio;
     *         false si falló alguna validación o si no pudo guardarse.
     *
     * @note La contraseña nunca se almacena en texto plano, siempre en forma de hash SHA-256.
     */
public boolean saveUser(String nombre, String apellidos, String celular,
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
     * @brief Autentica a un usuario en el sistema.
     *
     * Este método recibe un correo y una contraseña, valida que no estén vacíos,
     * normaliza el correo a minúsculas y encripta la contraseña con SHA-256.
     * Luego busca en el repositorio un usuario cuyo correo y contraseña coincidan
     * con los valores proporcionados.
     *
     * @param email    Correo electrónico ingresado por el usuario.
     * @param password Contraseña en texto plano ingresada por el usuario.
     *
     * @return El objeto {@link User} correspondiente al usuario autenticado si las credenciales
     *         son correctas; o {@code null} si las credenciales son inválidas o el usuario no existe.
     *
     * @note La comparación de contraseñas se realiza utilizando el hash SHA-256. 
     *       Nunca se comparan contraseñas en texto plano.
     *
     * @see #hashPassword(String) Método utilizado para generar el hash de la contraseña ingresada.
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
     * @brief Genera un hash SHA-256 a partir de una contraseña.

     *
     * @param password Contraseña en texto plano que se desea encriptar.
     *
     * @return Cadena hexadecimal representando el hash SHA-256 de la contraseña;
     *         o {@code null} si la contraseña es {@code null}.
     *
     * @throws RuntimeException Si el algoritmo SHA-256 no está disponible en la JVM.
     *
     * @note Este método se utiliza en la autenticación de usuarios y
     *       en el guardado de nuevos usuarios, garantizando que las contraseñas
     *       nunca se almacenen ni se comparen en texto plano.
     *
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

    
    
    
 

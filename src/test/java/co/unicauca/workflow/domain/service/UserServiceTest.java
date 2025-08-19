/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package co.unicauca.workflow.domain.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import co.unicauca.workflow.domain.entities.User;
import co.unicauca.workflow.domain.service.UserService;
import co.unicauca.workflow.access.IUserRepository;
/**
 *
 * @author Usuario
 */
public class UserServiceTest {

    private UserService instance;

    @BeforeEach
    public void setUp() {
        // Repositorio de prueba en memoria
        IUserRepository repo = new IUserRepository() {
            @Override
            public boolean save(User user) {
                return user != null;
            }

            /*@Override
            public User findByEmail(String email) {
                // para authenticateUser, simulamos un usuario válido
                if ("juan.perez@unicauca.edu.co".equals(email)) {
                    return new User("Juan", "Pérez", 123456789, email, "Abc123!@", null, null);
                }
                return null;
            }*/
            @Override
        public List<User> list() {
            // Para la prueba devolvemos una lista vacía
            return new ArrayList<>();
        }
        };

        instance = new UserService(repo);
    }

    /**
     * Test of hashPassword method, of class UserService.
     */
    @Test
    public void testHashPassword() {
        System.out.println("hashPassword");

        // Caso 1: misma contraseña, mismo hash
        String hash1 = instance.hashPassword("MiPass123!");
        String hash2 = instance.hashPassword("MiPass123!");
        assertEquals(hash1, hash2, "El hash de la misma contraseña debe ser igual");

        // Caso 2: contraseñas distintas, hashes distintos
        String hash3 = instance.hashPassword("OtraPass456!");
        assertNotEquals(hash1, hash3, "Contraseñas distintas no deben dar el mismo hash");

        // Caso 3: null → retorna null
        assertNull(instance.hashPassword(null), "Si la contraseña es null, el hash también debe ser null");

        // Caso 4: hash diferente al texto original
        String hash4 = instance.hashPassword("MiPass123!");
        assertNotEquals("MiPass123!", hash4, "El hash no debe ser igual al texto original");
    }

    /**
     * Test of validatePassword method, of class UserService.
     */
    @Test
    public void testValidatePassword() {
        System.out.println("validatePassword");

        // caso válido
        assertTrue(instance.validatePassword("Abc123!@"));

        // caso inválido (muy corta)
        assertFalse(instance.validatePassword("Ab1!"));

        // caso inválido (sin mayúsculas)
        assertFalse(instance.validatePassword("abc123!@"));

        // caso inválido (sin minúsculas)
        assertFalse(instance.validatePassword("ABC123!@"));

        // caso inválido (sin números)
        assertFalse(instance.validatePassword("Abcdef!@"));

        // caso inválido (sin caracteres especiales)
        assertFalse(instance.validatePassword("Abc12345"));

        // caso inválido (null)
        assertFalse(instance.validatePassword(null));
    }

    /**
     * Test of validateEmail method, of class UserService.
     */
    @Test
    public void testValidateEmail() {
        System.out.println("validateEmail");

        // Caso válido
        assertTrue(instance.validateEmail("juan.perez@unicauca.edu.co"));

        // Caso inválido (otro dominio)
        assertFalse(instance.validateEmail("juan@gmail.com"));

        // Caso inválido (mal formato)
        assertFalse(instance.validateEmail("@unicauca.edu.co"));

        // Caso inválido (null)
        assertFalse(instance.validateEmail(null));
    }

    /**
     * Test of saveUser method, of class UserService.
     */
    @Test
    public void testSaveUser() {
        System.out.println("saveUser");

        // Caso válido: usuario con todos los campos correctos
        User userValido = new User("Juan", "Pérez", 123456789, "juan.perez@unicauca.edu.co", "Abc123!@", null, null);
        boolean resultValido = instance.saveUser(userValido);
        assertTrue(resultValido, "Debe guardar correctamente un usuario válido");

        // Caso inválido: usuario null
        boolean resultNull = instance.saveUser(null);
        assertFalse(resultNull, "No debe permitir guardar un usuario null");

        // Caso inválido: email incorrecto
        User userEmailInvalido = new User("Ana", "Lopez", 987654321, "ana@gmail.com", "Abc123!@", null, null);
        boolean resultEmail = instance.saveUser(userEmailInvalido);
        assertFalse(resultEmail, "No debe permitir guardar un usuario con email inválido");

        // Caso inválido: contraseña débil
        User userPassInvalida = new User("Pedro", "Martinez", 456123789, "pedro.martinez@unicauca.edu.co", "123", null, null);
        boolean resultPass = instance.saveUser(userPassInvalida);
        assertFalse(resultPass, "No debe permitir guardar un usuario con contraseña inválida");
    }

    /**
     * Test of authenticateUser method, of class UserService.
     */
    @Test
    public void testAuthenticateUser() {
        System.out.println("authenticateUser");

        // Caso válido: usuario existente con credenciales correctas
        boolean resultValido = instance.authenticateUser("juan.perez@unicauca.edu.co", "Abc123!@");
        assertTrue(resultValido, "Debe autenticar correctamente con credenciales válidas");

        // Caso inválido: usuario existente con contraseña incorrecta
        boolean resultPassIncorrecta = instance.authenticateUser("juan.perez@unicauca.edu.co", "ClaveErrada!");
        assertFalse(resultPassIncorrecta, "No debe autenticar con contraseña incorrecta");

        // Caso inválido: usuario inexistente
        boolean resultNoExiste = instance.authenticateUser("otro@unicauca.edu.co", "Abc123!@");
        assertFalse(resultNoExiste, "No debe autenticar un usuario inexistente");

        // Caso inválido: email null
        boolean resultEmailNull = instance.authenticateUser(null, "Abc123!@");
        assertFalse(resultEmailNull, "No debe autenticar si el email es null");

        // Caso inválido: password null
        boolean resultPassNull = instance.authenticateUser("juan.perez@unicauca.edu.co", null);
        assertFalse(resultPassNull, "No debe autenticar si la contraseña es null");
    }
}

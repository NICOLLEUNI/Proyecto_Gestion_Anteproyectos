/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package co.unicauca.workflow.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import co.unicauca.workflow.domain.entities.User;
import co.unicauca.workflow.access.IUserRepository;

/**
 * Tests de UserService con repositorio en memoria.
 * - Se usa un repo fake que realmente almacena en una lista interna.
 * - En cada setUp() se siembra un usuario válido para probar authenticateUser.
 */
public class UserServiceTest {

    /**
     * Repositorio en memoria para pruebas. No toca BD real.
     */
    private static class InMemoryRepo implements IUserRepository {
        private final List<User> store = new ArrayList<>();

        @Override
        public boolean save(User user) {
            if (user == null) return false;
            store.add(user);
            return true;
        }

        @Override
        public List<User> list() {
            // Devolvemos copia para no exponer la lista interna
            return new ArrayList<>(store);
        }
    }

    private UserService instance;
    private InMemoryRepo repo;

    @BeforeEach
    public void setUp() {
        repo = new InMemoryRepo();
        instance = new UserService(repo);

        // Sembrar un usuario válido como lo esperan los tests de authenticateUser
        // OJO: saveUser hashea la contraseña y valida email/contraseña.
        User seeded = new User(
            "Juan",
            "Pérez",
            "123456789", // si tu User usa String para phone, cambia a "123456789"
            "juan.perez@unicauca.edu.co",
            "Abc123!@", // en texto plano, se hashea al guardar
            null,
            null
        );
        assertTrue(instance.saveUser(seeded), "No se pudo sembrar el usuario de prueba");
    }

    // ====================== Tests de hashPassword ======================

    @Test
    public void testHashPassword() {
        String hash1 = instance.hashPassword("MiPass123!");
        String hash2 = instance.hashPassword("MiPass123!");
        assertEquals(hash1, hash2, "El hash de la misma contraseña debe ser igual");

        String hash3 = instance.hashPassword("OtraPass456!");
        assertNotEquals(hash1, hash3, "Contraseñas distintas no deben dar el mismo hash");

        assertNull(instance.hashPassword(null), "Si la contraseña es null, el hash también debe ser null");

        String hash4 = instance.hashPassword("MiPass123!");
        assertNotEquals("MiPass123!", hash4, "El hash no debe ser igual al texto original");
    }

    // ====================== Tests de validatePassword ======================

    @Test
    public void testValidatePassword() {
        // válido: min 6, mayúscula, minúscula, dígito y especial
        assertTrue(instance.validatePassword("Abc123!@"));

        // inválidos
        assertFalse(instance.validatePassword("Ab1!"));       // muy corta
        assertFalse(instance.validatePassword("abc123!@"));   // sin mayúscula
        assertFalse(instance.validatePassword("ABC123!@"));   // sin minúscula
        assertFalse(instance.validatePassword("Abcdef!@"));   // sin dígitos
        assertFalse(instance.validatePassword("Abc12345"));   // sin especial
        assertFalse(instance.validatePassword(null));         // null
    }

    // ====================== Tests de validateEmail ======================

    @Test
    public void testValidateEmail() {
        assertTrue(instance.validateEmail("juan.perez@unicauca.edu.co"));

        assertFalse(instance.validateEmail("juan@gmail.com"));     // otro dominio
        assertFalse(instance.validateEmail("@unicauca.edu.co"));   // mal formato
        assertFalse(instance.validateEmail(null));                 // null
    }

    // ====================== Tests de saveUser ======================

    @Test
    public void testSaveUser() {
        // válido
        User userValido = new User("Ana", "López", "987654321", "ana.lopez@unicauca.edu.co", "Xy1!xy", null, null);
        assertTrue(instance.saveUser(userValido), "Debe guardar correctamente un usuario válido");

        // null
        assertFalse(instance.saveUser(null), "No debe permitir guardar un usuario null");

        // email inválido
        User userEmailInvalido = new User("Pedro", "Ruiz", "555111222", "pedro@gmail.com", "Abc123!@", null, null);
        assertFalse(instance.saveUser(userEmailInvalido), "No debe permitir guardar un usuario con email inválido");

        // contraseña débil
        User userPassInvalida = new User("Carla", "Mora", "444333222", "carla.mora@unicauca.edu.co", "123", null, null);
        assertFalse(instance.saveUser(userPassInvalida), "No debe permitir guardar un usuario con contraseña inválida");
    }

    // ====================== Tests de authenticateUser ======================

    @Test
    public void testAuthenticateUser() {
        // válido: el usuario sembrado en setUp()
        assertTrue(
            instance.authenticateUser("juan.perez@unicauca.edu.co", "Abc123!@"),
            "Debe autenticar correctamente con credenciales válidas"
        );

        // contraseña incorrecta
        assertFalse(
            instance.authenticateUser("juan.perez@unicauca.edu.co", "ClaveErrada!"),
            "No debe autenticar con contraseña incorrecta"
        );

        // usuario inexistente
        assertFalse(
            instance.authenticateUser("otro@unicauca.edu.co", "Abc123!@"),
            "No debe autenticar un usuario inexistente"
        );

        // email null
        assertFalse(
            instance.authenticateUser(null, "Abc123!@"),
            "No debe autenticar si el email es null"
        );

        // password null
        assertFalse(
            instance.authenticateUser("juan.perez@unicauca.edu.co", null),
            "No debe autenticar si la contraseña es null"
        );
    }
}

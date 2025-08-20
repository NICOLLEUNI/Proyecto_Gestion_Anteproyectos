/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package co.unicauca.workflow.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import co.unicauca.workflow.domain.entities.enumRol;
import co.unicauca.workflow.domain.entities.enumProgram;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import co.unicauca.workflow.domain.entities.User;
import co.unicauca.workflow.access.IUserRepository;
import co.unicauca.workflow.domain.entities.enumRol;

/**
 * Tests de UserService con repositorio en memoria.
 * - Se usa un repo fake que realmente almacena en una lista interna.
 * - En cada setUp() se siembra un usuario válido para probar authenticateUser.
 */
public class UserServiceTest {

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
            return new ArrayList<>(store);
        }
    }

    private UserService instance;
    private InMemoryRepo repo;

    @BeforeEach
    public void setUp() {
        repo = new InMemoryRepo();
        instance = new UserService(repo);

        User seeded = new User(
            "Juan",
            "Pérez",
            "123456789",
            "juan.perez@unicauca.edu.co",
            "Abc123!@",
            enumRol.ESTUDIANTE,
            enumProgram.INGENIERIA_SISTEMAS
        );

        assertTrue(instance.saveUser(
            seeded.getName(),
            seeded.getLastname(),
            seeded.getPhone(),
            seeded.getEmail(),
            seeded.getPassword(),
            seeded.getRol(),
            seeded.getProgram()
        ), "No se pudo sembrar el usuario de prueba");
    }

    // ====================== hashPassword ======================

    @Test
    void testHashSamePasswordGeneratesSameHash() {
        assertEquals(instance.hashPassword("MiPass123!"), instance.hashPassword("MiPass123!"));
    }

    @Test
    void testHashDifferentPasswordsGenerateDifferentHash() {
        assertNotEquals(instance.hashPassword("MiPass123!"), instance.hashPassword("OtraPass456!"));
    }

    @Test
    void testHashNullPasswordReturnsNull() {
        assertNull(instance.hashPassword(null));
    }

    @Test
    void testHashIsNotPlainText() {
        assertNotEquals("MiPass123!", instance.hashPassword("MiPass123!"));
    }

    // ====================== validatePassword ======================

    @Test
    void testValidPassword() {
        assertTrue(instance.validatePassword("Abc123!@"));
    }

    @Test
    void testPasswordTooShort() {
        assertFalse(instance.validatePassword("Ab1!"));
    }

    @Test
    void testPasswordWithoutUppercase() {
        assertFalse(instance.validatePassword("abc123!@"));
    }

    @Test
    void testPasswordWithoutLowercase() {
        assertFalse(instance.validatePassword("ABC123!@"));
    }

    @Test
    void testPasswordWithoutDigit() {
        assertFalse(instance.validatePassword("Abcdef!@"));
    }

    @Test
    void testPasswordWithoutSpecialCharacter() {
        assertFalse(instance.validatePassword("Abc12345"));
    }

    @Test
    void testPasswordNull() {
        assertFalse(instance.validatePassword(null));
    }

    // ====================== validateEmail ======================

    @Test
    void testValidEmail() {
        assertTrue(instance.validateEmail("juan.perez@unicauca.edu.co"));
    }

    @Test
    void testEmailWithWrongDomain() {
        assertFalse(instance.validateEmail("juan@gmail.com"));
    }

    @Test
    void testEmailWithBadFormat() {
        assertFalse(instance.validateEmail("@unicauca.edu.co"));
    }

    @Test
    void testEmailNull() {
        assertFalse(instance.validateEmail(null));
    }

    // ====================== saveUser ======================

    @Test
    void testSaveValidUser() {
        User userValido = new User("Ana", "López", "987654321",
            "ana.lopez@unicauca.edu.co", "Xy1!xy",
            enumRol.ESTUDIANTE, enumProgram.INGENIERIA_SISTEMAS);

        assertTrue(instance.saveUser(
            userValido.getName(), userValido.getLastname(), userValido.getPhone(),
            userValido.getEmail(), userValido.getPassword(), userValido.getRol(),
            userValido.getProgram()
        ));
    }

    @Test
    void testSaveNullUser() {
        assertFalse(instance.saveUser(null, null, null, null, null, null, null));
    }

    @Test
    void testSaveUserWithInvalidEmail() {
        User userEmailInvalido = new User("Pedro", "Ruiz", "555111222",
            "pedro@gmail.com", "Abc123!@",
            enumRol.ESTUDIANTE, enumProgram.INGENIERIA_ELECTRONICA);

        assertFalse(instance.saveUser(
            userEmailInvalido.getName(), userEmailInvalido.getLastname(),
            userEmailInvalido.getPhone(), userEmailInvalido.getEmail(),
            userEmailInvalido.getPassword(), userEmailInvalido.getRol(),
            userEmailInvalido.getProgram()
        ));
    }

    @Test
    void testSaveUserWithInvalidPassword() {
        User userPassInvalida = new User("Carla", "Mora", "444333222",
            "carla.mora@unicauca.edu.co", "123",
            enumRol.DOCENTE, enumProgram.AUTOMATICA_INDUSTRIAL);

        assertFalse(instance.saveUser(
            userPassInvalida.getName(), userPassInvalida.getLastname(),
            userPassInvalida.getPhone(), userPassInvalida.getEmail(),
            userPassInvalida.getPassword(), userPassInvalida.getRol(),
            userPassInvalida.getProgram()
        ));
    }

    // ====================== userExists ======================

    @Test
    void testUserExistsWithRegisteredEmail() {
        assertTrue(instance.userExists("juan.perez@unicauca.edu.co"));
    }

    @Test
    void testUserExistsWithUnregisteredEmail() {
        assertFalse(instance.userExists("otro@unicauca.edu.co"));
    }

    @Test
    void testUserExistsWithNullEmail() {
        assertFalse(instance.userExists(null));
    }

    @Test
    void testUserExistsWithEmptyEmail() {
        assertFalse(instance.userExists(""));
    }

    @Test
    void testUserExistsWithUppercaseEmail() {
        assertTrue(instance.userExists("JUAN.PEREZ@UNICAUCA.EDU.CO"));
    }

    // ====================== authenticateUser ======================

    @Test
    void testAuthenticateValidUser() {
        User userValido = instance.authenticateUser("juan.perez@unicauca.edu.co", "Abc123!@");
        assertNotNull(userValido);
    }

    @Test
    void testAuthenticateWithWrongPassword() {
        User userClaveErrada = instance.authenticateUser("juan.perez@unicauca.edu.co", "ClaveErrada!");
        assertNull(userClaveErrada);
    }

    @Test
    void testAuthenticateWithNonExistentUser() {
        User userInexistente = instance.authenticateUser("otro@unicauca.edu.co", "Abc123!@");
        assertNull(userInexistente);
    }

    @Test
    void testAuthenticateWithNullEmail() {
        User userEmailNull = instance.authenticateUser(null, "Abc123!@");
        assertNull(userEmailNull);
    }

    @Test
    void testAuthenticateWithNullPassword() {
        User userPasswordNull = instance.authenticateUser("juan.perez@unicauca.edu.co", null);
        assertNull(userPasswordNull);
    }}


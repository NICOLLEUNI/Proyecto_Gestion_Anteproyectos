/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package co.unicauca.workflow.domain.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Usuario
 */
public class UserServiceTest {
    /*
    public UserServiceTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }
    */

    /**
     * Test of saveUser method, of class UserService.
     */
    
    /*
    @Test
    public void testSaveUser() {
        System.out.println("saveUser");
        UserService instance = new UserService();
        instance.saveUser();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    */

    /**
     * Test of authenticateUser method, of class UserService.
     */
    /*
    @Test
    public void testAuthenticateUser() {
        System.out.println("authenticateUser");
        UserService instance = new UserService();
        instance.authenticateUser();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    */

    /**
     * Test of hashPassword method, of class UserService.
     */
    
    @Test
    public void testHashPassword() {
        System.out.println("hashPassword");
        UserService instance = new UserService();

        //Caso 1: misma contraseña, mismo hash
        String hash1 = instance.hashPassword("MiPass123!");
        String hash2 = instance.hashPassword("MiPass123!");
        assertEquals(hash1, hash2, "El hash de la misma contraseña debe ser igual");

        //Caso 2: contraseñas distintas, hashes distintos
        String hash3 = instance.hashPassword("OtraPass456!");
        assertNotEquals(hash1, hash3, "Contraseñas distintas no deben dar el mismo hash");

        //Caso 3: null → retorna null
        assertNull(instance.hashPassword(null), "Si la contraseña es null, el hash también debe ser null");

        //Caso 4: hash diferente al texto original
        String hash4 = instance.hashPassword("MiPass123!");
        assertNotEquals("MiPass123!", hash4, "El hash no debe ser igual al texto original");
    }

    

    /**
     * Test of validatePassword method, of class UserService.
     */
    
    @Test
    public void testValidatePassword() {
        System.out.println("validatePassword");
        UserService instance = new UserService();
        //caso valido
        assertTrue(instance.validatePassword("Abc123!@"));
        
        //caso invalido (Muy corta)
        assertFalse(instance.validatePassword("Ab1!"));
        
        //caso invalido (Sin mayúsculas)
        assertFalse(instance.validatePassword("abc123!@"));

        //caso invalido (Sin minúsculas)
        assertFalse(instance.validatePassword("ABC123!@"));

        //caso invalido (Sin números)
        assertFalse(instance.validatePassword("Abcdef!@"));

        //caso invalido (Sin caracteres especiales)
        assertFalse(instance.validatePassword("Abc12345"));

        //caso invalido (Null)
        assertFalse(instance.validatePassword(null));
    }
    

    /**
     * Test of validateEmail method, of class UserService.
     */
    
   @Test
    public void testValidateEmail() {
        System.out.println("ValidateEmail");
        UserService instance = new UserService();

        // Caso válido
        assertTrue(instance.validateEmail("juan.perez@unicauca.edu.co"));

        // Caso inválido (otro dominio)
        assertFalse(instance.validateEmail("juan@gmail.com"));

        // Caso inválido (mal formato)
        assertFalse(instance.validateEmail("@unicauca.edu.co"));

        // Caso inválido (null)
        assertFalse(instance.validateEmail(null));
    }

    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package co.unicauca.workflow.access;

import co.unicauca.workflow.domain.entities.Persona;
import co.unicauca.workflow.domain.entities.enumRol;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.EnumSet;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;



public class PersonaRepositoryTest {

    private static PersonaRepository repo;

  @BeforeEach
    public void setUp() {
        repo = new PersonaRepository();
        repo.connect();
    }
    

@AfterEach
void resetDB() {
    if (repo != null) {
        repo.disconnect(); // cerrar conexión principal
    }
    try {
        String url = "jdbc:sqlite:" + System.getProperty("user.dir") + "/BD.db";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            stmt.execute("DELETE FROM PersonaRol");
            stmt.execute("DELETE FROM Persona");
        }
    } catch (SQLException e) {
        System.out.println("⚠️ Error limpiando tablas: " + e.getMessage());
    }
}

    /**
     * Test básico del método save() con roles.
     */
    @Test
    public void testSaveWithRoles() {
        Persona persona = new Persona();
        persona.setName("Juan");
        persona.setLastname("Pérez");
        persona.setPhone("123456789");
        persona.setEmail("juan@example.com");
        persona.setPassword("secret");
        persona.setRoles(EnumSet.of(enumRol.ESTUDIANTE, enumRol.DOCENTE));

        boolean result = repo.save(persona);
        assertTrue(result, "La persona debería guardarse correctamente en la BD");
        assertTrue(persona.getIdUsuario() > 0, "El idUsuario debería generarse automáticamente");
    }

    /**
     * Test guardar sin roles.
     */
    @Test
    public void testSaveWithoutRoles() {
        Persona persona = new Persona();
        persona.setName("Ana");
        persona.setLastname("López");
        persona.setEmail("ana@example.com");
        persona.setPassword("claveSegura");

        boolean result = repo.save(persona);
        assertTrue(result, "Debería permitir guardar una persona sin roles");
    }

    /**
     * Test guardar con datos inválidos (nombre vacío).
     */
    @Test
    public void testSaveInvalidPersona() {
        Persona persona = new Persona();
        persona.setName(""); // inválido
        persona.setLastname("Gómez");
        persona.setEmail("gomez@example.com");
        persona.setPassword("123");

        boolean result = repo.save(persona);
        assertFalse(result, "No debería permitir guardar una persona con nombre vacío");
    }

    /**
     * Test del método list().
     */
    @Test
    public void testList() {
        Persona persona = new Persona();
        persona.setName("Carlos");
        persona.setLastname("Ramírez");
        persona.setEmail("carlos@example.com");
        persona.setPassword("clave123");
        repo.save(persona);

        List<Persona> personas = repo.list();
        assertNotNull(personas, "La lista no debería ser nula");
        assertTrue(personas.size() > 0, "La lista debería contener al menos un registro");
    }

       @Test
      public void testConnect() {
        repo.connect();
        Connection conn = repo.getConnection();
        assertNotNull(conn, "La conexión no debería ser nula después de connect()");
    }
    /**
     * Test de conexión.
     */
    @Test
   public void testGetConnection() {
        Connection conn = repo.getConnection();
        assertNotNull(conn, "Debería retornar la conexión establecida");
    }
     @Test
    public void testDisconnect() {
        repo.disconnect();
        Connection conn = repo.getConnection();
        assertNull(conn, "La conexión debería ser nula después de disconnect()");
    }
}           
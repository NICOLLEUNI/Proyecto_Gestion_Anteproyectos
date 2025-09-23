/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package co.unicauca.workflow.access;

import co.unicauca.workflow.domain.entities.Coordinador;
import co.unicauca.workflow.domain.entities.Departamento;
import co.unicauca.workflow.domain.entities.Facultad;
import java.sql.Connection;
import co.unicauca.workflow.domain.exceptions.ValidationException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CoordinadorRepositoryTest{

    private static CoordinadorRepository repo;

    @BeforeEach
    public void setUp() {
        repo = new CoordinadorRepository();
        repo.connect();

        // Asegurar que existan registros base para FK
        try (Statement stmt = repo.getConnection().createStatement()) {
            stmt.execute("INSERT OR IGNORE INTO Facultad (codFacultad, nombre) VALUES (1, 'Ingeniería')");
            stmt.execute("INSERT OR IGNORE INTO Departamento (codDepartamento, nombre, codFacultad) VALUES (1, 'Sistemas', 1)");
        } catch (SQLException e) {
            fail("Error preparando datos iniciales: " + e.getMessage());
        }
    }

    @AfterEach
    void resetDB() {
        if (repo != null) {
            repo.disconnect();
        }
        try {
            String url = "jdbc:sqlite:" + System.getProperty("user.dir") + "/BD.db";
            try (Connection conn = DriverManager.getConnection(url);
                 Statement stmt = conn.createStatement()) {
                stmt.execute("DELETE FROM Coordinador");
                stmt.execute("DELETE FROM User");
                stmt.execute("DELETE FROM Departamento");
                stmt.execute("DELETE FROM Facultad");
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Error limpiando tablas: " + e.getMessage());
        }
    }

    /**
     * Test guardar un coordinador válido.
     */
    @Test
    public void testSaveCoordinador() throws ValidationException {
        Facultad facultad = new Facultad("Ingeniería");
        facultad.setCodFacultad(1);

        Departamento depto = new Departamento("Sistemas", facultad);
        depto.setCodDepartamento(1);

        Coordinador coord = new Coordinador(0, depto, "Pedro", "Martínez", "12345", "pedro@example.com", "clave123");

        boolean result = repo.save(coord);

        assertTrue(result, "El coordinador debería guardarse correctamente");
        assertTrue(coord.getCodigoCoordinador() > 0, "El código debería generarse automáticamente");
        assertTrue(coord.getIdUsuario() > 0, "El idUsuario debería generarse al guardar como Persona");
    }

    /**
     * Test guardar coordinador sin departamento (FK nula).
     */
    @Test
    public void testSaveCoordinadorWithoutDepartamento() throws ValidationException {
        Coordinador coord = new Coordinador(0, null, "Laura", "Gómez", "98765", "laura@example.com", "claveSegura");

        boolean result = repo.save(coord);

        assertTrue(result, "Debería permitir guardar un coordinador sin departamento");
    }

    /**
     * Test del método list().
     */
    @Test
    public void testListCoordinadores() throws ValidationException {
        Facultad facultad = new Facultad("Ingeniería");
        facultad.setCodFacultad(1);

        Departamento depto = new Departamento("Sistemas", facultad);
        depto.setCodDepartamento(1);

        Coordinador coord = new Coordinador(0, depto, "Carlos", "Ramírez", "55555", "carlos@example.com", "clave123");
        repo.save(coord);

        List<Coordinador> coordinadores = repo.list();
        assertNotNull(coordinadores, "La lista no debería ser nula");
        assertTrue(coordinadores.size() > 0, "La lista debería contener al menos un coordinador");
    }

    /**
     * Test conexión.
     */
    @Test
    public void testConnect() {
        repo.connect();
        Connection conn = repo.getConnection();
        assertNotNull(conn, "La conexión no debería ser nula después de connect()");
    }

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
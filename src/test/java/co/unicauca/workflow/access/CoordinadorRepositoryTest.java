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


public class CoordinadorRepositoryTest {

    private Connection conn;
    private CoordinadorRepository repo;

    @BeforeEach
    void setUp() throws Exception {
        // 🔹 BD en memoria
        conn = DriverManager.getConnection("jdbc:sqlite::memory:");
        repo = new CoordinadorRepository();
        repo.disconnect();

        // Inyectamos conexión en memoria
        var field = CoordinadorRepository.class.getDeclaredField("conn");
        field.setAccessible(true);
        field.set(repo, conn);

        try (Statement stmt = conn.createStatement()) {
            // Crear tablas mínimas
            stmt.execute("CREATE TABLE Persona (" +
                    "idUsuario INTEGER PRIMARY KEY, " +
                    "name TEXT, lastname TEXT, phone TEXT, email TEXT UNIQUE, password TEXT)");

            stmt.execute("CREATE TABLE Facultad (" +
                    "codFacultad INTEGER PRIMARY KEY, nombre TEXT)");

            stmt.execute("CREATE TABLE Departamento (" +
                    "codDepartamento INTEGER PRIMARY KEY, nombre TEXT, codFacultad INTEGER, " +
                    "FOREIGN KEY (codFacultad) REFERENCES Facultad(codFacultad))");

            stmt.execute("CREATE TABLE Coordinador (" +
                    "idUsuario INTEGER PRIMARY KEY, codDepartamento INTEGER NOT NULL, " +
                    "FOREIGN KEY (idUsuario) REFERENCES Persona(idUsuario), " +
                    "FOREIGN KEY (codDepartamento) REFERENCES Departamento(codDepartamento))");
        }

        // Insertar datos base
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("INSERT INTO Persona VALUES (1, 'Carlos', 'Perez', '99999', 'carlos@mail.com', 'clave')");
            stmt.execute("INSERT INTO Facultad VALUES (10, 'Ingeniería')");
            stmt.execute("INSERT INTO Departamento VALUES (20, 'Sistemas', 10)");
        }
    }

    @AfterEach
    void tearDown() throws Exception {
        conn.close();
    }

    @Test
    void testSaveAndList() throws ValidationException {
        Facultad fac = new Facultad("Ingeniería");
        fac.setCodFacultad(10);
        Departamento dep = new Departamento("Sistemas", fac);
        dep.setCodDepartamento(20);

        Coordinador coord = new Coordinador(
                1, dep, "Carlos", "Perez", "99999", "carlos@mail.com", "clave"
        );

        boolean saved = repo.save(coord);
        assertTrue(saved, "El coordinador debería guardarse correctamente");

        List<Coordinador> coordinadores = repo.list();
        assertEquals(1, coordinadores.size());

        Coordinador result = coordinadores.get(0);
        assertEquals("Carlos", result.getName());
        assertEquals("Sistemas", result.getDepartamento().getNombre());
        assertEquals("Ingeniería", result.getDepartamento().getFacultad().getNombre());
    }

    @Test
    void testSaveWithoutPersonaFails() throws ValidationException {
        Facultad fac = new Facultad("Ingeniería");
        fac.setCodFacultad(10);
        Departamento dep = new Departamento("Sistemas", fac);
        dep.setCodDepartamento(20);

        Coordinador coord = new Coordinador(
                99, dep, "Fake", "User", "00000", "fake@mail.com", "clave"
        );

        boolean saved = repo.save(coord);
        assertFalse(saved, "No debería guardarse un coordinador sin persona existente");
    }

    @Test
    void testDuplicateCoordinatorFails() throws ValidationException {
        Facultad fac = new Facultad("Ingeniería");
        fac.setCodFacultad(10);
        Departamento dep = new Departamento("Sistemas", fac);
        dep.setCodDepartamento(20);

        Coordinador coord = new Coordinador(
                1, dep, "Carlos", "Perez", "99999", "carlos@mail.com", "clave"
        );

        assertTrue(repo.save(coord));
        assertFalse(repo.save(coord), "No debería permitir duplicar coordinador con el mismo idUsuario");
    }

    @Test
    void testListWhenEmpty() {
        // Nueva BD en memoria vacía
        assertTrue(repo.list().isEmpty(), "La lista debería estar vacía si no se han guardado coordinadores");
    }
}
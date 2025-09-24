/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package co.unicauca.workflow.access;

import co.unicauca.workflow.domain.entities.Departamento;
import co.unicauca.workflow.domain.entities.Estudiante;
import co.unicauca.workflow.domain.entities.Facultad;
import co.unicauca.workflow.domain.entities.Programa;
import co.unicauca.workflow.domain.exceptions.ValidationException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author User
 */
public class EstudianteRepositoryTest {
    

    private Connection conn;
    private EstudianteRepository repo;

    @BeforeEach
    void setUp() throws Exception {
        // 🔹 BD en memoria para no afectar otros tests
        conn = DriverManager.getConnection("jdbc:sqlite::memory:");
        repo = new EstudianteRepository();
        repo.disconnect();
        // Inyectamos la conexión en memoria
        var field = EstudianteRepository.class.getDeclaredField("conn");
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

            stmt.execute("CREATE TABLE Programa (" +
                    "codPrograma INTEGER PRIMARY KEY, nombre TEXT, codDepartamento INTEGER, " +
                    "FOREIGN KEY (codDepartamento) REFERENCES Departamento(codDepartamento))");

            stmt.execute("CREATE TABLE Estudiante (" +
                    "idUsuario INTEGER PRIMARY KEY, codPrograma INTEGER NOT NULL, " +
                    "FOREIGN KEY (idUsuario) REFERENCES Persona(idUsuario), " +
                    "FOREIGN KEY (codPrograma) REFERENCES Programa(codPrograma))");
        }

        // Insertar datos base
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("INSERT INTO Persona VALUES (1, 'Ana', 'Gomez', '12345', 'ana@mail.com', 'secret')");
            stmt.execute("INSERT INTO Facultad VALUES (10, 'Ingeniería')");
            stmt.execute("INSERT INTO Departamento VALUES (20, 'Sistemas', 10)");
            stmt.execute("INSERT INTO Programa VALUES (30, 'Software', 20)");
        }
    }

    @AfterEach
    void tearDown() throws Exception {
        conn.close();
    }

    @Test
    void testSaveAndList() throws ValidationException {
        // 🔹 Crear estudiante ligado al programa 30
        Facultad fac = new Facultad("Ingeniería");
        fac.setCodFacultad(10);
        Departamento dep = new Departamento("Sistemas", fac);
        dep.setCodDepartamento(20);
        Programa prog = new Programa(30, "Software", dep);

        Estudiante est = new Estudiante(
                1, prog, "Ana", "Gomez", "12345", "ana@mail.com", "secret"
        );

        boolean saved = repo.save(est);
        assertTrue(saved, "El estudiante debería guardarse correctamente");

        List<Estudiante> estudiantes = repo.list();
        assertEquals(1, estudiantes.size(), "Debe existir un estudiante en la lista");

        Estudiante result = estudiantes.get(0);
        assertEquals("Ana", result.getName());
        assertEquals("Software", result.getProgram().getNombrePrograma());
        assertEquals("Sistemas", result.getProgram().getDepartamento().getNombre());
        assertEquals("Ingeniería", result.getProgram().getDepartamento().getFacultad().getNombre());
    }
}
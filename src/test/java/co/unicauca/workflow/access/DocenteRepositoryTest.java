package co.unicauca.workflow.access;

import co.unicauca.workflow.domain.entities.Docente;
import co.unicauca.workflow.domain.entities.Departamento;
import co.unicauca.workflow.domain.entities.Facultad;
import co.unicauca.workflow.domain.exceptions.ValidationException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test independiente para DocenteRepository
 * con BD en memoria.
 */
public class DocenteRepositoryTest {

    private Connection conn;
    private DocenteRepository repo;

    @BeforeEach
    void setUp() throws Exception {
        // 🔹 BD en memoria (aislada de otros tests)
        conn = DriverManager.getConnection("jdbc:sqlite::memory:");
        repo = new DocenteRepository();
        repo.disconnect();

        // Inyectamos la conexión en memoria al repositorio
        var field = DocenteRepository.class.getDeclaredField("conn");
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

            stmt.execute("CREATE TABLE Docente (" +
                    "idUsuario INTEGER PRIMARY KEY, codDepartamento INTEGER NOT NULL, " +
                    "FOREIGN KEY (idUsuario) REFERENCES Persona(idUsuario), " +
                    "FOREIGN KEY (codDepartamento) REFERENCES Departamento(codDepartamento))");
        }

        // Insertar datos base
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("INSERT INTO Persona VALUES (1, 'Juan', 'Pérez', '3120000000', 'juan@mail.com', '12345')");
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
        // 🔹 Crear docente ligado al departamento 20
        Facultad fac = new Facultad("Ingeniería");
        fac.setCodFacultad(10);
        Departamento dep = new Departamento("Sistemas", fac);
        dep.setCodDepartamento(20);

        Docente docente = new Docente(
                1, dep, "Juan", "Pérez", "3120000000", "juan@mail.com", "12345"
        );

        boolean saved = repo.save(docente);
        assertTrue(saved, "El docente debería guardarse correctamente");

        List<Docente> docentes = repo.list();
        assertEquals(1, docentes.size(), "Debe existir un docente en la lista");

        Docente result = docentes.get(0);
        assertEquals("Juan", result.getName());
        assertEquals("Sistemas", result.getDepartamento().getNombre());
        assertEquals("Ingeniería", result.getDepartamento().getFacultad().getNombre());
    }
}

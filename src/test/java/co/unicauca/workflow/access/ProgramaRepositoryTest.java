package co.unicauca.workflow.access;

import co.unicauca.workflow.domain.entities.Departamento;
import co.unicauca.workflow.domain.entities.Facultad;
import co.unicauca.workflow.domain.entities.Programa;
import co.unicauca.workflow.domain.exceptions.ValidationException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase ProgramaRepository
 * 
 * @author ChatGPT
 */
public class ProgramaRepositoryTest {

    private ProgramaRepository repo;

    @BeforeEach
    public void setUp() throws SQLException {
        repo = new ProgramaRepository();
        Connection conn = repo.getConnection();

        try (Statement stmt = conn.createStatement()) {
            // Limpiar tablas relacionadas para asegurar un entorno limpio
            stmt.execute("DROP TABLE IF EXISTS Programa");
            stmt.execute("DROP TABLE IF EXISTS Departamento");
            stmt.execute("DROP TABLE IF EXISTS Facultad");

            // Crear tablas necesarias
            stmt.execute("CREATE TABLE Facultad (" +
                    "codFacultad INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "facNombre TEXT NOT NULL)");

            stmt.execute("CREATE TABLE Departamento (" +
                    "codDepartamento INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "depNombre TEXT NOT NULL, " +
                    "codFacultad INTEGER NOT NULL, " +
                    "FOREIGN KEY (codFacultad) REFERENCES Facultad(codFacultad))");

            stmt.execute("CREATE TABLE Programa (" +
                    "codPrograma INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "nombrePrograma TEXT NOT NULL, " +
                    "codDepartamento INTEGER NOT NULL, " +
                    "FOREIGN KEY (codDepartamento) REFERENCES Departamento(codDepartamento))");
        }

        // Insertar datos base para Facultad y Departamento
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("INSERT INTO Facultad (facNombre) VALUES ('Ingeniería')");
            stmt.execute("INSERT INTO Departamento (depNombre, codFacultad) VALUES ('Sistemas', 1)");
        }
    }

    @AfterEach
    public void tearDown() {
        repo.disconnect();
    }

    @Test
    public void testSaveProgramaExitoso() throws ValidationException {
        Facultad fac = new Facultad(1, "Ingeniería");
        Departamento dep = new Departamento("Sistemas", fac);
        dep.setCodDepartamento(1);

        Programa prog = new Programa();
        prog.setNombrePrograma("Ingeniería de Software");
        prog.setDepartamento(dep);

        boolean result = repo.save(prog);

        assertTrue(result, "El programa debería guardarse correctamente");
        assertTrue(prog.getCodPrograma() > 0, "El programa debería tener un ID asignado");
    }

    @Test
    public void testSaveProgramaInvalido() {
        Programa prog = new Programa(); // sin nombre ni departamento

        boolean result = repo.save(prog);

        assertFalse(result, "El programa inválido no debería guardarse");
    }

    @Test
    public void testListProgramas() throws ValidationException {
        Facultad fac = new Facultad(1, "Ingeniería");
        Departamento dep = new Departamento("Sistemas", fac);
        dep.setCodDepartamento(1);

        Programa prog = new Programa();
        prog.setNombrePrograma("Ingeniería Industrial");
        prog.setDepartamento(dep);

        repo.save(prog);

        List<Programa> programas = repo.list();

        assertNotNull(programas, "La lista de programas no debe ser nula");
        assertFalse(programas.isEmpty(), "La lista de programas no debe estar vacía");
        assertEquals("Ingeniería Industrial", programas.get(0).getNombrePrograma());
    }

    @Test
    public void testConexionYDesconexion() {
        assertNotNull(repo.getConnection(), "La conexión debe estar activa");

        repo.disconnect();
        assertNull(repo.getConnection(), "La conexión debe cerrarse después de disconnect()");
    }
}

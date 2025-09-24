package co.unicauca.workflow.access;

import co.unicauca.workflow.domain.entities.Departamento;
import co.unicauca.workflow.domain.entities.Facultad;
import co.unicauca.workflow.domain.entities.Programa;
import co.unicauca.workflow.domain.exceptions.ValidationException;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para la clase ProgramaRepository
 *
 * 
 */
public class ProgramaRepositoryTest {

    private Connection conn;
    private ProgramaRepository repo;

    @BeforeEach
    void setUp() throws Exception {
        // 🔹 BD temporal en memoria
        conn = DriverManager.getConnection("jdbc:sqlite::memory:");

        // 🔹 Instanciar repo y forzar conexión en memoria
        repo = new ProgramaRepository();
        repo.disconnect();
        var field = ProgramaRepository.class.getDeclaredField("conn");
        field.setAccessible(true);
        field.set(repo, conn);

        // 🔹 Crear tablas necesarias
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE Facultad (" +
                    "codFacultad INTEGER PRIMARY KEY, " +
                    "nombre TEXT NOT NULL)");

            stmt.execute("CREATE TABLE Departamento (" +
                    "codDepartamento INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nombre TEXT NOT NULL, " +
                    "codFacultad INTEGER NOT NULL, " +
                    "FOREIGN KEY (codFacultad) REFERENCES Facultad(codFacultad))");

            stmt.execute("CREATE TABLE Programa (" +
                    "codPrograma INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nombre TEXT NOT NULL, " +
                    "codDepartamento INTEGER NOT NULL, " +
                    "FOREIGN KEY (codDepartamento) REFERENCES Departamento(codDepartamento))");

            // 🔹 Insertar datos iniciales
            stmt.execute("INSERT INTO Facultad VALUES (1, 'Ingeniería')");
            stmt.execute("INSERT INTO Departamento (codDepartamento, nombre, codFacultad) VALUES (1, 'Sistemas', 1)");
        }
    }

    @AfterEach
    void tearDown() throws Exception {
        conn.close(); // 🔹 al cerrar, la BD temporal desaparece
    }

    @Test
    void testSaveValidPrograma() throws ValidationException {
        Facultad fac = new Facultad("Ingeniería");
        fac.setCodFacultad(1);

        Departamento dep = new Departamento("Sistemas", fac);
        dep.setCodDepartamento(1);

        Programa prog = new Programa("Ingeniería de Software", dep);
        boolean result = repo.save(prog);

        assertTrue(result, "El programa válido debería guardarse");
        assertTrue(prog.getCodPrograma() > 0, "El id debería haberse asignado");
    }

   

    @Test
    void testSaveProgramaWithoutDepartamento() {
        // 🔹 Aquí también lanza ValidationException
        assertThrows(ValidationException.class, () -> {
            new Programa("Ingeniería Civil", null);
        });
    }
    
    
    @Test
void testSaveProgramaWithoutName() throws Exception {
    Facultad fac = new Facultad("Ingeniería");
    fac.setCodFacultad(1);

    Departamento dep = new Departamento("Sistemas", fac);
    dep.setCodDepartamento(1);

    // 🔹 El constructor de Programa debe lanzar ValidationException
    assertThrows(ValidationException.class, () -> new Programa("", dep));
}

    
    

    @Test
    void testListProgramas() throws ValidationException {
        Facultad fac = new Facultad("Ingeniería");
        fac.setCodFacultad(1);

        Departamento dep = new Departamento("Sistemas", fac);
        dep.setCodDepartamento(1);

        repo.save(new Programa("Ingeniería de Software", dep));
        repo.save(new Programa("Ingeniería Electrónica", dep));

        List<Programa> programas = repo.list();
        assertNotNull(programas, "La lista no debería ser nula");
        assertEquals(2, programas.size(), "Debe haber exactamente dos programas");
    }

    @Test
    void testConnectAndDisconnect() {
        repo.connect();
        assertNotNull(repo.getConnection(), "La conexión no debería ser nula");

        repo.disconnect();
        assertNull(repo.getConnection(), "La conexión debería cerrarse después de disconnect()");
    }
}



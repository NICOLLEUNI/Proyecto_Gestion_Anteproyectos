package co.unicauca.workflow.access;

import co.unicauca.workflow.domain.entities.Departamento;
import co.unicauca.workflow.domain.entities.Facultad;
import co.unicauca.workflow.domain.exceptions.ValidationException;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DepartamentoTest {

    private Connection conn;
    private DepartamentoRepository repo;

    @BeforeEach
    void setUp() throws Exception {
        // 🔹  temporal en memoria
        conn = DriverManager.getConnection("jdbc:sqlite::memory:");

        // 🔹 Instanciar repo y forzar conexión en memoria
        repo = new DepartamentoRepository();
        repo.disconnect();
        var field = DepartamentoRepository.class.getDeclaredField("conn");
        field.setAccessible(true);
        field.set(repo, conn);

        try (Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE Facultad (" +
                    "codFacultad INTEGER PRIMARY KEY, " +
                    "facNombre TEXT NOT NULL)");

            stmt.execute("CREATE TABLE Departamento (" +
                    "codDepartamento INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "depNombre TEXT NOT NULL, " +
                    "codFacultad INTEGER NOT NULL, " +
                    "FOREIGN KEY (codFacultad) REFERENCES Facultad(codFacultad))");

            stmt.execute("INSERT INTO Facultad VALUES (1, 'Ingeniería')");
        }
    }

    @AfterEach
    void tearDown() throws Exception {
        conn.close(); // 🔹 al cerrar, la BD temporal desaparece
    }

    @Test
    void testSaveValidDepartamento() throws ValidationException {
        Facultad fac = new Facultad("Ingeniería");
        fac.setCodFacultad(1);

        Departamento depto = new Departamento("Sistemas", fac);
        boolean result = repo.save(depto);

        assertTrue(result, "El departamento válido debería guardarse");
        assertTrue(depto.getCodDepartamento() > 0, "El id debería haberse asignado");
    }

    @Test
    void testSaveWithOutName() throws ValidationException {
        Facultad fac = new Facultad("Ingeniería");
    fac.setCodFacultad(1);

    assertThrows(ValidationException.class, () -> {
        new Departamento("", fac); // aquí lanza
    });
    }

    @Test
    void testSaveWithOutFacultad() throws ValidationException {
        assertThrows(ValidationException.class, () -> {
        new Departamento("Sistemas", null); 
    });
    }

    @Test
    void testList() throws ValidationException {
        Facultad fac = new Facultad("Ingeniería");
        fac.setCodFacultad(1);
        repo.save(new Departamento("Sistemas", fac));

        List<Departamento> departamentos = repo.list();
        assertNotNull(departamentos);
        assertEquals(1, departamentos.size(), "Debe existir exactamente un departamento");
    }

    @Test
    void testConnectAndDisconnect() {
        repo.connect();
        assertNotNull(repo.getConnection(), "La conexión no debería ser nula");

        repo.disconnect();
        assertNull(repo.getConnection(), "La conexión debería cerrarse después de disconnect()");
    }
}

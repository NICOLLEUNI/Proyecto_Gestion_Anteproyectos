package co.unicauca.workflow.access;

import co.unicauca.workflow.domain.entities.Facultad;
import co.unicauca.workflow.domain.exceptions.ValidationException;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FacultadRepositoryTest {

    private Connection conn;
    private FacultadRepository repo;

    @BeforeEach
    void setUp() throws Exception {
        // 🔹 BD temporal en memoria
        conn = DriverManager.getConnection("jdbc:sqlite::memory:");

        // 🔹 Instanciar repo y forzar conexión en memoria
        repo = new FacultadRepository();
        repo.disconnect();
        var field = FacultadRepository.class.getDeclaredField("conn");
        field.setAccessible(true);
        field.set(repo, conn);

        try (Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE Facultad (" +
                    "codFacultad INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "nombre TEXT NOT NULL)");
        }
    }

    @AfterEach
    void tearDown() throws Exception {
        conn.close(); // 🔹 al cerrar, la BD temporal desaparece
    }

    @Test
    void testSaveValidFacultad() throws ValidationException {
        Facultad fac = new Facultad("Ingeniería");
        boolean result = repo.save(fac);

        assertTrue(result, "La facultad válida debería guardarse");
        assertTrue(fac.getCodFacultad() > 0, "El id debería haberse asignado");
    }

    @Test
    void testSaveWithNullName() {
        assertThrows(ValidationException.class, () -> {
            new Facultad(null); // debería lanzar excepción
        });
    }

    @Test
    void testSaveWithEmptyName() {
        assertThrows(ValidationException.class, () -> {
            new Facultad("   "); // debería lanzar excepción
        });
    }

    @Test
    void testList() throws ValidationException {
        Facultad fac = new Facultad("Ingeniería");
        repo.save(fac);

        List<Facultad> facultades = repo.list();
        assertNotNull(facultades);
        assertEquals(1, facultades.size(), "Debe existir exactamente una facultad");
        assertEquals("Ingeniería", facultades.get(0).getNombre());
    }

    @Test
    void testConnectAndDisconnect() {
        repo.connect();
        assertNotNull(repo.getConnection(), "La conexión no debería ser nula");

        repo.disconnect();
        assertNull(repo.getConnection(), "La conexión debería cerrarse después de disconnect()");
    }
}

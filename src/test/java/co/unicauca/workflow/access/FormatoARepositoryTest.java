package co.unicauca.workflow.access;

import co.unicauca.workflow.domain.entities.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class FormatoARepositoryTest {

    private FormatoARepository repo;

    @BeforeEach
    void setUp() {
        cleanDatabase();
        repo = new FormatoARepository();
    }

    @AfterEach
    void tearDown() {
        if (repo != null) repo.disconnect();
    }

    private void cleanDatabase() {
        String url = "jdbc:sqlite:" + System.getProperty("user.dir") + "/BD.db";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM FormatoA_Estudiante");
            stmt.execute("DELETE FROM FormatoA");
        } catch (SQLException e) {
            System.out.println("⚠️ Error limpiando tablas: " + e.getMessage());
        }
    }

    private FormatoA createValidFormatoA() {
        FormatoA f = new FormatoA();
        f.setTitle("Proyecto de prueba");
        f.setMode(enumModalidad.INVESTIGACION);

        Docente manager = new Docente();
        manager.setIdUsuario(1);
        f.setProjectManager(manager);

        f.setArchivoPDF("archivo.pdf");
        f.setDate(LocalDate.now());
        f.setGeneralObjetive("Objetivo general de prueba");
        f.setSpecificObjetives("Objetivos específicos de prueba");
        f.setState(enumEstado.ENTREGADO);
        f.setCounter(0);

        Estudiante e1 = new Estudiante();
        e1.setIdUsuario(10);
        List<Estudiante> estudiantes = new ArrayList<>();
        estudiantes.add(e1);
        f.setEstudiantes(estudiantes);

        return f;
    }

    @Test
    void testSaveValidFormatoA() {
        FormatoA f = createValidFormatoA();
        boolean result = repo.save(f);
        assertTrue(result, "El FormatoA debería guardarse correctamente en la BD");

        List<FormatoA> formatos = repo.list();
        assertFalse(formatos.isEmpty(), "Debería haber al menos un FormatoA en la BD");
        assertEquals("Proyecto de prueba", formatos.get(0).getTitle());
    }

    @Test
    void testSaveInvalidFormatoA() {
        FormatoA f = new FormatoA();
        boolean result = repo.save(f);
        assertFalse(result, "No debería permitir guardar un FormatoA inválido");
    }

    @Test
    void testList() {
        List<FormatoA> formatosVacios = repo.list();
        assertTrue(formatosVacios.isEmpty(), "La lista debería estar vacía inicialmente");

        FormatoA f = createValidFormatoA();
        repo.save(f);

        List<FormatoA> formatos = repo.list();
        assertEquals(1, formatos.size(), "Debería haber exactamente 1 FormatoA en la BD");
    }

    @Test
    void testFindById() {
        FormatoA f = createValidFormatoA();
        f.setTitle("Formato para buscar");
        repo.save(f);

        int savedId = repo.list().get(0).getId();
        FormatoA found = repo.findById(savedId);

        assertNotNull(found, "Debería encontrar el FormatoA recién insertado");
        assertEquals("Formato para buscar", found.getTitle());
        assertEquals(savedId, found.getId());
    }

    @Test
    void testFindByIdNotFound() {
        FormatoA found = repo.findById(999);
        assertNull(found, "Debería retornar null para un ID inexistente");
    }

    @Test
    void testConnection() {
        Connection conn = repo.getConnection();
        assertNotNull(conn, "El repositorio debería tener una conexión activa");

        try {
            assertFalse(conn.isClosed(), "La conexión debería estar abierta");
        } catch (SQLException e) {
            fail("Error verificando estado de la conexión: " + e.getMessage());
        }
    }
}

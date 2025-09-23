package co.unicauca.workflow.access;

import co.unicauca.workflow.domain.entities.Facultad;
import co.unicauca.workflow.domain.exceptions.ValidationException;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests para la clase FacultadRepository.
 * 
 * @author User
 */
public class FacultadRepositoryTest {

    private FacultadRepository repo;

    @BeforeEach
    public void setUp() {
        repo = new FacultadRepository();
        repo.connect();
    }

    @AfterEach
    public void tearDown() {
        repo.disconnect();
    }

    @Test
    public void testSave() throws ValidationException {
        Facultad fac = new Facultad(1, "Ingeniería"); // ✅ incluye id y nombre
        boolean result = repo.save(fac);

        assertTrue(result, "La facultad debería guardarse correctamente");
        assertTrue(fac.getCodFacultad() > 0, "La facultad debería tener un código asignado");
    }

    @Test
    public void testListFacultades() throws ValidationException {
        Facultad fac = new Facultad(2, "Ciencias Económicas"); // ✅ incluye id y nombre
        repo.save(fac);

        List<Facultad> facultades = repo.list();

        assertNotNull(facultades, "La lista de facultades no debe ser nula");
        assertFalse(facultades.isEmpty(), "La lista de facultades no debe estar vacía");
        assertEquals("Ciencias Económicas", facultades.get(0).getNombre());
    }

    @Test
    public void testFindById() throws ValidationException {
        Facultad fac = new Facultad(3, "Salud"); // ✅ incluye id y nombre
        repo.save(fac);

        Facultad encontrada = repo.findById(fac.getCodFacultad());

        assertNotNull(encontrada, "La facultad debería encontrarse");
        assertEquals("Salud", encontrada.getNombre());
    }

    @Test
    public void testUpdate() throws ValidationException {
        Facultad fac = new Facultad(4, "Artes"); // ✅ incluye id y nombre
        repo.save(fac);

        fac.setNombre("Artes y Diseño");
        boolean result = repo.update(fac);

        assertTrue(result, "La facultad debería actualizarse correctamente");

        Facultad actualizada = repo.findById(fac.getCodFacultad());
        assertEquals("Artes y Diseño", actualizada.getNombre());
    }

    @Test
    public void testDelete() throws ValidationException {
        Facultad fac = new Facultad(5, "Derecho"); // ✅ incluye id y nombre
        repo.save(fac);

        boolean result = repo.delete(fac.getCodFacultad());

        assertTrue(result, "La facultad debería eliminarse correctamente");

        Facultad eliminada = repo.findById(fac.getCodFacultad());
        assertNull(eliminada, "La facultad ya no debería existir");
    }
}


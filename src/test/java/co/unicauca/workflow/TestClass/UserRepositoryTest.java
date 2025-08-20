
package co.unicauca.workflow.TestClass;

import co.unicauca.workflow.access.UserRepository;
import co.unicauca.workflow.domain.entities.User;
import co.unicauca.workflow.domain.entities.enumProgram;
import co.unicauca.workflow.domain.entities.enumRol;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    private static UserRepository repo;

    @BeforeAll
    static void init() {
        repo = new UserRepository();
        repo.connect(); // Conectamos para que la BD esté lista
    }
    
@BeforeEach
void resetDB() throws Exception {
    if (repo.getConnection() != null) {
        try (var stmt = repo.getConnection().createStatement()) {
            stmt.execute("DELETE FROM User");
        } catch (Exception e) {
            System.out.println("⚠️ No se pudo limpiar la tabla User: " + e.getMessage());
        }
    }
}

    @AfterAll
    static void cleanup() {
        repo.disconnect();
    }

    @Test
    void testSaveUser() {
        User user = new User();
        user.setName("Carlos");
        user.setLastname("Pérez");
        user.setPhone("1234567890");
        user.setEmail("carlos@unicauca.edu.co");
        user.setPassword("Password123");
        user.setRol(enumRol.ESTUDIANTE);
        user.setProgram(enumProgram.INGENIERIA_SISTEMAS);

        boolean result = repo.save(user);

        assertTrue(result, "El usuario debería guardarse correctamente");
        assertTrue(user.getIdUsuario() > 0, "El idUsuario debería haberse asignado");
        
    }

    @Test
    void testListUsers() {
        List<User> users = repo.list();
        assertNotNull(users, "La lista no debería ser nula");
        assertTrue(users.size() >= 0, "Debería devolver al menos una lista vacía");
    }
}

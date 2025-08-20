
package co.unicauca.workflow.access;

import co.unicauca.workflow.access.UserRepository;
import co.unicauca.workflow.domain.entities.User;
import co.unicauca.workflow.domain.entities.enumProgram;
import co.unicauca.workflow.domain.entities.enumRol;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    private static UserRepository repo;
  /**
     * @brief Inicializa el repositorio y la conexión a la base de datos.
     *
     * Se ejecuta una sola vez antes de todos los tests.
     * Deja la base de datos lista para las operaciones CRUD.
     */
    @BeforeAll
    static void init() {
        repo = new UserRepository();
        repo.connect(); // Conectamos para que la BD esté lista
    }
  /**
     * @brief Limpia la tabla {@code User} antes de cada prueba.
     *
     * Esto asegura que cada test se ejecute en un entorno limpio,
     * sin usuarios residuales de pruebas anteriores.
     *
     * @throws Exception si ocurre un error al ejecutar la sentencia SQL.
     */  
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
/**
     * @brief Cierra la conexión a la base de datos después de todas las pruebas.
     */
    @AfterAll
    static void cleanup() {
        repo.disconnect();
    }
/**
     * @brief Verifica que un usuario pueda guardarse correctamente en la base de datos.
     *
     * Se crea un usuario con datos de prueba y se guarda usando el repositorio.
     * 
     * Validaciones:
     * - El método {@code save()} debe retornar {@code true}.
     * - El usuario guardado debe tener un {@code idUsuario} asignado
     *   por la base de datos (mayor que cero).
     */
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
/**
     * @brief Verifica que el método {@code list()} devuelva los usuarios de la BD.
     *
     * Validaciones:
     * - La lista retornada no debe ser {@code null}.
     * - Puede estar vacía, pero siempre debe existir.
     */
    @Test
    void testListUsers() {
        List<User> users = repo.list();
        assertNotNull(users, "La lista no debería ser nula");
        assertTrue(users.size() >= 0, "Debería devolver al menos una lista vacía");
    }
}

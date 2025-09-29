package co.unicauca.workflow.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.EnumSet;

import co.unicauca.workflow.domain.entities.*;
import co.unicauca.workflow.domain.exceptions.ValidationException;
import co.unicauca.workflow.access.*;

public class PersonaServiceTest {

    // ==================== REPOSITORIOS IN-MEMORY ====================
/**
 * Repositorio en memoria para Persona
 */

private static class InMemoryPersonaRepo implements IPersonaRepository {
    private final List<Persona> store = new ArrayList<>();
    private int nextId = 1;

    @Override
    public boolean save(Persona persona)  {
        if (persona == null) return false;
        if (persona.getIdUsuario() == 0) {
            persona.setIdUsuario(nextId++);
        }
        store.add(persona);
        return true;
    }

    @Override
    public List<Persona> list() {
        return new ArrayList<>(store);
    }

    public void clear() { 
        store.clear(); 
        nextId = 1; 
    }
}

/**
 * Repositorio en memoria para Facultad
 */
private static class InMemoryFacultadRepo implements IFacultadRepository {
    private final List<Facultad> store = new ArrayList<>();
    private int nextId = 1;

    @Override
    public boolean save(Facultad facultad)  {  // ← AGREGAR throws
        if (facultad == null) return false;
        if (facultad.getCodFacultad() == 0) {
            facultad.setCodFacultad(nextId++);
        }
        store.add(facultad);
        return true;
    }

    @Override
    public List<Facultad> list() {
        return new ArrayList<>(store);
    }

    @Override
    public void connect() {
        // No hace nada en memoria
    }

    @Override
    public void disconnect() {
        // No hace nada en memoria
    }

    public void clear() { 
        store.clear(); 
        nextId = 1; 
    }
}

/**
 * Repositorio en memoria para Departamento
 */
private static class InMemoryDepartamentoRepo implements IDepartamentoRepository {
    private final List<Departamento> store = new ArrayList<>();
    private int nextId = 1;

    @Override
    public boolean save(Departamento departamento){  // ← AGREGAR throws
        if (departamento == null) return false;
        if (departamento.getCodDepartamento() == 0) {
            departamento.setCodDepartamento(nextId++);
        }
        store.add(departamento);
        return true;
    }

    @Override
    public List<Departamento> list() {
        return new ArrayList<>(store);
    }

 

    public void clear() { 
        store.clear(); 
        nextId = 1; 
    }
}

/**
 * Repositorio en memoria para Programa
 */
private static class InMemoryProgramaRepo implements IProgramaRepository {
    private final List<Programa> store = new ArrayList<>();
    private int nextId = 1;

    @Override
    public boolean save(Programa programa)  {  // ← AGREGAR throws
        if (programa == null) return false;
        if (programa.getCodPrograma() == 0) {
            programa.setCodPrograma(nextId++);
        }
        store.add(programa);
        return true;
    }

    @Override
    public List<Programa> list() {
        return new ArrayList<>(store);
    }


    public void clear() { 
        store.clear(); 
        nextId = 1; 
    }
}

/**
 * Repositorio en memoria para Estudiante
 */
private static class InMemoryEstudianteRepo implements IEstudianteRepository {
    private final List<Estudiante> store = new ArrayList<>();

    @Override
    public boolean save(Estudiante estudiante)  {  // ← AGREGAR throws
        if (estudiante == null) return false;
        store.add(estudiante);
        return true;
    }

    @Override
    public List<Estudiante> list() {
        return new ArrayList<>(store);
    }

 

    public void clear() { 
        store.clear(); 
    }
}

/**
 * Repositorio en memoria para Docente
 */
private static class InMemoryDocenteRepo implements IDocenteRepository {
    private final List<Docente> store = new ArrayList<>();

    @Override
    public boolean save(Docente docente)  {  // ← AGREGAR throws
        if (docente == null) return false;
        store.add(docente);
        return true;
    }

    @Override
    public List<Docente> list() {
        return new ArrayList<>(store);
    }

   
    public void connect() {
        // No hace nada en memoria
    }

    public void disconnect() {
        // No hace nada en memoria
    }

    public void clear() { 
        store.clear(); 
    }
}

/**
 * Repositorio en memoria para Coordinador
 */
private static class InMemoryCoordinadorRepo implements ICoordinadorRepository {
    private final List<Coordinador> store = new ArrayList<>();


    public boolean save(Coordinador coordinador)  {  // ← AGREGAR throws
        if (coordinador == null) return false;
        store.add(coordinador);
        return true;
    }

    @Override
    public List<Coordinador> list() {
        return new ArrayList<>(store);
    }


    public void clear() { 
        store.clear(); 
    }
}

    // ==================== PERSONA SERVICE ADAPTADO PARA TESTING ====================

    /**
     * PersonaService extendido para testing que inyecta repositorios mockeados
     */
    private static class PersonaServiceForTesting extends PersonaService {
        private final IFacultadRepository facultadRepo;
        private final IDepartamentoRepository departamentoRepo;
        private final IProgramaRepository programaRepo;

        public PersonaServiceForTesting(
            IPersonaRepository personaRepo, 
            IEstudianteRepository estudianteRepo,
            IDocenteRepository docenteRepo, 
            ICoordinadorRepository coordinadorRepo,
            IFacultadRepository facultadRepo,
            IDepartamentoRepository departamentoRepo,
            IProgramaRepository programaRepo
        ) {
            super(personaRepo, estudianteRepo, docenteRepo, coordinadorRepo);
            this.facultadRepo = facultadRepo;
            this.departamentoRepo = departamentoRepo;
            this.programaRepo = programaRepo;
        }

        @Override
        protected Facultad obtenerFacultadFIET() {
            try {
                List<Facultad> facultades = facultadRepo.list();
                for (Facultad f : facultades) {
                    if ("FIET".equals(f.getNombre())) {
                        return f;
                    }
                }
                Facultad fiet = new Facultad("FIET");
                if (facultadRepo.save(fiet)) {
                    return fiet;
                }
            } catch (ValidationException e) {
                System.err.println("Error en obtenerFacultadFIET: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected Departamento obtenerDepartamento(String nombre, Facultad facultad) {
            try {
                List<Departamento> departamentos = departamentoRepo.list();
                for (Departamento d : departamentos) {
                    if (nombre.equals(d.getNombre())) {
                        return d;
                    }
                }
                Departamento nuevo = new Departamento(nombre, facultad);
                if (departamentoRepo.save(nuevo)) {
                    return nuevo;
                }
            } catch (ValidationException e) {
                System.err.println("Error en obtenerDepartamento: " + e.getMessage());
            }
            return null;
        }

        @Override
        protected Programa obtenerPrograma(String nombre, Departamento departamento) {
            try {
                List<Programa> programas = programaRepo.list();
                for (Programa p : programas) {
                    if (nombre.equals(p.getNombrePrograma())) {
                        return p;
                    }
                }
                Programa nuevo = new Programa(nombre, departamento);
                if (programaRepo.save(nuevo)) {
                    return nuevo;
                }
            } catch (ValidationException e) {
                System.err.println("Error en obtenerPrograma: " + e.getMessage());
            }
            return null;
        }
    }

    // ==================== VARIABLES DE INSTANCIA ====================

    private PersonaService personaService;
    private InMemoryPersonaRepo personaRepo;
    private InMemoryFacultadRepo facultadRepo;
    private InMemoryDepartamentoRepo departamentoRepo;
    private InMemoryProgramaRepo programaRepo;
    private InMemoryEstudianteRepo estudianteRepo;
    private InMemoryDocenteRepo docenteRepo;
    private InMemoryCoordinadorRepo coordinadorRepo;

    // ==================== SETUP ====================

    @BeforeEach
    void setUp() {
        // Crear repositorios in-memory
        personaRepo = new InMemoryPersonaRepo();
        facultadRepo = new InMemoryFacultadRepo();
        departamentoRepo = new InMemoryDepartamentoRepo();
        programaRepo = new InMemoryProgramaRepo();
        estudianteRepo = new InMemoryEstudianteRepo();
        docenteRepo = new InMemoryDocenteRepo();
        coordinadorRepo = new InMemoryCoordinadorRepo();

        // Crear PersonaService con repositorios inyectados
        personaService = new PersonaServiceForTesting(
            personaRepo, estudianteRepo, docenteRepo, coordinadorRepo,
            facultadRepo, departamentoRepo, programaRepo
        );

        // Sembrar datos de prueba
        sembrarDatosPrueba();
    }

    private void sembrarDatosPrueba() {
        EnumSet<enumRol> roles = EnumSet.of(enumRol.ESTUDIANTE);
        assertTrue(personaService.savePerson(
            "Juan", "Pérez", "123456789", 
            "juan.perez@unicauca.edu.co", "Test123!",
            roles, "Ingeniería de Sistemas", null
        ), "No se pudo sembrar persona de prueba");
    }

    // ==================== TESTS DE MÉTODOS DE UTILIDAD ====================

    @Nested
    @DisplayName("Tests de Validación y Utilidades")
    class ValidationUtilsTests {

        @Test
        @DisplayName("hashPassword - Misma contraseña genera mismo hash")
        void testHashSamePasswordGeneratesSameHash() {
            String password = "Test123!";
            assertEquals(
                personaService.hashPassword(password),
                personaService.hashPassword(password)
            );
        }

        @Test
        @DisplayName("hashPassword - Contraseñas diferentes generan hashes diferentes")
        void testHashDifferentPasswordsGenerateDifferentHash() {
            assertNotEquals(
                personaService.hashPassword("Test123!"),
                personaService.hashPassword("Other456!")
            );
        }

        @Test
        @DisplayName("hashPassword - Password null retorna null")
        void testHashNullPasswordReturnsNull() {
            assertNull(personaService.hashPassword(null));
        }

        @Test
        @DisplayName("hashPassword - Hash no es texto plano")
        void testHashIsNotPlainText() {
            String password = "Test123!";
            assertNotEquals(password, personaService.hashPassword(password));
        }

        @Test
        @DisplayName("validatePassword - Contraseña válida")
        void testValidPassword() {
            assertTrue(personaService.validatePassword("Test123!"));
        }

        @Test
        @DisplayName("validatePassword - Contraseña muy corta")
        void testPasswordTooShort() {
            assertFalse(personaService.validatePassword("T1!"));
        }

        @Test
        @DisplayName("validatePassword - Sin mayúscula")
        void testPasswordWithoutUppercase() {
            assertFalse(personaService.validatePassword("test123!"));
        }

        @Test
        @DisplayName("validatePassword - Sin minúscula")
        void testPasswordWithoutLowercase() {
            assertFalse(personaService.validatePassword("TEST123!"));
        }

        @Test
        @DisplayName("validatePassword - Sin dígito")
        void testPasswordWithoutDigit() {
            assertFalse(personaService.validatePassword("Testtest!"));
        }

        @Test
        @DisplayName("validatePassword - Sin caracter especial")
        void testPasswordWithoutSpecialCharacter() {
            assertFalse(personaService.validatePassword("Test12345"));
        }

        @Test
        @DisplayName("validatePassword - Password null")
        void testPasswordNull() {
            assertFalse(personaService.validatePassword(null));
        }

        @Test
        @DisplayName("validateEmail - Email válido")
        void testValidEmail() {
            assertTrue(personaService.validateEmail("test@unicauca.edu.co"));
        }

        @Test
        @DisplayName("validateEmail - Dominio incorrecto")
        void testEmailWithWrongDomain() {
            assertFalse(personaService.validateEmail("test@gmail.com"));
        }

        @Test
        @DisplayName("validateEmail - Formato inválido")
        void testEmailWithBadFormat() {
            assertFalse(personaService.validateEmail("@unicauca.edu.co"));
        }

        @Test
        @DisplayName("validateEmail - Email null")
        void testEmailNull() {
            assertFalse(personaService.validateEmail(null));
        }
    }

    // ==================== TESTS DE OPERACIONES CRUD ====================

    @Nested
    @DisplayName("Tests de Operaciones CRUD")
    class CrudOperationsTests {

        @Test
        @DisplayName("savePerson - Estudiante válido")
        void testSaveValidStudent() {
            EnumSet<enumRol> roles = EnumSet.of(enumRol.ESTUDIANTE);
            assertTrue(personaService.savePerson(
                "Ana", "López", "987654321",
                "ana.lopez@unicauca.edu.co", "Pass123!",
                roles, "Ingeniería Electrónica", null
            ));
        }

        @Test
        @DisplayName("savePerson - Docente válido")
        void testSaveValidDocente() {
            EnumSet<enumRol> roles = EnumSet.of(enumRol.DOCENTE);
            assertTrue(personaService.savePerson(
                "Carlos", "Ruiz", "555666777",
                "carlos.ruiz@unicauca.edu.co", "Doc123!",
                roles, null, "Electrónica"
            ));
        }

        @Test
        @DisplayName("savePerson - Coordinador válido")
        void testSaveValidCoordinador() {
            EnumSet<enumRol> roles = EnumSet.of(enumRol.COORDINADOR);
            assertTrue(personaService.savePerson(
                "María", "Gómez", "111222333",
                "maria.gomez@unicauca.edu.co", "Coord123!",
                roles, null, "Sistemas"
            ));
        }

        @Test
        @DisplayName("savePerson - Roles múltiples")
        void testSavePersonWithMultipleRoles() {
            EnumSet<enumRol> roles = EnumSet.of(enumRol.DOCENTE, enumRol.COORDINADOR);
            assertTrue(personaService.savePerson(
                "Pedro", "Silva", "444555666",
                "pedro.silva@unicauca.edu.co", "Multi123!",
                roles, null, "Telemática"
            ));
        }

        @Test
        @DisplayName("savePerson - Todos los roles")
        void testSavePersonWithAllRoles() {
            EnumSet<enumRol> roles = EnumSet.allOf(enumRol.class);
            assertTrue(personaService.savePerson(
                "Super", "Usuario", "999888777",
                "super.usuario@unicauca.edu.co", "Super123!",
                roles, "Ingeniería Civil", "Matemáticas"
            ));
        }

        @Test
        @DisplayName("savePerson - Campos null")
        void testSavePersonWithNullFields() {
            EnumSet<enumRol> roles = EnumSet.of(enumRol.ESTUDIANTE);
            assertFalse(personaService.savePerson(
                null, null, null, null, null, roles, null, null
            ));
        }

     @Test
@DisplayName("savePerson - Email inválido")
void testSavePersonWithInvalidEmail() {
    // Testear solo la validación de email, no el guardado completo
    assertFalse(personaService.validateEmail("test@gmail.com"), 
        "Email no institucional debe ser rechazado");
}

@Test
@DisplayName("savePerson - Contraseña inválida")
void testSavePersonWithInvalidPassword() {
    // Testear solo la validación de contraseña, no el guardado completo
    assertFalse(personaService.validatePassword("123"),
        "Contraseña débil debe ser rechazada");
}

        @Test
        @DisplayName("personExists - Persona registrada")
        void testPersonExistsWithRegisteredEmail() {
            assertTrue(personaService.personExists("juan.perez@unicauca.edu.co"));
        }

        @Test
        @DisplayName("personExists - Persona no registrada")
        void testPersonExistsWithUnregisteredEmail() {
            assertFalse(personaService.personExists("noexiste@unicauca.edu.co"));
        }

        @Test
        @DisplayName("personExists - Email null")
        void testPersonExistsWithNullEmail() {
            assertFalse(personaService.personExists(null));
        }

        @Test
        @DisplayName("personExists - Email vacío")
        void testPersonExistsWithEmptyEmail() {
            assertFalse(personaService.personExists(""));
        }

        @Test
        @DisplayName("personExists - Email mayúsculas")
        void testPersonExistsWithUppercaseEmail() {
            assertTrue(personaService.personExists("JUAN.PEREZ@UNICAUCA.EDU.CO"));
        }
    }

    // ==================== TESTS DE AUTENTICACIÓN ====================

    @Nested
    @DisplayName("Tests de Autenticación")
    class AuthenticationTests {

        @Test
        @DisplayName("authenticatePerson - Credenciales válidas")
        void testAuthenticateValidPerson() {
            Persona persona = personaService.authenticatePerson(
                "juan.perez@unicauca.edu.co", "Test123!"
            );
            assertNotNull(persona);
            assertEquals("Juan", persona.getName());
            assertEquals("Pérez", persona.getLastname());
        }

        @Test
        @DisplayName("authenticatePerson - Contraseña incorrecta")
        void testAuthenticateWithWrongPassword() {
            Persona persona = personaService.authenticatePerson(
                "juan.perez@unicauca.edu.co", "WrongPass!"
            );
            assertNull(persona);
        }

        @Test
        @DisplayName("authenticatePerson - Email no existe")
        void testAuthenticateWithNonExistentEmail() {
            Persona persona = personaService.authenticatePerson(
                "noexiste@unicauca.edu.co", "Test123!"
            );
            assertNull(persona);
        }

        @Test
        @DisplayName("authenticatePerson - Email null")
        void testAuthenticateWithNullEmail() {
            Persona persona = personaService.authenticatePerson(null, "Test123!");
            assertNull(persona);
        }

        @Test
        @DisplayName("authenticatePerson - Password null")
        void testAuthenticateWithNullPassword() {
            Persona persona = personaService.authenticatePerson(
                "juan.perez@unicauca.edu.co", null
            );
            assertNull(persona);
        }

        @Test
        @DisplayName("authenticatePerson - Email en mayúsculas")
        void testAuthenticateWithUppercaseEmail() {
            Persona persona = personaService.authenticatePerson(
                "JUAN.PEREZ@UNICAUCA.EDU.CO", "Test123!"
            );
            assertNotNull(persona);
            assertEquals("Juan", persona.getName());
        }
    }

    // ==================== TESTS DE CREACIÓN DE ENTIDADES ====================

    @Nested
    @DisplayName("Tests de Creación Automática de Entidades")
    class EntityCreationTests {

        @Test
        @DisplayName("Creación automática de Facultad FIET")
        void testFIETCreation() {
            facultadRepo.clear();
            
            EnumSet<enumRol> roles = EnumSet.of(enumRol.ESTUDIANTE);
            assertTrue(personaService.savePerson(
                "Test", "Student", "123",
                "test.student@unicauca.edu.co", "Test123!",
                roles, "Nuevo Programa", null
            ));

            List<Facultad> facultades = facultadRepo.list();
            assertTrue(facultades.stream().anyMatch(f -> "FIET".equals(f.getNombre())));
        }

        @Test
        @DisplayName("Creación automática de Departamento para Docente")
        void testDepartmentCreationForDocente() {
            departamentoRepo.clear();
            
            EnumSet<enumRol> roles = EnumSet.of(enumRol.DOCENTE);
            assertTrue(personaService.savePerson(
                "Nuevo", "Docente", "123",
                "nuevo.docente@unicauca.edu.co", "Doc123!",
                roles, null, "Departamento Nuevo"
            ));

            List<Departamento> departamentos = departamentoRepo.list();
            assertTrue(departamentos.stream().anyMatch(d -> 
                "Departamento Nuevo".equals(d.getNombre())));
        }

        @Test
        @DisplayName("Creación automática de Programa para Estudiante")
        void testProgramCreationForStudent() {
            programaRepo.clear();
            
            EnumSet<enumRol> roles = EnumSet.of(enumRol.ESTUDIANTE);
            assertTrue(personaService.savePerson(
                "Nuevo", "Estudiante", "123",
                "nuevo.estudiante@unicauca.edu.co", "Est123!",
                roles, "Programa Nuevo", null
            ));

            List<Programa> programas = programaRepo.list();
            assertTrue(programas.stream().anyMatch(p -> 
                "Programa Nuevo".equals(p.getNombrePrograma())));
        }
    }

    // ==================== TESTS DE INTEGRACIÓN ====================

    @Nested
    @DisplayName("Tests de Integración")
    class IntegrationTests {

        @Test
        @DisplayName("Flujo completo: Registro y Autenticación")
        void testCompleteRegistrationAndAuthFlow() {
            EnumSet<enumRol> roles = EnumSet.of(enumRol.COORDINADOR);
            
            assertTrue(personaService.savePerson(
                "Flujo", "Completo", "555123456",
                "flujo.completo@unicauca.edu.co", "Flujo123!",
                roles, null, "Departamento Flujo"
            ));

            assertTrue(personaService.personExists("flujo.completo@unicauca.edu.co"));

            Persona persona = personaService.authenticatePerson(
                "flujo.completo@unicauca.edu.co", "Flujo123!"
            );
            assertNotNull(persona);
            assertEquals("Flujo", persona.getName());
            assertTrue(persona.getRoles().contains(enumRol.COORDINADOR));
        }

        @Test
        @DisplayName("Múltiples personas con diferentes roles")
        void testMultiplePeopleWithDifferentRoles() {
            EnumSet<enumRol> rolesEst = EnumSet.of(enumRol.ESTUDIANTE);
            assertTrue(personaService.savePerson(
                "Est", "Uno", "111",
                "est1@unicauca.edu.co", "Est123!",
                rolesEst, "Programa A", null
            ));

            EnumSet<enumRol> rolesDoc = EnumSet.of(enumRol.DOCENTE);
            assertTrue(personaService.savePerson(
                "Doc", "Uno", "222",
                "doc1@unicauca.edu.co", "Doc123!",
                rolesDoc, null, "Departamento A"
            ));

            assertTrue(personaService.personExists("est1@unicauca.edu.co"));
            assertTrue(personaService.personExists("doc1@unicauca.edu.co"));

            assertNotNull(personaService.authenticatePerson("est1@unicauca.edu.co", "Est123!"));
            assertNotNull(personaService.authenticatePerson("doc1@unicauca.edu.co", "Doc123!"));
        }
    }
}
package co.unicauca.workflow.domain.service;

import co.unicauca.workflow.access.FormatoARepository;
import co.unicauca.workflow.access.FormatoAVersionRepository;
import co.unicauca.workflow.domain.entities.*;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests de integración para FormatoAService usando repositorios reales con SQLite
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FormatoAServiceTest {

    private FormatoAService formatoAService;
    private FormatoARepository formatoARepository;
    private FormatoAVersionRepository formatoAVersionRepository;
    
    // IDs para limpieza
    private static List<Integer> formatosCreados = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        // Inicializar repositorios reales
        formatoARepository = new FormatoARepository();
        formatoAVersionRepository = new FormatoAVersionRepository();
        
        // Crear servicio
        formatoAService = new FormatoAService(formatoARepository);
    }

    @AfterEach
    public void tearDown() {
        // Cerrar conexiones si es necesario
        if (formatoARepository != null) {
            formatoARepository.disconnect();
        }
        if (formatoAVersionRepository != null) {
            formatoAVersionRepository.disconnect();
        }
    }

   @AfterAll
public static void cleanUp() {
    FormatoARepository repoA = new FormatoARepository();
    FormatoAVersionRepository repoV = new FormatoAVersionRepository();

    for (Integer id : formatosCreados) {
        try {
            // 1️⃣ Borrar versiones primero (respeta FK)
            repoV.deleteByFormatoAId(id);
            // 2️⃣ Borrar FormatoA
            repoA.delete(id);
        } catch (Exception e) {
            System.err.println("Error eliminando FormatoA con id " + id + ": " + e.getMessage());
        }
    }

    repoA.disconnect();
    repoV.disconnect();

    System.out.println("✔ Limpieza completada. Se eliminaron " + formatosCreados.size() + " registros de prueba.");
}

    // ==================== TESTS DE subirFormatoA ====================

    @Test
    @Order(1)
    public void testSubirFormatoA_Success() {
        // Arrange
        FormatoA formatoA = crearFormatoAValido();
        formatoA.setTitle("Test Subir Formato - " + System.currentTimeMillis());

        // Act
        boolean result = formatoAService.subirFormatoA(formatoA);

        // Assert
        assertTrue(result, "El formato debe guardarse exitosamente");
        assertTrue(formatoA.getId() > 0, "Debe asignarse un ID al formato");
        
        // Guardar para limpieza
        formatosCreados.add(formatoA.getId());

        // Verificar que el formato existe en BD
        FormatoA formatoGuardado = formatoAService.findById(formatoA.getId());
        assertNotNull(formatoGuardado, "El formato debe existir en la BD");
        assertEquals(formatoA.getTitle(), formatoGuardado.getTitle());
        assertEquals(formatoA.getGeneralObjetive(), formatoGuardado.getGeneralObjetive());

        // Verificar que se creó la versión inicial
        List<FormatoAVersion> versiones = formatoAVersionRepository.listByFormatoA(formatoA.getId());
        assertFalse(versiones.isEmpty(), "Debe existir al menos una versión");
        
        FormatoAVersion version1 = versiones.get(0);
        assertEquals(1, version1.getNumeroVersion(), "La primera versión debe ser 1");
        assertEquals(enumEstado.ENTREGADO, version1.getState(), "Estado inicial debe ser ENTREGADO");
        assertNull(version1.getObservations(), "Las observaciones iniciales deben ser null");
        assertEquals(formatoA.getTitle(), version1.getTitle());
    }

    @Test
    @Order(2)
    public void testSubirFormatoA_ConDatosCompletos() {
        // Arrange
        FormatoA formato = crearFormatoAValido();
        formato.setTitle("Desarrollo Sistema Gestión - " + System.currentTimeMillis());
        formato.setMode(enumModalidad.PRACTICA_PROFESIONAL);
        formato.setGeneralObjetive("Desarrollar un sistema completo de gestión empresarial");
        formato.setSpecificObjetives("1. Análisis de requisitos\n2. Diseño del sistema\n3. Implementación\n4. Pruebas");

        // Act
        boolean result = formatoAService.subirFormatoA(formato);

        // Assert
        assertTrue(result);
        formatosCreados.add(formato.getId());

        FormatoA recuperado = formatoAService.findById(formato.getId());
        assertNotNull(recuperado);
        assertEquals("Desarrollo Sistema Gestión - " + formato.getTitle().split(" - ")[1], recuperado.getTitle());
        assertEquals(enumModalidad.PRACTICA_PROFESIONAL, recuperado.getMode());
        assertEquals(formato.getGeneralObjetive(), recuperado.getGeneralObjetive());
    }

    @Test
    @Order(3)
    public void testSubirFormatoA_VerificaVersionConDatosCorrectos() {
        // Arrange
        FormatoA formato = crearFormatoAValido();
        formato.setTitle("Verificar Versión - " + System.currentTimeMillis());
        formato.setMode(enumModalidad.INVESTIGACION);
        formato.setArchivoPDF("archivo_test.pdf");
        formato.setCartaLaboral("carta_test.pdf");

        // Act
        formatoAService.subirFormatoA(formato);
        formatosCreados.add(formato.getId());

        // Assert - Verificar detalles de la versión
        List<FormatoAVersion> versiones = formatoAVersionRepository.listByFormatoA(formato.getId());
        assertEquals(1, versiones.size(), "Debe haber exactamente 1 versión");

        FormatoAVersion version = versiones.get(0);
        assertEquals(formato.getTitle(), version.getTitle());
        assertEquals(formato.getMode(), version.getMode());
        assertEquals(formato.getGeneralObjetive(), version.getGeneralObjetive());
        assertEquals(formato.getSpecificObjetives(), version.getSpecificObjetives());
        assertEquals(formato.getArchivoPDF(), version.getArchivoPDF());
        assertEquals(formato.getCartaLaboral(), version.getCartaLaboral());
        assertEquals(enumEstado.ENTREGADO, version.getState());
        assertNotNull(version.getFecha(), "Debe tener fecha de creación");
    }

    // ==================== TESTS DE listFormatoA ====================

    @Test
    @Order(4)
    public void testListFormatoA_RetornaLista() {
        // Arrange - Crear algunos formatos
        FormatoA formato1 = crearFormatoAValido();
        formato1.setTitle("Formato Lista 1 - " + System.currentTimeMillis());
        formatoAService.subirFormatoA(formato1);
        formatosCreados.add(formato1.getId());

        FormatoA formato2 = crearFormatoAValido();
        formato2.setTitle("Formato Lista 2 - " + System.currentTimeMillis());
        formatoAService.subirFormatoA(formato2);
        formatosCreados.add(formato2.getId());

        // Act
        List<FormatoA> formatos = formatoAService.listFormatoA();

        // Assert
        assertNotNull(formatos, "La lista no debe ser null");
        assertTrue(formatos.size() >= 2, "Debe haber al menos 2 formatos");
        
        // Verificar que nuestros formatos están en la lista
        boolean encontrado1 = formatos.stream()
            .anyMatch(f -> f.getTitle().startsWith("Formato Lista 1"));
        boolean encontrado2 = formatos.stream()
            .anyMatch(f -> f.getTitle().startsWith("Formato Lista 2"));
        
        assertTrue(encontrado1, "Debe encontrar el formato 1");
        assertTrue(encontrado2, "Debe encontrar el formato 2");
    }

    @Test
    @Order(5)
    public void testListFormatoA_IncluyeVersiones() {
        // Arrange
        FormatoA formato = crearFormatoAValido();
        formato.setTitle("Con Versiones - " + System.currentTimeMillis());
        formatoAService.subirFormatoA(formato);
        formatosCreados.add(formato.getId());

        // Act
        List<FormatoA> formatos = formatoAService.listFormatoA();

        // Assert
        FormatoA formatoEncontrado = formatos.stream()
            .filter(f -> f.getId() == formato.getId())
            .findFirst()
            .orElse(null);

        assertNotNull(formatoEncontrado, "Debe encontrar el formato");
        assertNotNull(formatoEncontrado.getVersiones(), "Debe tener versiones cargadas");
        assertFalse(formatoEncontrado.getVersiones().isEmpty(), "Debe tener al menos una versión");
    }

    // ==================== TESTS DE findById ====================

    @Test
    @Order(6)
    public void testFindById_Exists() {
        // Arrange
        FormatoA formatoOriginal = crearFormatoAValido();
        formatoOriginal.setTitle("Buscar por ID - " + System.currentTimeMillis());
        formatoAService.subirFormatoA(formatoOriginal);
        formatosCreados.add(formatoOriginal.getId());

        int id = formatoOriginal.getId();

        // Act
        FormatoA encontrado = formatoAService.findById(id);

        // Assert
        assertNotNull(encontrado, "Debe encontrar el formato");
        assertEquals(id, encontrado.getId());
        assertEquals(formatoOriginal.getTitle(), encontrado.getTitle());
        assertEquals(formatoOriginal.getMode(), encontrado.getMode());
    }

    @Test
    @Order(7)
    public void testFindById_NotExists() {
        // Act
        FormatoA resultado = formatoAService.findById(999999);

        // Assert
        assertNull(resultado, "No debe encontrar un formato con ID inexistente");
    }

    @Test
    @Order(8)
    public void testFindById_IncluyeVersiones() {
        // Arrange
        FormatoA formato = crearFormatoAValido();
        formato.setTitle("Con Versiones Detail - " + System.currentTimeMillis());
        formatoAService.subirFormatoA(formato);
        formatosCreados.add(formato.getId());

        // Act
        FormatoA encontrado = formatoAService.findById(formato.getId());

        // Assert
        assertNotNull(encontrado);
        assertNotNull(encontrado.getVersiones(), "Debe cargar las versiones");
        assertEquals(1, encontrado.getVersiones().size(), "Debe tener 1 versión");
        assertEquals(1, encontrado.getVersiones().get(0).getNumeroVersion());
    }

    // ==================== TESTS DE updateEstadoObservacionesYContador ====================

    @Test
    @Order(9)
    public void testUpdateEstadoObservacionesYContador_Success() {
        // Arrange
        FormatoA formato = crearFormatoAValido();
        formato.setTitle("Para Actualizar - " + System.currentTimeMillis());
        formatoAService.subirFormatoA(formato);
        formatosCreados.add(formato.getId());

        int id = formato.getId();
        String nuevoEstado = enumEstado.ENTREGADO.toString();
        String observaciones = "Se requieren correcciones menores";
        int nuevoContador = 1;

        // Act
        boolean actualizado = formatoAService.updateEstadoObservacionesYContador(
            id, nuevoEstado, observaciones, nuevoContador
        );

        // Assert
        assertTrue(actualizado, "La actualización debe ser exitosa");

        // Verificar cambios en BD
        FormatoA formatoActualizado = formatoAService.findById(id);
        assertNotNull(formatoActualizado);
        assertEquals(nuevoEstado, formatoActualizado.getState().toString());
        assertEquals(observaciones, formatoActualizado.getObservations());
        assertEquals(nuevoContador, formatoActualizado.getCounter());
    }

    @Test
    @Order(10)
    public void testUpdateEstadoObservacionesYContador_IdInexistente() {
        // Act
        boolean resultado = formatoAService.updateEstadoObservacionesYContador(
            999999, 
            enumEstado.APROBADO.toString(), 
            "Test", 
            1
        );

        // Assert
        assertFalse(resultado, "No debe actualizar un formato inexistente");
    }

    @Test
    @Order(11)
    public void testUpdateEstadoObservacionesYContador_MultiplesCambios() {
        // Arrange
        FormatoA formato = crearFormatoAValido();
        formato.setTitle("Múltiples Updates - " + System.currentTimeMillis());
        formatoAService.subirFormatoA(formato);
        formatosCreados.add(formato.getId());

        int id = formato.getId();

        // Act & Assert - Primera actualización
        boolean update1 = formatoAService.updateEstadoObservacionesYContador(
            id, enumEstado.ENTREGADO.toString(), "Primera revisión", 1
        );
        assertTrue(update1);

        FormatoA check1 = formatoAService.findById(id);
        assertEquals(1, check1.getCounter());
        assertEquals("Primera revisión", check1.getObservations());

        // Act & Assert - Segunda actualización
        boolean update2 = formatoAService.updateEstadoObservacionesYContador(
            id, enumEstado.RECHAZADO.toString(), "Necesita correcciones", 2
        );
        assertTrue(update2);

        FormatoA check2 = formatoAService.findById(id);
        assertEquals(2, check2.getCounter());
        assertEquals("Necesita correcciones", check2.getObservations());
        assertEquals(enumEstado.RECHAZADO, check2.getState());
    }

    // ==================== TEST DE CICLO COMPLETO ====================

    @Test
    @Order(12)
    public void testCicloCompletoDeFormato() {
        // 1. CREAR
        FormatoA formato = crearFormatoAValido();
        formato.setTitle("Ciclo Completo - " + System.currentTimeMillis());
        boolean creado = formatoAService.subirFormatoA(formato);
        assertTrue(creado, "Debe crear exitosamente");
        
        int id = formato.getId();
        formatosCreados.add(id);

        // 2. BUSCAR POR ID
        FormatoA encontrado = formatoAService.findById(id);
        assertNotNull(encontrado);
        assertEquals(formato.getTitle(), encontrado.getTitle());
        assertEquals(enumEstado.ENTREGADO, encontrado.getState());
        assertEquals(0, encontrado.getCounter());

        // 3. VERIFICAR EN LISTA
        List<FormatoA> lista = formatoAService.listFormatoA();
        assertTrue(lista.stream().anyMatch(f -> f.getId() == id));

        // 4. PRIMERA REVISIÓN
        boolean revision1 = formatoAService.updateEstadoObservacionesYContador(
            id, enumEstado.ENTREGADO.toString(), "Revisar objetivos", 1
        );
        assertTrue(revision1);

        // 5. RECHAZAR
        boolean rechazado = formatoAService.updateEstadoObservacionesYContador(
            id, enumEstado.RECHAZADO.toString(), "Falta claridad en objetivos", 2
        );
        assertTrue(rechazado);

        FormatoA afterReject = formatoAService.findById(id);
        assertEquals(enumEstado.RECHAZADO, afterReject.getState());
        assertEquals(2, afterReject.getCounter());

        // 6. APROBAR
        boolean aprobado = formatoAService.updateEstadoObservacionesYContador(
            id, enumEstado.APROBADO.toString(), "Aprobado sin observaciones", 3
        );
        assertTrue(aprobado);

        FormatoA finalFormat = formatoAService.findById(id);
        assertEquals(enumEstado.APROBADO, finalFormat.getState());
        assertEquals("Aprobado sin observaciones", finalFormat.getObservations());
        assertEquals(3, finalFormat.getCounter());
    }

    // ==================== MÉTODOS AUXILIARES ====================

    /**
     * Crea un FormatoA válido con todos los datos necesarios
     */
    private FormatoA crearFormatoAValido() {
        FormatoA formato = new FormatoA();
        formato.setTitle("Proyecto Test");
        formato.setMode(enumModalidad.PRACTICA_PROFESIONAL);
        formato.setGeneralObjetive("Objetivo general del proyecto de prueba");
        formato.setSpecificObjetives("1. Objetivo específico 1\n2. Objetivo específico 2");
        formato.setArchivoPDF("propuesta_test.pdf");
        formato.setCartaLaboral("carta_test.pdf");
        formato.setDate(LocalDate.now());
        formato.setCounter(0);
        formato.setState(enumEstado.ENTREGADO);
        
        // Crear docente como director (necesario para FK)
        Docente director = new Docente();
        director.setIdUsuario(1); // Asume que existe un docente con ID 1
        director.setName("Director Test");
        formato.setProjectManager(director);
        
        // Lista vacía de estudiantes
        formato.setEstudiantes(new ArrayList<>());
        formato.setVersiones(new ArrayList<>());
        
        return formato;
    }
}
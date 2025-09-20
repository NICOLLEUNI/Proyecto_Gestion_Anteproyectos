/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package co.unicauca.workflow.access;

import co.unicauca.workflow.domain.entities.FormatoA;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author User
 */
public class FormatoARepositoryTest {
    
    private static FormatoARepository repo;
    
    @BeforeEach
    public void setUp() {
        repo = new FormatoARepository();
        repo.connect();
    }
    
    @AfterEach
    void resetDB() {
        if (repo != null && repo.getConnection() != null) {
            try (Statement stmt = repo.getConnection().createStatement()) {
                stmt.execute("DELETE FROM FormatoA");
            } catch (SQLException e) {
                System.out.println("⚠️ No se pudo limpiar la tabla FormatoA: " + e.getMessage());
            }
        }
    }

    /**
     * Test básico del método save().
     */
    @Test
    public void testSave() {
        FormatoA newFormatoA = new FormatoA();
        newFormatoA.setTitle("Proyecto Prueba");
        newFormatoA.setMode("Investigación");
        newFormatoA.setProyectManager("Docente 1");
        newFormatoA.setProjectCoManager("Docente 2");
        newFormatoA.setDate(LocalDate.now());
        newFormatoA.setGeneralObjetive("Objetivo General");
        newFormatoA.setSpecificObjetives("Objetivo Específico");
        newFormatoA.setArchivoPDF("ruta/prueba.pdf");
        newFormatoA.setStudentCode("2025A001");
        newFormatoA.setCounter(1);
        newFormatoA.setState("Entregado");
        newFormatoA.setObservations("Pendiente revisión");
        

        boolean result = repo.save(newFormatoA);
        assertTrue(result, "El FormatoA debería guardarse correctamente en la BD");
    }

    /**
     * Test para guardar con observaciones nulas.
     */
    @Test
    public void testSaveWithNullObservaciones() {
        FormatoA newFormatoA = new FormatoA();
        newFormatoA.setTitle("Proyecto Sin Observaciones");
        newFormatoA.setMode("Aplicación");
        newFormatoA.setProyectManager("Docente 3");
        newFormatoA.setProjectCoManager("Docente 4");
        newFormatoA.setDate(LocalDate.now());
        newFormatoA.setGeneralObjetive("Objetivo General sin obs");
        newFormatoA.setSpecificObjetives("Objetivos específicos sin obs");
        newFormatoA.setArchivoPDF("ruta/sinObs.pdf");
        newFormatoA.setStudentCode("2025A002");
        newFormatoA.setCounter(2);
        newFormatoA.setState("Aprobado");
        newFormatoA.setObservations(null);
       

        boolean result = repo.save(newFormatoA);
        assertTrue(result, "Debería permitir guardar aunque las observaciones sean nulas");
    }

    /**
     * Test para verificar valores del campo estado.
     */
    @Test
    public void testSaveWithDifferentEstados() {
        String[] estados = {"Rechazado", "Aprobado", "Entregado"};

        for (String estado : estados) {
            FormatoA newFormatoA = new FormatoA();
            newFormatoA.setTitle("Proyecto con estado " + estado);
            newFormatoA.setMode("Investigación");
            newFormatoA.setProyectManager("Docente X");
            newFormatoA.setProjectCoManager("Docente Y");
            newFormatoA.setDate(LocalDate.now());
            newFormatoA.setGeneralObjetive("Objetivo para estado " + estado);
            newFormatoA.setSpecificObjetives("Objetivo específico");
            newFormatoA.setArchivoPDF("ruta/" + estado + ".pdf");
            newFormatoA.setStudentCode("2025" + estado);
            newFormatoA.setCounter(3);
            newFormatoA.setState(estado);
            newFormatoA.setObservations("Observaciones de prueba");
          

            boolean result = repo.save(newFormatoA);
            assertTrue(result, "Debería guardar correctamente el estado: " + estado);
        }
    }

    /**
     * Test del método list().
     */
    @Test
    public void testList() {
        // Se guarda uno antes
        FormatoA formato = new FormatoA();
        formato.setTitle("Proyecto List Test");
        formato.setMode("Investigación");
        formato.setStudentCode("2025A003");
        repo.save(formato);

        List<FormatoA> result = repo.list();
        assertNotNull(result, "La lista no debería ser nula");
        assertTrue(result.size() > 0, "La lista debería contener al menos un registro");
    }

    /**
     * Test del método connect().
     */
    @Test
    public void testConnect() {
        repo.connect();
        Connection conn = repo.getConnection();
        assertNotNull(conn, "La conexión no debería ser nula después de connect()");
    }

    /**
     * Test del método getConnection().
     */
    @Test
    public void testGetConnection() {
        Connection conn = repo.getConnection();
        assertNotNull(conn, "Debería retornar la conexión establecida");
    }

    /**
     * Test del método disconnect().
     */
    @Test
    public void testDisconnect() {
        repo.disconnect();
        Connection conn = repo.getConnection();
        assertNull(conn, "La conexión debería ser nula después de disconnect()");
    }
}
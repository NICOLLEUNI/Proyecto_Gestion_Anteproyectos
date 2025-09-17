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
    
    public FormatoARepositoryTest() {
    }
    
   
    

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
    } else {
        System.out.println("⚠️ No se limpió la BD porque la conexión estaba cerrada.");
    }
}

    /**
     * Test del método save().
     */
    @Test
    public void testSave() {
        System.out.println("save");
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
        newFormatoA.setCounter("1");

        boolean result = repo.save(newFormatoA);
        assertTrue(result, "El FormatoA debería guardarse correctamente en la BD");
    }

    /**
     * Test del método list().
     */
    @Test
    public void testList() {
        System.out.println("list");
        List<FormatoA> result = repo.list();
        assertNotNull(result, "La lista de formatos no debería ser nula");
        assertTrue(result.size() >= 0, "La lista debería retornar registros existentes o vacía");
    }

    /**
     * Test del método connect().
     */
    @Test
    public void testConnect() {
        System.out.println("connect");
        repo.connect();
        Connection conn = repo.getConnection();
        assertNotNull(conn, "La conexión no debería ser nula después de connect()");
    }

    /**
     * Test del método disconnect().
     */
     /**
     * Test del método getConnection().
     */
    @Test
    public void testGetConnection() {
        System.out.println("getConnection");
        Connection conn = repo.getConnection();
        assertNotNull(conn, "Debería retornar la conexión establecida");
    }
    @Test
    public void testDisconnect() {
        System.out.println("disconnect");
        repo.disconnect();
        Connection conn = repo.getConnection();
        assertNull(conn, "La conexión debería ser nula después de disconnect()");
    }

    /**
     * Test del método getConnection().
     */
   
}
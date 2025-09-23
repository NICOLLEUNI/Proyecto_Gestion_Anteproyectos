/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package co.unicauca.workflow.access;

import co.unicauca.workflow.domain.entities.Departamento;
import co.unicauca.workflow.domain.entities.Estudiante;
import co.unicauca.workflow.domain.entities.Facultad;
import co.unicauca.workflow.domain.entities.Persona;
import co.unicauca.workflow.domain.entities.Programa;
import co.unicauca.workflow.domain.entities.enumRol;
import co.unicauca.workflow.domain.exceptions.ValidationException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.EnumSet;
import java.util.List;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class EstudianteRepositoryTest {
    
    private static EstudianteRepository repo;
     private int codProgramaValido; 
    
  @BeforeEach
public void setUp() throws SQLException {
    repo = new EstudianteRepository();
    repo.connect();

    String url = "jdbc:sqlite:" + System.getProperty("user.dir") + "/BD.db";
    try (Connection conn = DriverManager.getConnection(url);
         Statement stmt = conn.createStatement()) {

        // Insertar Facultad
        stmt.execute("INSERT INTO Facultad (idFacultad, nombre) VALUES (1, 'FIET')");

        // Insertar Departamento
        stmt.execute("INSERT INTO Departamento (idDepartamento, nombre, idFacultad) VALUES (1, 'SISTEMAS', 1)");

        // Insertar Programa
        stmt.execute("INSERT INTO Programa (codPrograma, nombre, idDepartamento) VALUES (1, 'sistemas', 1)");
    }
}

    @AfterEach
    
    void resetBD()
    {
        if (repo!=null)
        {
            repo.disconnect();
        }try {
        String url = "jdbc:sqlite:" + System.getProperty("user.dir") + "/BD.db";
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

        stmt.execute("DROP TABLE IF EXISTS Estudiante");
        stmt.execute("DROP TABLE IF EXISTS Programa");
        stmt.execute("DROP TABLE IF EXISTS Departamento");
        stmt.execute("DROP TABLE IF EXISTS Facultad");
        
        
        
          System.out.println(" Tablas de estudiantes y jerarquía limpiadas");


        }
    } catch (SQLException e) {
        System.out.println("⚠️ Error limpiando tablas: " + e.getMessage());
    }
        
    }
    
    public void testSaveWithRoles() throws ValidationException {
        
        Facultad facu = new Facultad("FIET");
        Departamento depto = new Departamento("SISTEMAS", facu);
        Programa programa = new Programa(1, "sistemas", depto);
        
        Estudiante estudiante = new Estudiante();
        estudiante.setIdEstudiante(1);
        estudiante.setProgram(programa);
        estudiante.setIdUsuario(2);
        estudiante.setName("Juan");
       estudiante.setLastname("Pérez");
       estudiante.setPhone("123456789");
        estudiante.setEmail("juan@example.com");
        estudiante.setPassword("secret");
        estudiante.setRoles(EnumSet.of(enumRol.ESTUDIANTE));

        boolean result = repo.save(estudiante);
        assertTrue(result, "La persona debería guardarse correctamente en la BD");
        assertTrue(estudiante.getIdUsuario() > 0, "El idUsuario debería generarse automáticamente");
    }
    
    @Test
    
    public void testSavedwithRoles() throws ValidationException
    {
        Facultad facu = new Facultad("FIET");
        Departamento depto = new Departamento("SISTEMAS", facu);
        Programa programa = new Programa(1, "sistemas", depto);
        Estudiante estudiante = new Estudiante();
        estudiante.setProgram(programa);  // necesario
        estudiante.setIdUsuario(1);  
        estudiante.setName("");
        estudiante.setLastname("Torres");
        estudiante.setEmail("cristhian@example.com");
        estudiante.setPassword("secret");
        estudiante.setRoles(EnumSet.of(enumRol.ESTUDIANTE));
        
        boolean result = repo.save(estudiante);
        assertFalse(result, "No debería permitir guardar una estudiante con nombre vacío");
    }
    

   /* @Test
    public void testList() throws ValidationException {
        
        Facultad facu = new Facultad("FIET");
        Departamento depto = new Departamento("SISTEMAS", facu);
        Programa programa = new Programa(1, "sistemas", depto);
        Estudiante estudiante = new Estudiante();
        estudiante.setName("Carlos");
        estudiante.setLastname("Ramírez");
        estudiante.setEmail("carlos@example.com");
        estudiante.setPhone("123456789");
        estudiante.setPassword("clave123");
        estudiante.setProgram(programa);
        estudiante.setRoles(EnumSet.of(enumRol.ESTUDIANTE));
        repo.save(estudiante);

        List<Estudiante> estudiantes = repo.list();
        assertNotNull(estudiantes, "La lista no debería ser nula");
        assertTrue(!estudiantes.isEmpty(), "La lista debería contener al menos un registro");
    }*/
    
    @Test
      public void testConnect() {
        repo.connect();
        Connection conn = repo.getConnection();
        assertNotNull(conn, "La conexión no debería ser nula después de connect()");
        
      }
      
       @Test
   public void testGetConnection() {
        Connection conn = repo.getConnection();
        assertNotNull(conn, "Debería retornar la conexión establecida");
    }
     @Test
    public void testDisconnect() {
        repo.disconnect();
        Connection conn = repo.getConnection();
        assertNull(conn, "La conexión debería ser nula después de disconnect()");
    }
    
}
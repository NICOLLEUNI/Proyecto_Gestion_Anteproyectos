/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.unicauca.workflow.access;

import co.unicauca.workflow.domain.entities.FormatoA;
import co.unicauca.workflow.domain.service.UserService;
import java.security.Provider.Service;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class FormatoARepository implements IFormatoARepository {
   
    private Connection conn;

    public FormatoARepository() {
        initDatabase();
    }

 
@Override
public boolean save(FormatoA newFormatoA) {
    try {
        // Validaciones básicas
        if (newFormatoA == null 
                || newFormatoA.getTitle() == null || newFormatoA.getTitle().isBlank()
                || newFormatoA.getMode() == null || newFormatoA.getMode().isBlank()
                || newFormatoA.getStudentCode() == null || newFormatoA.getStudentCode().isBlank()) {
            return false;
        }

        String sql = "INSERT INTO FormatoA (title, mode, proyectManager, projectCoManager, date, generalObjetive, specificObjetives, archivoPDF, studentCode, counter) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, newFormatoA.getTitle());
        pstmt.setString(2, newFormatoA.getMode());
        pstmt.setString(3, newFormatoA.getProyectManager());
        pstmt.setString(4, newFormatoA.getProjectCoManager());
        pstmt.setString(5, newFormatoA.getDate() != null ? newFormatoA.getDate().toString() : null);
        pstmt.setString(6, newFormatoA.getGeneralObjetive());
        pstmt.setString(7, newFormatoA.getSpecificObjetives());
        pstmt.setString(8, newFormatoA.getArchivoPDF()); // aquí guardas la ruta al PDF
        pstmt.setString(9, newFormatoA.getStudentCode());
        pstmt.setString(10, newFormatoA.getCounter());

        pstmt.executeUpdate();
        return true;
    } catch (SQLException ex) {
        Logger.getLogger(FormatoARepository.class.getName()).log(Level.SEVERE, null, ex);
    }
    return false;
}
    @Override
    public List<FormatoA> list() {
        List<FormatoA> formatos = new ArrayList<>();
        try {

            String sql = "SELECT id, title, mode, proyectManager, projectCoManager, date, generalObjetive, specificObjetives, archivoPDF, studentCode, counter FROM FormatoA";
            //this.connect();

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                FormatoA newFormatoA = new FormatoA();
                newFormatoA.setId(rs.getInt("id"));
                newFormatoA.setTitle(rs.getString("title"));
                newFormatoA.setMode(rs.getString("mode"));
                newFormatoA.setProyectManager(rs.getString("projectCoManager"));
                newFormatoA.setDate(rs.getString("date") != null ? LocalDate.parse(rs.getString("date")) : null);
                newFormatoA.setGeneralObjetive(rs.getString("generalObjetive"));
                newFormatoA.setSpecificObjetives(rs.getString("specificObjetives"));
                newFormatoA.setArchivoPDF(rs.getString("archivoPDF"));
                newFormatoA.setStudentCode(rs.getString("studentCode"));
                newFormatoA.setCounter(rs.getString("counter"));
                
                formatos.add(newFormatoA);
            }
            //this.disconnect();

        } catch (SQLException ex) {
            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
        }
        return formatos;
    }

   
    private void initDatabase() {
    // SQL statement for creating the FormatoA table
    String sql = "CREATE TABLE IF NOT EXISTS FormatoA (\n"
            + "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
            + "    title TEXT NOT NULL,\n"
            + "    mode TEXT NOT NULL,\n"
            + "    proyectManager TEXT,\n"
            + "    projectCoManager TEXT,\n"
            + "    date DATE,\n"
            + "    generalObjetive TEXT,\n"
            + "    specificObjetives TEXT,\n"
            + "    archivoPDF TEXT,\n"
            + "    studentCode TEXT NOT NULL,\n"
            + "    counter TEXT\n"
            + ");";

    try {
        this.connect();
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
        //this.disconnect();

    } catch (SQLException ex) {
        Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
    }
}
    
    
  public void connect() {
        // SQLite connection string

        String url = "jdbc:sqlite:./BD.db";
      
        try {
            conn = DriverManager.getConnection(url);
             System.out.println("Conectado a la BD en archivo");

        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void disconnect() {
        try {
            if (conn != null) {
                conn.close();
                conn = null;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }
     public Connection getConnection() {
        return conn;
    }
 
}

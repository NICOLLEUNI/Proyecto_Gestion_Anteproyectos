/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.unicauca.workflow.access;

import co.unicauca.workflow.domain.entities.Docente;
import co.unicauca.workflow.domain.entities.Facultad;
import co.unicauca.workflow.domain.entities.Departamento;
import co.unicauca.workflow.domain.exceptions.ValidationException;
import java.util.List;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;


/**
 *
 * @author CRISTHIAN TORRES
 */
public class DocenteRepository implements IDocenteRepository {
    
    public Connection conn;
    
        public DocenteRepository()
        {
            initDatabase();
        }
    
   
    

    @Override
    public boolean save(Docente newDocente) {
        
        // Validaciones simples
        if (newDocente == null || newDocente.getDepartamento() == null) {
            return false;
        }

        String sql = "INSERT INTO Docente (nombre, apellido, telefono, email, password, codDepartamento) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, newDocente.getName());
            pstmt.setString(2, newDocente.getLastname());
            pstmt.setString(3, newDocente.getPhone());
            pstmt.setString(4, newDocente.getEmail());
            pstmt.setString(5, newDocente.getPassword());
            pstmt.setInt(6, newDocente.getDepartamento().getCodDepartamento());

            int affected = pstmt.executeUpdate();
            if (affected == 0) {
                return false;
            }

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    newDocente.setCodDocente(rs.getInt(1));
                }
            }

            return true;
        } catch (SQLException e) {
            Logger.getLogger(DocenteRepository.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

@Override
    public List<Docente> list() {
        List<Docente> docentes = new ArrayList<>();
        String sql = "SELECT d.codDocente, d.nombre, d.apellido, d.telefono, d.email, d.password, " +
                     "dep.codDepartamento, dep.nombre AS depNombre, " +
                     "fac.codFacultad, fac.nombre AS facNombre " +
                     "FROM Docente d " +
                     "JOIN Departamento dep ON d.codDepartamento = dep.codDepartamento " +
                     "JOIN Facultad fac ON dep.codFacultad = fac.codFacultad";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Facultad
                Facultad fac = new Facultad(rs.getString("facNombre"));

                // Departamento
                Departamento dep = new Departamento(rs.getString("depNombre"), fac);

                // Docente
                Docente docente = new Docente(
                        dep,
                         rs.getInt("idUsuario"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("password")
                );
                docente.setCodDocente(rs.getInt("codDocente"));

                docentes.add(docente);
            }

        } catch (SQLException | ValidationException ex) {
            Logger.getLogger(DocenteRepository.class.getName()).log(Level.SEVERE, null, ex);
        }

        return docentes;
    }
    
     private void initDatabase() {
        String sqlDocente = "CREATE TABLE IF NOT EXISTS Docente ("
                + "codDocente INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + "nombre TEXT NOT NULL, "
                + "apellido TEXT NOT NULL, "
                + "telefono TEXT, "
                + "email TEXT NOT NULL UNIQUE, "
                + "password TEXT NOT NULL, "
                + "codDepartamento INTEGER NOT NULL, "
                + "FOREIGN KEY (codDepartamento) REFERENCES Departamento(codDepartamento)"
                + ");";

        try {
            this.connect();
            Statement stmt = conn.createStatement();
            stmt.execute(sqlDocente);
        } catch (SQLException ex) {
            Logger.getLogger(DocenteRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
     public void connect() {
        try {
            if (conn == null || conn.isClosed()) {
                String url = "jdbc:sqlite:" + System.getProperty("user.dir") + "/BD.db";
                conn = DriverManager.getConnection(url);
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute("PRAGMA busy_timeout = 5000");
                }
                System.out.println("Conectado a la BD en archivo");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
     
     public Connection getConnection() {
        return conn;
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
    
}
    
    
    


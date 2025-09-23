/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.unicauca.workflow.access;
import co.unicauca.workflow.domain.entities.Departamento;
import co.unicauca.workflow.domain.entities.Facultad;
import co.unicauca.workflow.domain.exceptions.ValidationException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author CRISTHIAN TORRES
 */
public class DepartamentoRepository implements IDepartamentoRepository{
    
    
    public Connection conn;
    
    public DepartamentoRepository()
    {
        
        initDatabase();
    }

    @Override
    public boolean save(Departamento newDepartamento) {
        
       try {
            // Validación de negocio en la entidad
            newDepartamento.validarCamposDepartamento();

            String sql = "INSERT INTO Departamento (depNombre, codFacultad) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, newDepartamento.getNombre());
                pstmt.setInt(2, newDepartamento.getFacultad().getCodFacultad());
                int affected = pstmt.executeUpdate();

                if (affected == 0) {
                    return false;
                }

                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        newDepartamento.setCodDepartamento(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (ValidationException ex) {
            System.err.println("Error de validación en Departamento: " + ex.getMessage());
            return false;
        } catch (SQLException e) {
            Logger.getLogger(DepartamentoRepository.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    @Override
    public List<Departamento> list() {
       
        List<Departamento> departamentos = new ArrayList<>();

        String sql = "SELECT d.codDepartamento, d.depNombre, " +
                     "f.codFacultad, f.facNombre AS facNombre " +
                     "FROM Departamento d " +
                     "JOIN Facultad f ON d.codFacultad = f.codFacultad";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Crear Facultad
                Facultad fac = new Facultad(
                    rs.getString("facNombre")
                );

                // Crear Departamento
                Departamento dep = new Departamento(
                        rs.getString("depNombre"),
                        fac
                );
                dep.setCodDepartamento(rs.getInt("codDepartamento"));

                departamentos.add(dep);
            }

        } catch (SQLException e) {
            Logger.getLogger(DepartamentoRepository.class.getName()).log(Level.SEVERE, null, e);
        } catch (ValidationException ex) {
            // Si falla la validación de negocio al reconstruir el objeto
            System.err.println("Error reconstruyendo Departamento: " + ex.getMessage());
        }

        return departamentos;
    }

    private void initDatabase() {
        

        String sqlDepartamento = "CREATE TABLE IF NOT EXISTS Departamento ("
                + "codDepartamento INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + "depNombre TEXT NOT NULL, "
                + "codFacultad INTEGER NOT NULL, "
                + "FOREIGN KEY (codFacultad) REFERENCES Facultad(codFacultad)"
                + ");";
       
        try {
            this.connect();
            Statement stmt = conn.createStatement();
            
            stmt.execute(sqlDepartamento);
        } catch (SQLException ex) {
            Logger.getLogger(DepartamentoRepository.class.getName()).log(Level.SEVERE, null, ex);
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

package co.unicauca.workflow.access;

import co.unicauca.workflow.domain.entities.Programa;
import co.unicauca.workflow.domain.entities.Departamento;
import co.unicauca.workflow.domain.entities.Facultad;
import co.unicauca.workflow.domain.entities.enumProgram;
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
 * Implementación concreta del repositorio de Programa
 * 
 * @author USUARIO
 */
public class ProgramaRepository implements IProgramaRepository {

    public Connection conn;

    public ProgramaRepository() {
        initDatabase();
    }
@Override
    public boolean save(Programa newPrograma) {
        try {
            // Validación de negocio en la entidad
            newPrograma.validarCamposPrograma(); 

            String sql = "INSERT INTO Programa (nombrePrograma, codDepartamento) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, newPrograma.getNombrePrograma());
                pstmt.setInt(2, newPrograma.getDepartamento().getCodDepartamento());

                int affected = pstmt.executeUpdate();

                if (affected == 0) {
                    return false;
                }

                // Obtener la clave generada (codPrograma)
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        newPrograma.setCodPrograma(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (ValidationException ex) {
            System.err.println("Error de validación en Programa: " + ex.getMessage());
            return false;
        } catch (SQLException e) {
            Logger.getLogger(ProgramaRepository.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    public List<Programa> list() {
        List<Programa> programas = new ArrayList<>();

        String sql = "SELECT p.codPrograma, p.nombrePrograma, " +
                     "d.codDepartamento, d.depNombre, " +
                     "f.codFacultad, f.facNombre " +
                     "FROM Programa p " +
                     "JOIN Departamento d ON p.codDepartamento = d.codDepartamento " +
                     "JOIN Facultad f ON d.codFacultad = f.codFacultad";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // Crear Facultad
Facultad fac = new Facultad(
    rs.getInt("codFacultad"),
    rs.getString("facNombre")
);

                fac.setCodFacultad(rs.getInt("codFacultad"));

                // Crear Departamento
                Departamento dep = new Departamento(rs.getString("depNombre"), fac);
                dep.setCodDepartamento(rs.getInt("codDepartamento"));

                // Crear Programa
                Programa prog = new Programa();
                prog.setCodPrograma(rs.getInt("codPrograma"));
                prog.setNombrePrograma(rs.getString("nombrePrograma"));
                prog.setDepartamento(dep);

                programas.add(prog);
            }

        } catch (SQLException e) {
            Logger.getLogger(ProgramaRepository.class.getName()).log(Level.SEVERE, null, e);
        } catch (ValidationException ex) {
            System.err.println("Error reconstruyendo Programa: " + ex.getMessage());
        }

        return programas;
    }

    private void initDatabase() {
        String sqlPrograma = "CREATE TABLE IF NOT EXISTS Programa ("
                + "codPrograma INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + "nombrePrograma TEXT NOT NULL, "
                + "codDepartamento INTEGER NOT NULL, "
                + "FOREIGN KEY (codDepartamento) REFERENCES Departamento(codDepartamento)"
                + ");";

        try {
            this.connect();
            Statement stmt = conn.createStatement();
            stmt.execute(sqlPrograma);
        } catch (SQLException ex) {
            Logger.getLogger(ProgramaRepository.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(ProgramaRepository.class.getName()).log(Level.SEVERE, null, e);
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
                System.out.println("Conexión cerrada");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ProgramaRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Programa findById(int codPrograma) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Programa> findByPrograma(enumProgram programa) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Programa> findByNombre(String nombrePrograma) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Programa> findByDepartamento(int codDepartamento) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Programa> findByFacultad(int codFacultad) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean update(Programa programa) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean delete(int codPrograma) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean existsByNombreAndFacultad(String nombrePrograma, int codFacultad) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}

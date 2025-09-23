package co.unicauca.workflow.access;

import co.unicauca.workflow.domain.entities.Programa;
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
    public boolean save(Programa programa) {
        try {
            if (programa == null || programa.getNombrePrograma() == null || programa.getNombrePrograma().isBlank()) {
                return false;
            }
            if (programa.getDepartamento() == null || programa.getDepartamento().getCodDepartamento() <= 0) {
                return false;
            }

            String sql = "INSERT INTO Programa (nombre, codDepartamento) VALUES (?, ?)";

            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, programa.getNombrePrograma());
            pstmt.setInt(2, programa.getDepartamento().getCodDepartamento());

            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    programa.setCodPrograma(rs.getInt(1));
                    System.out.println("Programa creado con ID: " + programa.getCodPrograma());
                }
                return true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(ProgramaRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public List<Programa> list() {
        List<Programa> programas = new ArrayList<>();
        try {
            String sql = "SELECT p.codPrograma, p.nombre AS nombrePrograma, " +
                         "d.codDepartamento, d.nombre AS nombreDepartamento, " +
                         "f.codFacultad, f.nombre AS nombreFacultad " +
                         "FROM Programa p " +
                         "JOIN Departamento d ON p.codDepartamento = d.codDepartamento " +
                         "JOIN Facultad f ON d.codFacultad = f.codFacultad";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                // Crear Facultad
                Facultad fac = new Facultad(rs.getString("nombreFacultad"));
                fac.setCodFacultad(rs.getInt("codFacultad"));

                // Crear Departamento
                Departamento dep = new Departamento(rs.getString("nombreDepartamento"), fac);
                dep.setCodDepartamento(rs.getInt("codDepartamento"));

                // Crear Programa
                Programa prog = new Programa(rs.getString("nombrePrograma"), dep);
                prog.setCodPrograma(rs.getInt("codPrograma"));

                programas.add(prog);
            }

        } catch (SQLException ex) {
            Logger.getLogger(ProgramaRepository.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ValidationException ex) {
            Logger.getLogger(ProgramaRepository.class.getName()).log(Level.WARNING, "Error validando programa desde BD", ex);
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
}

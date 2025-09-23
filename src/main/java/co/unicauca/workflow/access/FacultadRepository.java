package co.unicauca.workflow.access;

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
 * Repositorio para la entidad Facultad.
 * Maneja operaciones CRUD contra la base de datos.
 * 
 * @author User
 */
public class FacultadRepository {

    private Connection conn;

    public FacultadRepository() {
        initDatabase();
    }

    /**
     * Guarda una nueva Facultad en la BD.
     * 
     * @param newFacultad entidad Facultad
     * @return true si fue insertada, false en caso contrario
     */
    public boolean save(Facultad newFacultad) {
        try {
            // Validación de negocio
            // (la propia clase Facultad ya valida en el constructor/setters)
            String sql = "INSERT INTO Facultad (facNombre) VALUES (?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, newFacultad.getNombre());

                int affected = pstmt.executeUpdate();
                if (affected == 0) {
                    return false;
                }

                // Obtener clave generada
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        newFacultad.setCodFacultad(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (ValidationException ex) {
            System.err.println("Error de validación en Facultad: " + ex.getMessage());
            return false;
        } catch (SQLException e) {
            Logger.getLogger(FacultadRepository.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    /**
     * Lista todas las Facultades de la BD.
     */
    public List<Facultad> list() {
        List<Facultad> facultades = new ArrayList<>();
        String sql = "SELECT codFacultad, facNombre FROM Facultad";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Facultad fac = new Facultad(
                    rs.getInt("codFacultad"),
                    rs.getString("facNombre")
                );
                facultades.add(fac);
            }

        } catch (SQLException e) {
            Logger.getLogger(FacultadRepository.class.getName()).log(Level.SEVERE, null, e);
        } catch (ValidationException ex) {
            System.err.println("Error reconstruyendo Facultad: " + ex.getMessage());
        }

        return facultades;
    }

    /**
     * Busca una Facultad por su ID.
     */
    public Facultad findById(int codFacultad) {
        String sql = "SELECT codFacultad, facNombre FROM Facultad WHERE codFacultad = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, codFacultad);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Facultad(
                        rs.getInt("codFacultad"),
                        rs.getString("facNombre")
                    );
                }
            }
        } catch (SQLException | ValidationException e) {
            Logger.getLogger(FacultadRepository.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    /**
     * Actualiza una Facultad.
     */
    public boolean update(Facultad facultad) {
        String sql = "UPDATE Facultad SET facNombre = ? WHERE codFacultad = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, facultad.getNombre());
            pstmt.setInt(2, facultad.getCodFacultad());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(FacultadRepository.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    /**
     * Elimina una Facultad por su código.
     */
    public boolean delete(int codFacultad) {
        String sql = "DELETE FROM Facultad WHERE codFacultad = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, codFacultad);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            Logger.getLogger(FacultadRepository.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    /**
     * Inicializa la BD creando la tabla si no existe.
     */
    private void initDatabase() {
        String sqlFacultad = "CREATE TABLE IF NOT EXISTS Facultad ("
                + "codFacultad INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + "facNombre TEXT NOT NULL"
                + ");";

        try {
            this.connect();
            Statement stmt = conn.createStatement();
            stmt.execute(sqlFacultad);
        } catch (SQLException ex) {
            Logger.getLogger(FacultadRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Conexión a la BD SQLite.
     */
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
            Logger.getLogger(FacultadRepository.class.getName()).log(Level.SEVERE, null, e);
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
            Logger.getLogger(FacultadRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}



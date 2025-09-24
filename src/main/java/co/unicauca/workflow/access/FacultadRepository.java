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
public class FacultadRepository implements IFacultadRepository{

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
    @Override
    public boolean save(Facultad fac) {
        try {
            // 🔹 Validación básica
            if (fac == null || fac.getNombre() == null || fac.getNombre().isBlank()) {
                return false;
            }

            String sql = "INSERT INTO Facultad (nombre) VALUES (?)";

            // 🔹 Solicitamos que nos devuelva el codFacultad generado
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, fac.getNombre());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                // Recuperar el codFacultad generado
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idGenerado = rs.getInt(1);
                        fac.setCodFacultad(idGenerado);
                    }
                }
                return true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(FacultadRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }


    /**
     * Lista todas las Facultades de la BD.
     */
    @Override
    public List<Facultad> list() {
        List<Facultad> facultades = new ArrayList<>();
        try {
            String sql = "SELECT codFacultad, nombre FROM Facultad";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Facultad fac = new Facultad(rs.getString("nombre"));
                fac.setCodFacultad(rs.getInt("codFacultad"));

                facultades.add(fac);
            }

        } catch (SQLException ex) {
            Logger.getLogger(FacultadRepository.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ValidationException ex) {
            // En caso de que falle la validación de nombre (aunque ya viene de BD)
            Logger.getLogger(FacultadRepository.class.getName()).log(Level.WARNING, "Facultad inválida recuperada", ex);
        }
        return facultades;
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
    @Override
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
    
    
    @Override
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



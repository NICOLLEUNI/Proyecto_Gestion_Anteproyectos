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
public class FacultadRepository implements IFacultadRepository {

    private Connection conn;

    public FacultadRepository() {
        initDatabase();
    }

    /**
     * Guarda una nueva Facultad en la BD.
     * 
     * @param fac entidad Facultad
     * @return true si fue insertada, false en caso contrario
     */
    
    // Dentro de FacultadRepository.java
   
    
    @Override
public boolean save(Facultad fac) {
    try {
        System.out.println("=== SAVE FACULTAD DEBUG ===");
        
        // Validación básica
        if (fac == null || fac.getNombre() == null || fac.getNombre().isBlank()) {
            System.err.println("Facultad o nombre es null/vacío");
            return false;
        }
        
        System.out.println("Facultad a guardar: " + fac.getNombre());
        System.out.println("Conexión: " + (conn != null ? "OK" : "NULL"));
        
        if (conn == null) {
            System.err.println("Conexión es NULL, intentando conectar...");
            this.connect();
            System.out.println("Después de connect: " + (conn != null ? "OK" : "NULL"));
        }

        String sql = "INSERT INTO Facultad (nombre) VALUES (?)";
        System.out.println("SQL a ejecutar: " + sql);

        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pstmt.setString(1, fac.getNombre());

        int rowsAffected = pstmt.executeUpdate();
        System.out.println("Rows affected: " + rowsAffected);

        if (rowsAffected > 0) {
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);
                    fac.setCodFacultad(idGenerado);
                    System.out.println("ID generado: " + idGenerado);
                }
            }
            return true;
        }

    } catch (SQLException ex) {
        System.err.println("SQLException en save: " + ex.getMessage());
        System.err.println("SQLState: " + ex.getSQLState());
        System.err.println("Error Code: " + ex.getErrorCode());
        ex.printStackTrace();
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
            // ← CORREGIDO: Usar "nombre" consistente con el esquema
            String sql = "SELECT codFacultad, nombre FROM Facultad";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                Facultad fac = new Facultad(rs.getString("nombre"));
                fac.setCodFacultad(rs.getInt("codFacultad"));
                facultades.add(fac);
            }

            System.out.println("Facultades encontradas: " + facultades.size());

        } catch (SQLException ex) {
            System.err.println("Error SQL al listar facultades: " + ex.getMessage());
            ex.printStackTrace();
        } catch (ValidationException ex) {
            System.err.println("Error validando facultad: " + ex.getMessage());
        }
        return facultades;
    }

    /**
     * Inicializa la BD creando la tabla si no existe.
     */
    private void initDatabase() {
        // ← CAMBIADO: Usar "nombre" en lugar de "facNombre" para consistencia
        String sqlFacultad = "CREATE TABLE IF NOT EXISTS Facultad ("
                + "codFacultad INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + "nombre TEXT NOT NULL"  // ← Cambiado de "facNombre" a "nombre"
                + ");";

        try {
            this.connect();
            Statement stmt = conn.createStatement();
            stmt.execute(sqlFacultad);
            System.out.println("Tabla Facultad creada/verificada");
        } catch (SQLException ex) {
            System.err.println("Error creando tabla Facultad: " + ex.getMessage());
            ex.printStackTrace();
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
                System.out.println("Conectado a la BD en archivo (FacultadRepository)");
            }
        } catch (SQLException e) {
            System.err.println("Error conectando a BD: " + e.getMessage());
            e.printStackTrace();
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
                System.out.println("Conexión FacultadRepository cerrada");
            }
        } catch (SQLException ex) {
            System.err.println("Error cerrando conexión: " + ex.getMessage());
        }
    }
}
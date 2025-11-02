package co.unicauca.workflow.access;

import co.unicauca.workflow.domain.entities.FormatoA;
import co.unicauca.workflow.domain.entities.FormatoAVersion;
import co.unicauca.workflow.domain.entities.enumEstado;
import co.unicauca.workflow.domain.entities.enumModalidad;
import co.unicauca.workflow.domain.service.FormatoAService;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FormatoAVersionRepository implements IFormatoAVersionRepository {

    private Connection conn;

    public FormatoAVersionRepository() {
        initDatabase();
    }

    @Override
    public boolean save(FormatoAVersion newVersion) {
        if (newVersion == null || newVersion.getFormatoA() == null) {
            return false;
        }

        String sql = "INSERT INTO FormatoAVersion (numeroVersion, fecha, title, mode, generalObjetive, specificObjetives, archivoPDF, cartaLaboral, state, observations, formatoA_id) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, newVersion.getNumeroVersion());
            pstmt.setString(2, newVersion.getFecha() != null ? newVersion.getFecha().toString() : null);
            pstmt.setString(3, newVersion.getTitle());
            pstmt.setString(4, newVersion.getMode() != null ? newVersion.getMode().name() : null);
            pstmt.setString(5, newVersion.getGeneralObjetive());
            pstmt.setString(6, newVersion.getSpecificObjetives());
            pstmt.setString(7, newVersion.getArchivoPDF());
            pstmt.setString(8, newVersion.getCartaLaboral());
            pstmt.setString(9, newVersion.getState() != null ? newVersion.getState().name() : null);
            pstmt.setString(10, newVersion.getObservations());
            pstmt.setInt(11, newVersion.getFormatoA().getId());

            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        newVersion.setIdCopia(generatedKeys.getInt(1));
                    }
                }
                return true;
            }

        } catch (SQLException ex) {
            Logger.getLogger(FormatoAVersionRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public List<FormatoAVersion> list() {
        List<FormatoAVersion> versiones = new ArrayList<>();
        String sql = "SELECT * FROM FormatoAVersion";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                versiones.add(mapResultSetToVersion(rs));
            }

        } catch (SQLException ex) {
            Logger.getLogger(FormatoAVersionRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return versiones;
    }

    @Override
    public FormatoAVersion findById(int id) {
        String sql = "SELECT * FROM FormatoAVersion WHERE idCopia = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToVersion(rs);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(FormatoAVersionRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public boolean update(FormatoAVersion version) {
        String sql = "UPDATE FormatoAVersion SET state = ?, observations = ? WHERE idCopia = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, version.getState() != null ? version.getState().name() : null);
            pstmt.setString(2, version.getObservations());
            pstmt.setInt(3, version.getIdCopia());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(FormatoAVersionRepository.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    // Nuevo método para cargar las versiones de un FormatoA específico
    @Override
    public List<FormatoAVersion> listByFormatoA(int formatoAId) {
        List<FormatoAVersion> versiones = new ArrayList<>();
        String sql = "SELECT * FROM FormatoAVersion WHERE formatoA_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, formatoAId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    versiones.add(mapResultSetToVersion(rs));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(FormatoAVersionRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return versiones;
    }

    private FormatoAVersion mapResultSetToVersion(ResultSet rs) throws SQLException {
        FormatoAVersion v = new FormatoAVersion(
                rs.getInt("idCopia"),
                rs.getInt("numeroVersion"),
                rs.getString("fecha") != null ? LocalDate.parse(rs.getString("fecha")) : null,
                rs.getString("title"),
                rs.getString("mode") != null ? enumModalidad.valueOf(rs.getString("mode")) : null,
                rs.getString("generalObjetive"),
                rs.getString("specificObjetives"),
                rs.getString("archivoPDF"),
                rs.getString("cartaLaboral"),
                rs.getString("state") != null ? enumEstado.valueOf(rs.getString("state")) : null,
                rs.getString("observations"),
                new FormatoA() // se crea un objeto vacío solo con el id
        );
        v.getFormatoA().setId(rs.getInt("formatoA_id"));
        return v;
    }

    private void initDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS FormatoAVersion (\n"
                + "idCopia INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "numeroVersion INTEGER NOT NULL,\n"
                + "fecha TEXT,\n"
                + "title TEXT,\n"
                + "mode TEXT,\n"
                + "generalObjetive TEXT,\n"
                + "specificObjetives TEXT,\n"
                + "archivoPDF TEXT,\n"
                + "cartaLaboral TEXT,\n"
                + "state TEXT CHECK(state IN ('RECHAZADO','APROBADO','ENTREGADO')),\n"
                + "observations TEXT,\n"
                + "formatoA_id INTEGER NOT NULL,\n"
                + "FOREIGN KEY (formatoA_id) REFERENCES FormatoA(id)\n"
                + ");";

        try {
            this.connect();
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
            }
        } catch (SQLException ex) {
            Logger.getLogger(FormatoAVersionRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean deleteByFormatoAId(int formatoAId) {
        String sql = "DELETE FROM FormatoAVersion WHERE formatoA_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, formatoAId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            Logger.getLogger(FormatoAVersionRepository.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    public void connect() {
        // Se asume que ConexionSQLite.getConnection() maneja la ruta y el driver.
        try {
            conn = ConexionSQLite.getConnection();
            if (conn != null) {
                System.out.println("Conectado a la BD (FormatoAVersion)");
            } else {
                Logger.getLogger(FormatoAVersionRepository.class.getName()).log(Level.WARNING, "Conexion devuelta nula.");
            }
        } catch (SQLException ex) {
            Logger.getLogger(FormatoAVersionRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void disconnect() {
        try {
            if (conn != null) {
                conn.close();
                conn = null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(FormatoAVersionRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Connection getConnection() {
        return conn;
    }
}

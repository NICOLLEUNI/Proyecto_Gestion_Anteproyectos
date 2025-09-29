package co.unicauca.workflow.access;

import co.unicauca.workflow.domain.entities.*;
import co.unicauca.workflow.domain.service.FormatoAService;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FormatoARepository implements IFormatoARepository {

    private Connection conn;

    public FormatoARepository() {
        initDatabase();
    }

    @Override
    public boolean save(FormatoA newFormatoA) {
        String sql = "INSERT INTO FormatoA (title, mode, projectManager, projectCoManager, date, generalObjetive, specificObjetives, archivoPDF, cartaLaboral, counter, state, observations) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            conn.setAutoCommit(false); // 🔹 iniciar transacción

            try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                pstmt.setString(1, newFormatoA.getTitle());
                pstmt.setString(2, newFormatoA.getMode().name());
                pstmt.setObject(3, newFormatoA.getProjectManager().getIdUsuario());
                pstmt.setObject(4, newFormatoA.getProjectCoManager() != null ? newFormatoA.getProjectCoManager().getIdUsuario() : null);
                pstmt.setString(5, newFormatoA.getDate() != null ? newFormatoA.getDate().toString() : null);
                pstmt.setString(6, newFormatoA.getGeneralObjetive());
                pstmt.setString(7, newFormatoA.getSpecificObjetives());
                pstmt.setString(8, newFormatoA.getArchivoPDF());
                pstmt.setString(9, newFormatoA.getCartaLaboral());
                pstmt.setInt(10, newFormatoA.getCounter());
                pstmt.setString(11, newFormatoA.getState() != null ? newFormatoA.getState().name() : enumEstado.ENTREGADO.name());
                pstmt.setString(12, newFormatoA.getObservations());

                int rows = pstmt.executeUpdate();

                if (rows > 0) {
                    try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int formatoId = generatedKeys.getInt(1);

                            // 🔥🔥🔥 ESTA ES LA LÍNEA QUE FALTA 🔥🔥🔥
                            newFormatoA.setId(formatoId); // Asignar el ID al objeto

                            System.out.println("DEBUG - FormatoA ID asignado: " + formatoId);

                            if (newFormatoA.getEstudiantes() != null) {
                                String sqlEst = "INSERT INTO FormatoA_Estudiante (formatoA_id, estudiante_id) VALUES (?, ?)";
                                for (Estudiante est : newFormatoA.getEstudiantes()) {
                                    try (PreparedStatement pstmtEst = conn.prepareStatement(sqlEst)) {
                                        pstmtEst.setInt(1, formatoId);
                                        pstmtEst.setInt(2, est.getIdUsuario());
                                        pstmtEst.executeUpdate();
                                    }
                                }
                            }
                        }
                    }
                }
            }

            conn.commit(); // 🔹 confirmar transacción
            return true;

        } catch (SQLException ex) {
            try {
                conn.rollback(); // 🔹 revertir si falla
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Logger.getLogger(FormatoARepository.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                conn.setAutoCommit(true); // 🔹 restaurar
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    @Override
    public List<FormatoA> list() {
        List<FormatoA> formatos = new ArrayList<>();
        try {
            String sql = "SELECT * FROM FormatoA";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            FormatoAVersionRepository repoVersiones = new FormatoAVersionRepository();

            while (rs.next()) {
                
                FormatoA f = mapFormatoA(rs);
                
                //aquí conectamos el formatoA con sus versiones 
                f.setVersiones(repoVersiones.listByFormatoA(f.getId()));
                formatos.add(f);
            }

        } catch (SQLException ex) {
            Logger.getLogger(FormatoARepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return formatos;
    }

    @Override
    public FormatoA findById(int id) {
        try {
            String sql = "SELECT * FROM FormatoA WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                FormatoA f = mapFormatoA(rs);

                // 👇 Aquí conectamos con las versiones
                FormatoAVersionRepository repoVersiones = new FormatoAVersionRepository();
                f.setVersiones(repoVersiones.listByFormatoA(f.getId()));

                return f;
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar FormatoA por id: " + e.getMessage());
        }
        return null;
    }
    
    @Override
    public boolean update(FormatoA formato) {
    String sql = "UPDATE FormatoA SET title = ?, mode = ?, projectManager = ?, projectCoManager = ?, date = ?, "
               + "generalObjetive = ?, specificObjetives = ?, archivoPDF = ?, cartaLaboral = ?, counter = ?, "
               + "state = ?, observations = ? WHERE id = ?";

    try (Connection c = ConexionSQLite.getConnection();
         PreparedStatement ps = c.prepareStatement(sql)) {

        ps.setString(1, formato.getTitle());
        ps.setString(2, formato.getMode() != null ? formato.getMode().name() : null);
        ps.setObject(3, formato.getProjectManager() != null ? formato.getProjectManager().getIdUsuario() : null);
        ps.setObject(4, formato.getProjectCoManager() != null ? formato.getProjectCoManager().getIdUsuario() : null);
        ps.setString(5, formato.getDate() != null ? formato.getDate().toString() : null);
        ps.setString(6, formato.getGeneralObjetive());
        ps.setString(7, formato.getSpecificObjetives());
        ps.setString(8, formato.getArchivoPDF());
        ps.setString(9, formato.getCartaLaboral());
        ps.setInt(10, formato.getCounter());
        ps.setString(11, formato.getState() != null ? formato.getState().name() : null);
        ps.setString(12, formato.getObservations());
        ps.setInt(13, formato.getId());

        int rows = ps.executeUpdate();
        return rows > 0;
    } catch (SQLException ex) {
        Logger.getLogger(FormatoARepository.class.getName()).log(Level.SEVERE, null, ex);
        return false;
    }
    }




    private FormatoA mapFormatoA(ResultSet rs) throws SQLException {
    FormatoA f = new FormatoA();
    f.setId(rs.getInt("id"));
    f.setTitle(rs.getString("title"));
    f.setMode(enumModalidad.valueOf(rs.getString("mode")));

    // 🔹 Cargar director completo
    int managerId = rs.getInt("projectManager");
    if (managerId > 0) {
        DocenteRepository repoDoc = new DocenteRepository();
        f.setProjectManager(repoDoc.findById(managerId));
    }

    // 🔹 Cargar codirector completo
    int coManagerId = rs.getInt("projectCoManager");
    if (!rs.wasNull() && coManagerId > 0) {
        DocenteRepository repoDoc = new DocenteRepository();
        f.setProjectCoManager(repoDoc.findById(coManagerId));
    }

    f.setDate(rs.getString("date") != null ? LocalDate.parse(rs.getString("date")) : null);
    f.setGeneralObjetive(rs.getString("generalObjetive"));
    f.setSpecificObjetives(rs.getString("specificObjetives"));
    f.setArchivoPDF(rs.getString("archivoPDF"));
    f.setCartaLaboral(rs.getString("cartaLaboral"));
    f.setCounter(rs.getInt("counter"));

    String stateValue = rs.getString("state");
    f.setState(stateValue != null ? enumEstado.valueOf(stateValue) : enumEstado.ENTREGADO);
    f.setObservations(rs.getString("observations"));

    // 🔹 Estudiantes completos
    f.setEstudiantes(loadEstudiantesByFormato(f.getId()));

    f.setVersiones(new ArrayList<>());
    return f;
}

   private List<Estudiante> loadEstudiantesByFormato(int formatoId) throws SQLException {
    List<Estudiante> estudiantes = new ArrayList<>();
    String sql = "SELECT estudiante_id FROM FormatoA_Estudiante WHERE formatoA_id = ?";
    PreparedStatement pstmt = conn.prepareStatement(sql);
    pstmt.setInt(1, formatoId);
    ResultSet rs = pstmt.executeQuery();

    EstudianteRepository repoEst = new EstudianteRepository();

    while (rs.next()) {
        int estId = rs.getInt("estudiante_id");
        Estudiante est = repoEst.findById(estId);
        if (est != null) {
            estudiantes.add(est);
        }
    }
    return estudiantes;
}
    // CREACIÓN DE TABLAS
    private void initDatabase() {
        String sqlFormatoA = "CREATE TABLE IF NOT EXISTS FormatoA (\n"
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "title TEXT NOT NULL,\n"
                + "mode TEXT NOT NULL,\n"
                + "projectManager INTEGER NOT NULL,\n"
                + "projectCoManager INTEGER,\n"
                + "date TEXT,\n"
                + "generalObjetive TEXT,\n"
                + "specificObjetives TEXT,\n"
                + "archivoPDF TEXT NOT NULL,\n"
                + "cartaLaboral TEXT,\n"
                + "counter INTEGER,\n"
                + "state TEXT CHECK(state IN ('RECHAZADO','APROBADO','ENTREGADO')),\n"
                + "observations TEXT,\n"
                + "FOREIGN KEY (projectManager) REFERENCES Docente(idUsuario),\n"
                + "FOREIGN KEY (projectCoManager) REFERENCES Docente(idUsuario)\n"
                + ");";

        String sqlFormatoEst = "CREATE TABLE IF NOT EXISTS FormatoA_Estudiante (\n"
                + "formatoA_id INTEGER NOT NULL,\n"
                + "estudiante_id INTEGER NOT NULL,\n"
                + "PRIMARY KEY (formatoA_id, estudiante_id),\n"
                + "FOREIGN KEY (formatoA_id) REFERENCES FormatoA(id),\n"
                + "FOREIGN KEY (estudiante_id) REFERENCES Estudiante(idUsuario)\n"
                + ");";

        try {
            this.connect();
            Statement stmt = conn.createStatement();
            stmt.execute(sqlFormatoA);
            stmt.execute(sqlFormatoEst);
        } catch (SQLException ex) {
            Logger.getLogger(FormatoARepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public void connect() {
        String url = "jdbc:sqlite:" + System.getProperty("user.dir") + "/BD.db";
        try {
            conn =ConexionSQLite.getConnection();
            System.out.println("Conectado a la BD en archivo");
        } catch (SQLException ex) {
            Logger.getLogger(FormatoAService.class.getName()).log(Level.SEVERE, null, ex);
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
   public boolean updateEstadoObservacionesYContador(int id, String estado, String observaciones, int contador) {
    String sql = "UPDATE formatoA SET state = ?, observations = ?, counter = ? WHERE id = ?";
    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, estado);
        ps.setString(2, observaciones);
        ps.setInt(3, contador);
        ps.setInt(4, id);

        int filas = ps.executeUpdate();
        return filas > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

}

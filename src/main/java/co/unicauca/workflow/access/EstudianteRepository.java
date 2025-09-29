package co.unicauca.workflow.access;

import co.unicauca.workflow.domain.entities.Departamento;
import co.unicauca.workflow.domain.entities.Estudiante;
import co.unicauca.workflow.domain.entities.Facultad;
import co.unicauca.workflow.domain.entities.Programa;
import co.unicauca.workflow.domain.entities.enumRol;
import co.unicauca.workflow.domain.exceptions.ValidationException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.EnumSet;

import java.util.List;

import java.util.logging.Level;
import java.util.logging.Logger;

public class EstudianteRepository implements IEstudianteRepository {

    private Connection conn;

    public EstudianteRepository() {
        initDatabase();
    }

   @Override
public boolean save(Estudiante est) {
    try {
        // 🔹 Validar que la persona ya exista
        PersonaRepository personaRepo = new PersonaRepository();
        personaRepo.conn = this.conn; // reutilizar la misma conexión

        if (!personaRepo.exists(est.getIdUsuario())) {
            System.err.println("La persona con idUsuario=" + est.getIdUsuario() + " no existe en Persona.");
            return false;
        }

        // 🔹 Insertar en Estudiante (datos específicos)
        String sqlEst = "INSERT INTO Estudiante (idUsuario, codPrograma) VALUES (?, ?)";
        try (PreparedStatement psEst = conn.prepareStatement(sqlEst)) {
            psEst.setInt(1, est.getIdUsuario());

            if (est.getProgram() != null) {
                psEst.setInt(2, est.getProgram().getCodPrograma());
            } else {
                psEst.setNull(2, java.sql.Types.INTEGER);
            }

            psEst.executeUpdate();
        }

        return true;

    } catch (SQLException e) {
        Logger.getLogger(EstudianteRepository.class.getName()).log(Level.SEVERE, null, e);
        return false;
    }
}

@Override
public List<Estudiante> list() {
    List<Estudiante> estudiantes = new ArrayList<>();
    try {
        String sql = "SELECT e.idUsuario, p.name, p.lastname, p.phone, p.email, p.password, "
                   + "pr.codPrograma, pr.nombre AS nombrePrograma, "
                   + "d.codDepartamento, d.depNombre AS nombreDepartamento, "
                   + "f.codFacultad, f.nombre AS nombreFacultad "
                   + "FROM Estudiante e "
                   + "JOIN Persona p ON e.idUsuario = p.idUsuario "
                   + "JOIN Programa pr ON e.codPrograma = pr.codPrograma "
                   + "JOIN Departamento d ON pr.codDepartamento = d.codDepartamento "
                   + "JOIN Facultad f ON d.codFacultad = f.codFacultad";

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            // Facultad
            Facultad facultad = new Facultad(rs.getString("nombreFacultad"));
            facultad.setCodFacultad(rs.getInt("codFacultad"));

            // Departamento
            Departamento departamento = new Departamento(rs.getString("nombreDepartamento"), facultad);
            departamento.setCodDepartamento(rs.getInt("codDepartamento"));

            // Programa (usando el constructor completo)
            Programa programa = new Programa(
                rs.getInt("codPrograma"),
                rs.getString("nombrePrograma"),
                departamento
            );

            // Estudiante
            Estudiante est = new Estudiante(
                    rs.getInt("idUsuario"),
                    programa,
                    rs.getString("name"),
                    rs.getString("lastname"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("password")
            );

            estudiantes.add(est);
        }

    } catch (SQLException | ValidationException ex) {
        Logger.getLogger(EstudianteRepository.class.getName()).log(Level.SEVERE, null, ex);
    }
    return estudiantes;
}



   private void initDatabase() {
    String sql = "CREATE TABLE IF NOT EXISTS Estudiante ("
               + "idUsuario INTEGER PRIMARY KEY, "
               + "codPrograma INTEGER NOT NULL, "
               + "FOREIGN KEY (idUsuario) REFERENCES Persona(idUsuario), "
               + "FOREIGN KEY (codPrograma) REFERENCES Programa(codPrograma)"
               + ");";

    try {
        this.connect();
        Statement stmt = conn.createStatement();
        stmt.execute(sql);

    } catch (SQLException ex) {
        Logger.getLogger(EstudianteRepository.class.getName()).log(Level.SEVERE, null, ex);
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
                System.out.println("Conectado a la BD en archivo (Estudiante)");
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
     public Estudiante findById(int id) {
        String sql = "SELECT p.idUsuario, p.name, p.lastname, p.phone, p.email, p.password, " +
                     "e.codPrograma, prog.nombrePrograma " +
                     "FROM Estudiante e " +
                     "INNER JOIN Persona p ON e.idUsuario = p.idUsuario " +
                     "LEFT JOIN Programa prog ON e.codPrograma = prog.codPrograma " +
                     "WHERE e.idUsuario = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Programa programa = null;
                if (rs.getInt("codPrograma") > 0) {
                    programa = new Programa(
                        rs.getInt("codPrograma"),
                        rs.getString("nombrePrograma"),
                        null // puedes asociar el departamento después si quieres
                    );
                }

                return new Estudiante(
                    rs.getInt("idUsuario"),
                    programa,
                    rs.getString("name"),
                    rs.getString("lastname"),
                    rs.getString("phone"),
                    rs.getString("email"),
                    rs.getString("password")
                );
            }
        } catch (Exception e) {
            System.out.println("Error cargando Estudiante: " + e.getMessage());
        }
        return null;
    }
}
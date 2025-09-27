package co.unicauca.workflow.access;

import co.unicauca.workflow.domain.entities.Departamento;
import co.unicauca.workflow.domain.entities.Docente;
import co.unicauca.workflow.domain.entities.Facultad;
import co.unicauca.workflow.domain.exceptions.ValidationException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class DocenteRepository implements IDocenteRepository {

    private Connection conn;

    public DocenteRepository() {
        initDatabase();
    }

    @Override
    public boolean save(Docente docente) {
        try {
            // 🔹 Validar que la persona ya exista
            PersonaRepository personaRepo = new PersonaRepository();
            personaRepo.conn = this.conn; // reutilizar la misma conexión

            if (!personaRepo.exists(docente.getIdUsuario())) {
                System.err.println("La persona con idUsuario=" + docente.getIdUsuario() + " no existe en Persona.");
                return false;
            }

            // 🔹 Insertar en Docente (datos específicos)
            String sqlDoc = "INSERT INTO Docente (idUsuario, codDepartamento) VALUES (?, ?)";
            try (PreparedStatement psDoc = conn.prepareStatement(sqlDoc)) {
                psDoc.setInt(1, docente.getIdUsuario());

                if (docente.getDepartamento() != null) {
                    psDoc.setInt(2, docente.getDepartamento().getCodDepartamento());
                } else {
                    psDoc.setNull(2, java.sql.Types.INTEGER);
                }

                psDoc.executeUpdate();
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
        try {
            String sql = "SELECT d.idUsuario, p.name, p.lastname, p.phone, p.email, p.password, "
                       + "dep.codDepartamento, dep.depNombre AS nombreDepartamento, "
                       + "f.codFacultad, f.nombre AS nombreFacultad "
                       + "FROM Docente d "
                       + "JOIN Persona p ON d.idUsuario = p.idUsuario "
                       + "JOIN Departamento dep ON d.codDepartamento = dep.codDepartamento "
                       + "JOIN Facultad f ON dep.codFacultad = f.codFacultad";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                // Facultad
                Facultad facultad = new Facultad(rs.getString("nombreFacultad"));
                facultad.setCodFacultad(rs.getInt("codFacultad"));

                // Departamento
                Departamento departamento = new Departamento(rs.getString("nombreDepartamento"), facultad);
                departamento.setCodDepartamento(rs.getInt("codDepartamento"));

                // Docente (usa idUsuario heredado de Persona)
                Docente docente = new Docente(
                        rs.getInt("idUsuario"),
                        departamento,
                        rs.getString("name"),
                        rs.getString("lastname"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("password")
                );

                docentes.add(docente);
            }

        } catch (SQLException | ValidationException ex) {
            Logger.getLogger(DocenteRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return docentes;
    }

    private void initDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS Docente ("
                   + "idUsuario INTEGER PRIMARY KEY, "
                   + "codDepartamento INTEGER NOT NULL, "
                   + "FOREIGN KEY (idUsuario) REFERENCES Persona(idUsuario), "
                   + "FOREIGN KEY (codDepartamento) REFERENCES Departamento(codDepartamento)"
                   + ");";

        try {
            this.connect();
            Statement stmt = conn.createStatement();
            stmt.execute(sql);

        } catch (SQLException ex) {
            Logger.getLogger(DocenteRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void connect() {
        String url = "jdbc:sqlite:" + System.getProperty("user.dir") + "/BD.db";
        try {
            conn = ConexionSQLite.getConnection();
            System.out.println("Conectado a la BD en archivo");
        } catch (SQLException ex) {
            Logger.getLogger(DocenteRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void disconnect() {
        try {
            if (conn != null) conn.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public Connection getConnection() {
        return conn;
    }
    
 public Docente findById(int id) {
    String sql = "SELECT p.idUsuario, p.name, p.lastname, p.phone, p.email, p.password, " +
                 "d.codDepartamento, dep.depNombre, " +
                 "f.codFacultad, f.nombre " +
                 "FROM Docente d " +
                 "INNER JOIN Persona p ON d.idUsuario = p.idUsuario " +
                 "LEFT JOIN Departamento dep ON d.codDepartamento = dep.codDepartamento " +
                 "LEFT JOIN Facultad f ON dep.codFacultad = f.codFacultad " +
                 "WHERE d.idUsuario = ?";

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            // Facultad
            Facultad facultad = null;
            if (rs.getInt("codFacultad") > 0) {
                facultad = new Facultad(rs.getString("facNombre"));
                facultad.setCodFacultad(rs.getInt("codFacultad"));
            }

            // Departamento
            Departamento departamento = null;
            if (rs.getInt("codDepartamento") > 0) {
                departamento = new Departamento(
                    rs.getString("depNombre"),
                    facultad
                );
                departamento.setCodDepartamento(rs.getInt("codDepartamento"));
            }

            // Docente
            Docente docente = new Docente(
                rs.getInt("idUsuario"),
                departamento,
                rs.getString("name"),
                rs.getString("lastname"),
                rs.getString("phone"),
                rs.getString("email"),
                rs.getString("password")
            );

            // 👀 imprimir para verificar
            System.out.println("Docente encontrado:");
            System.out.println("ID: " + docente.getIdUsuario());
            System.out.println("Nombre: " + docente.getName() + " " + docente.getLastname());
            System.out.println("Teléfono: " + docente.getPhone());
            System.out.println("Email: " + docente.getEmail());
            System.out.println("Departamento: " +
                (departamento != null ? departamento.getNombre() : "Sin departamento"));
            System.out.println("Facultad: " +
                (facultad != null ? facultad.getNombre() : "Sin facultad"));

            return docente;
        }
    } catch (Exception e) {
        System.out.println("Error cargando Docente: " + e.getMessage());
    }
    return null;
}
}

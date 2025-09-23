package co.unicauca.workflow.access;

import co.unicauca.workflow.domain.entities.Coordinador;
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
 * @author User
 */
public class CoordinadorRepository implements ICoordinadorRepository {

    private Connection conn;

    public CoordinadorRepository() {
        initDatabase();
    }

    @Override
    public boolean save(Coordinador coord) {
        try {
            // 🔹 Validar que la persona ya exista
            PersonaRepository personaRepo = new PersonaRepository();
            personaRepo.conn = this.conn; // reutilizar la misma conexión

            if (!personaRepo.exists(coord.getIdUsuario())) {
                System.err.println("La persona con idUsuario=" + coord.getIdUsuario() + " no existe en Persona.");
                return false;
            }

            // 🔹 Insertar en Coordinador (datos específicos)
            String sqlCoord = "INSERT INTO Coordinador (idUsuario, codDepartamento) VALUES (?, ?)";
            try (PreparedStatement psCoord = conn.prepareStatement(sqlCoord)) {
                psCoord.setInt(1, coord.getIdUsuario());

                if (coord.getDepartamento() != null) {
                    psCoord.setInt(2, coord.getDepartamento().getCodDepartamento());
                } else {
                    psCoord.setNull(2, java.sql.Types.INTEGER);
                }

                psCoord.executeUpdate();
            }

            return true;

        } catch (SQLException e) {
            Logger.getLogger(CoordinadorRepository.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }
    }

    @Override
    public List<Coordinador> list() {
        List<Coordinador> coordinadores = new ArrayList<>();
        try {
            String sql = "SELECT c.idUsuario, p.name, p.lastname, p.phone, p.email, p.password, "
                       + "d.codDepartamento, d.nombre AS nombreDepartamento, "
                       + "f.codFacultad, f.nombre AS nombreFacultad "
                       + "FROM Coordinador c "
                       + "JOIN Persona p ON c.idUsuario = p.idUsuario "
                       + "JOIN Departamento d ON c.codDepartamento = d.codDepartamento "
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

                // Coordinador (usa idUsuario heredado de Persona)
                Coordinador coord = new Coordinador(
                        rs.getInt("idUsuario"),
                        departamento,
                        rs.getString("name"),
                        rs.getString("lastname"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("password")
                );

                coordinadores.add(coord);
            }

        } catch (SQLException | ValidationException ex) {
            Logger.getLogger(CoordinadorRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
        return coordinadores;
    }

    private void initDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS Coordinador ("
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
            Logger.getLogger(CoordinadorRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void connect() {
        String url = "jdbc:sqlite:" + System.getProperty("user.dir") + "/BD.db";
        try {
            conn = DriverManager.getConnection(url);
            System.out.println("Conectado a la BD en archivo");
        } catch (SQLException ex) {
            Logger.getLogger(CoordinadorRepository.class.getName()).log(Level.SEVERE, null, ex);
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
}

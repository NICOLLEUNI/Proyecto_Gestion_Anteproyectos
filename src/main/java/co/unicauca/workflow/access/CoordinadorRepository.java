/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.List;

/**
 *
 * @author User
 */
public class CoordinadorRepository  implements ICoordinadorRepository {
    
    private Connection conn;

    public CoordinadorRepository() {
        initDatabase();
    }
    
  @Override
public boolean save(Coordinador coord) {
    try {
        // 1. Guardar como Persona
        PersonaRepository personaRepo = new PersonaRepository();
        personaRepo.conn = this.conn; // 👈 reusar conexión
        boolean savedPersona = personaRepo.save(coord);

        if (!savedPersona) {
            return false;
        }

        // 2. Insertar en Coordinador
        String sqlCoord = "INSERT INTO Coordinador (idUsuario, codDepartamento) VALUES (?, ?)";
        try (PreparedStatement psCoord = conn.prepareStatement(sqlCoord, Statement.RETURN_GENERATED_KEYS)) {
            psCoord.setInt(1, coord.getIdUsuario());

            if (coord.getDepartamento() != null) {
                psCoord.setInt(2, coord.getDepartamento().getCodDepartamento());
            } else {
                psCoord.setNull(2, java.sql.Types.INTEGER);
            }

            psCoord.executeUpdate();

            // Recuperar código generado
            ResultSet generatedKeys = psCoord.getGeneratedKeys();
            if (generatedKeys.next()) {
                coord.setCodigoCoordinador(generatedKeys.getInt(1));
            }
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
        String sql = "SELECT c.codCoordinador, u.name, u.lastname, u.phone, u.email, u.password, "
                   + "d.codDepartamento, d.nombre AS nombreDepartamento, "
                   + "f.codFacultad, f.nombre AS nombreFacultad "
                   + "FROM Coordinador c "
                   + "JOIN User u ON c.idUsuario = u.idUsuario "
                   + "JOIN Departamento d ON c.codDepartamento = d.codDepartamento "
                   + "JOIN Facultad f ON d.codFacultad = f.codFacultad";

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while (rs.next()) {
            // Crear Facultad
            Facultad facultad = new Facultad(rs.getString("nombreFacultad"));
            facultad.setCodFacultad(rs.getInt("codFacultad"));

            // Crear Departamento con su facultad
            Departamento departamento = new Departamento(rs.getString("nombreDepartamento"), facultad);
            departamento.setCodDepartamento(rs.getInt("codDepartamento"));

            // Crear Coordinador con su departamento
            Coordinador coord = new Coordinador(
                rs.getInt("codCoordinador"),
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
        Logger.getLogger(Coordinador.class.getName()).log(Level.SEVERE, null, ex);
    }
    return coordinadores;
}

    
 private void initDatabase() {
    // SQL statement for creating the Coordinador table
    String sql = "CREATE TABLE IF NOT EXISTS Coordinador (\n"
            + "   codCoordinador INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n"
            + "   idUsuario INTEGER NOT NULL,\n"
            + "   codDepartamento INTEGER NOT NULL,\n"
            + "   FOREIGN KEY (idUsuario) REFERENCES User(idUsuario),\n"
            + "   FOREIGN KEY (codDepartamento) REFERENCES Departamento(codDepartamento)\n"
            + ");";

    try {
        this.connect();
        Statement stmt = conn.createStatement();
        stmt.execute(sql);
        //this.disconnect();

    } catch (SQLException ex) {
        Logger.getLogger(CoordinadorRepository.class.getName()).log(Level.SEVERE, null, ex);
    }
}
 public void connect() {
        // SQLite connection string

        String url = "jdbc:sqlite:"+  System.getProperty("user.dir")  +"/BD.db";
      
        try {
            conn = DriverManager.getConnection(url);
             System.out.println("Conectado a la BD en archivo");

        } catch (SQLException ex) {
            Logger.getLogger(CoordinadorRepository.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void disconnect() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }
     public Connection getConnection() {
        return conn;
    }   

}
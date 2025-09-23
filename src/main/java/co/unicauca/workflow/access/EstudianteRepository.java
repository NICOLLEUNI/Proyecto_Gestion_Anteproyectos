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
        if (est.getName() == null || est.getName().trim().isEmpty()) return false;
        if (est.getLastname() == null || est.getLastname().trim().isEmpty()) return false;
        if (est.getEmail() == null || est.getEmail().trim().isEmpty()) return false;
        if (est.getPassword() == null || est.getPassword().trim().isEmpty()) return false;
        if (est.getProgram() == null) return false;

        String sqlPersona = "INSERT INTO Persona (name, lastname, phone, email, password) VALUES (?, ?, ?, ?, ?)";
        String sqlEstudiante = "INSERT INTO Estudiante (idUsuario, CodPrograma) VALUES (?, ?)";

        try (PreparedStatement pstmt1 = conn.prepareStatement(sqlPersona, Statement.RETURN_GENERATED_KEYS)) {
            pstmt1.setString(1, est.getName());
            pstmt1.setString(2, est.getLastname());
            pstmt1.setString(3, est.getPhone());
            pstmt1.setString(4, est.getEmail());
            pstmt1.setString(5, est.getPassword());

            int affected = pstmt1.executeUpdate();
            if (affected == 0) return false;

            try (ResultSet rs = pstmt1.getGeneratedKeys()) {
                if (rs.next()) {
                    est.setIdUsuario(rs.getInt(1));
                }
            }

            try (PreparedStatement pstmt2 = conn.prepareStatement(sqlEstudiante, Statement.RETURN_GENERATED_KEYS)) {
                pstmt2.setInt(1, est.getIdUsuario());
                pstmt2.setInt(2, est.getProgram().getCodPrograma());
                pstmt2.executeUpdate();

                try (ResultSet rs = pstmt2.getGeneratedKeys()) {
                    if (rs.next()) {
                        est.setIdEstudiante(rs.getInt(1));
                    }
                }
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

    String sql = "SELECT " +
                "e.idEstudiante,"+
                "e.idUsuario,"+
                 "p.name, p.lastname, p.phone, p.email, p.password, " +
                 "pr.codPrograma, pr.nombre AS nombrePrograma, " +
                 "d.idDepartamento, d.nombre AS nombreDepartamento, " +
                 "f.idFacultad, f.nombre AS nombreFacultad " +
                 "FROM Estudiante e " +
                 "JOIN Persona p ON e.idUsuario = p.idUsuario " +
                 "JOIN Programa pr ON e.CodPrograma = pr.CodPrograma " +
                 "JOIN Departamento d ON pr.idDepartamento = d.idDepartamento " +
                 "JOIN Facultad f ON d.idFacultad = f.idFacultad";

    try (Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        while (rs.next()) {
            Facultad fac = new Facultad(
                rs.getString("nombreFacultad")
            );

            Departamento depto = new Departamento(
                rs.getString("nombreDepartamento"),
                fac
            );

            Programa prog = new Programa(
                rs.getInt("codPrograma"),
                rs.getString("nombrePrograma"),
                depto
            );

            Estudiante est = new Estudiante(
                
                rs.getInt("idEstudiante"),
                prog,
                rs.getInt("idUsuario"),   
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
        String sqlPersona = "CREATE TABLE IF NOT EXISTS Persona ("
                + "idUsuario INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + "name TEXT NOT NULL, "
                + "lastname TEXT NOT NULL, "
                + "phone TEXT, "
                + "email TEXT NOT NULL UNIQUE, "
                + "password TEXT NOT NULL"
                + ");";

        String sqlFacultad = "CREATE TABLE IF NOT EXISTS Facultad ("
                + "idFacultad INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + "nombre TEXT NOT NULL UNIQUE"
                + ");";

        String sqlDepartamento = "CREATE TABLE IF NOT EXISTS Departamento ("
                + "idDepartamento INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + "nombre TEXT NOT NULL, "
                + "idFacultad INTEGER NOT NULL, "
                + "FOREIGN KEY (idFacultad) REFERENCES Facultad(idFacultad)"
                + ");";

        String sqlPrograma = "CREATE TABLE IF NOT EXISTS Programa ("
                + "CodPrograma INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + "nombre TEXT NOT NULL, "
                + "idDepartamento INTEGER NOT NULL, "
                + "FOREIGN KEY (idDepartamento) REFERENCES Departamento(idDepartamento)"
                + ");";

        String sqlEstudiante = "CREATE TABLE IF NOT EXISTS Estudiante ("
                + "idEstudiante INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + "idUsuario INTEGER NOT NULL,"
                + "CodPrograma INTEGER NOT NULL, "
                + "FOREIGN KEY (idUsuario) REFERENCES Persona(idUsuario), "
                + "FOREIGN KEY (CodPrograma) REFERENCES Programa(CodPrograma)"
                + ");";

        try {
            this.connect();
            Statement stmt = conn.createStatement();
            stmt.execute(sqlPersona);
            stmt.execute(sqlFacultad);
            stmt.execute(sqlDepartamento);
            stmt.execute(sqlPrograma);
            stmt.execute(sqlEstudiante);
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
}

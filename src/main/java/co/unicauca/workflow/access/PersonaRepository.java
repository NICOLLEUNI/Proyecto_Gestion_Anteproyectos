/*
* Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
* Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
*/
package co.unicauca.workflow.access;

import co.unicauca.workflow.domain.entities.Persona;
import co.unicauca.workflow.domain.entities.enumRol;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
*
* @author User
*/
public class PersonaRepository implements IPersonaRepository{
	
   

	public Connection conn;
	
	public PersonaRepository() {
		initDatabase();
	}
	
	@Override
		public boolean save(Persona persona) {
		// Validar antes de guardar
		if (persona.getName() == null || persona.getName().trim().isEmpty()) return false;
		if (persona.getLastname() == null || persona.getLastname().trim().isEmpty()) return false;
		if (persona.getEmail() == null || persona.getEmail().trim().isEmpty()) return false;
		if (persona.getPassword() == null || persona.getPassword().trim().isEmpty()) return false;
		String sql = "INSERT INTO Persona (name, lastname, phone, email, password) VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			pstmt.setString(1, persona.getName());
			pstmt.setString(2, persona.getLastname());
			pstmt.setString(3, persona.getPhone());
			pstmt.setString(4, persona.getEmail());
			pstmt.setString(5, persona.getPassword());
			int affected = pstmt.executeUpdate();
			
			if (affected == 0) {
				return false;
			}
			
			try (ResultSet rs = pstmt.getGeneratedKeys()) {
				if (rs.next()) {
					persona.setIdUsuario(rs.getInt(1));
				}
			}
			
			// Guardar roles si existen
			if (persona.getRoles() != null) {
				for (enumRol rol : persona.getRoles()) {
					String sqlRol = "INSERT INTO PersonaRol (idUsuario, idRol) " +
						"SELECT ?, idRol FROM Rol WHERE nombre = ?";
					try (PreparedStatement psRol = conn.prepareStatement(sqlRol)) {
						psRol.setInt(1, persona.getIdUsuario());
						psRol.setString(2, rol.name());
						psRol.executeUpdate();
					}
				}
			}
			return true;
			
		} catch (SQLException e) {
			    e.printStackTrace();
			return false;
		}
	}
		@Override
			public List<Persona> list() {
				List<Persona> personas = new ArrayList<>();
				
				try {
					// Traer personas con un JOIN a sus roles
					String sql = "SELECT p.idUsuario, p.name, p.lastname, p.phone, p.email, p.password, r.nombre AS rol " +
						"FROM Persona p " +
						"LEFT JOIN PersonaRol pr ON p.idUsuario = pr.idUsuario " +
						"LEFT JOIN Rol r ON pr.idRol = r.idRol";
					
					Statement stmt = conn.createStatement();
					ResultSet rs = stmt.executeQuery(sql);
					
					// Mapa temporal para no duplicar personas
					Map<Integer, Persona> mapPersonas = new HashMap<>();
					
					while (rs.next()) {
						int idUsuario = rs.getInt("idUsuario");
						
						// Si la persona aÃºn no estÃ¡ en el mapa, la creamos
						Persona persona = mapPersonas.get(idUsuario);
						if (persona == null) {
							persona = new Persona(
												  idUsuario,
												  rs.getString("name"),
												  rs.getString("lastname"),
												  rs.getString("phone"),
												  rs.getString("email"),
												  rs.getString("password")
												  );
							persona.setRoles(EnumSet.noneOf(enumRol.class)); // inicializamos vacÃ­o
							mapPersonas.put(idUsuario, persona);
						}
						
						// Agregar rol si existe
						String rolNombre = rs.getString("rol");
						if (rolNombre != null) {
							try {
								enumRol rol = enumRol.valueOf(rolNombre);
								persona.getRoles().add(rol);
							} catch (IllegalArgumentException e) {
								System.err.println("Rol no reconocido en enum: " + rolNombre);
							}
						}
					}
					
					personas.addAll(mapPersonas.values());
					
				} catch (SQLException ex) {
					Logger.getLogger(PersonaRepository.class.getName()).log(Level.SEVERE, null, ex);
				}
				
				return personas;
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
				
				String sqlRol = "CREATE TABLE IF NOT EXISTS Rol ("
					+ "idRol INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
					+ "nombre TEXT NOT NULL UNIQUE"
					+ ");";
				
				String sqlPersonaRol = "CREATE TABLE IF NOT EXISTS PersonaRol ("
					+ "idUsuario INTEGER NOT NULL, "
					+ "idRol INTEGER NOT NULL, "
					+ "FOREIGN KEY (idUsuario) REFERENCES Persona(idUsuario), "
					+ "FOREIGN KEY (idRol) REFERENCES Rol(idRol), "
					+ "PRIMARY KEY (idUsuario, idRol)"
					+ ");";
				
				try {
					this.connect();
					Statement stmt = conn.createStatement();
					stmt.execute(sqlPersona);
					stmt.execute(sqlRol);
					stmt.execute(sqlPersonaRol);
					
					// ðŸ”¹ Insertar roles por defecto si la tabla estÃ¡ vacÃ­a
					String checkRoles = "SELECT COUNT(*) AS total FROM Rol";
					ResultSet rs = stmt.executeQuery(checkRoles);
					if (rs.next() && rs.getInt("total") == 0) {
						stmt.execute("INSERT INTO Rol (nombre) VALUES ('ESTUDIANTE')");
						stmt.execute("INSERT INTO Rol (nombre) VALUES ('DOCENTE')");
						stmt.execute("INSERT INTO Rol (nombre) VALUES ('COORDINADOR')");
					}
					
				} catch (SQLException ex) {
					Logger.getLogger(PersonaRepository.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
			public boolean exists(int idUsuario) {
				String sql = "SELECT 1 FROM Persona WHERE idUsuario = ?";
				try (PreparedStatement ps = conn.prepareStatement(sql)) {
					ps.setInt(1, idUsuario);
					ResultSet rs = ps.executeQuery();
					return rs.next();
				} catch (SQLException e) {
					return false;
				}
			}
			public void connect() {
				try {
					if (conn == null || conn.isClosed()) {
						String url = "jdbc:sqlite:" + System.getProperty("user.dir") + "/BD.db";
						conn = ConexionSQLite.getConnection();
						try (Statement stmt = conn.createStatement()) {
							stmt.execute("PRAGMA busy_timeout = 5000"); // espera hasta 5 seg
						}
						System.out.println("Conectado a la BD en archivo");
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
						conn = null; // ðŸ‘ˆ IMPORTANTE
					}
				} catch (SQLException ex) {
					System.out.println(ex.getMessage());
				}
			}
}


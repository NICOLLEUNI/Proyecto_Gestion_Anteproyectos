
package co.unicauca.workflow.access;

import co.unicauca.workflow.domain.entities.User;



import co.unicauca.workflow.domain.entities.enumProgram;
import co.unicauca.workflow.domain.entities.enumRol;
import co.unicauca.workflow.domain.service.UserService;
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


public class UserRepository implements IUserRepository {
  
  

    private Connection conn;

    public UserRepository() {
        initDatabase();
    }

    @Override
    public boolean save( User newUser) {

        try {
            //Validate User
            if (newUser == null ||  newUser.getName().isBlank()) {
                return false;
            }
            //this.connect();
            String sql = "INSERT INTO User (name, lastname, phone, email, password, rol, program) "
           + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            // OJO: aquí pedimos que devuelva las llaves generadas
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, newUser.getName());
            pstmt.setString(2, newUser.getLastname());
            pstmt.setInt(3, newUser.getPhone());
            pstmt.setString(4, newUser.getEmail());
            pstmt.setString(5, newUser.getPassword());
            pstmt.setString(6, newUser.getRol().name());
            pstmt.setString(7, newUser.getProgram().name());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                // Recuperar el id generado
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    int idGenerado = rs.getInt(1);  // primera columna del ResultSet
                    newUser.setIdUsuario(idGenerado); // guardamos el id en el objeto
                    System.out.println("Nuevo usuario creado con id: " + idGenerado);
                }
            }

            //this.disconnect();
            return true;
            
       } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    @Override
    public List<User> list() {
        List<User> users = new ArrayList<>();
        try {

           String sql = "SELECT idUsuario, name, lastname, phone, email, password, rol, program FROM User";
            //this.connect();

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                User newUser = new User();
                newUser.setIdUsuario(rs.getInt("idUsuario"));
                newUser.setName(rs.getString("name"));
                newUser.setLastname(rs.getString("lastname"));
                newUser.setPhone(rs.getInt("phone"));
                newUser.setEmail(rs.getString("email"));
                newUser.setPassword(rs.getString("password"));
                newUser.setRol(enumRol.valueOf(rs.getString("rol"))); 
                newUser.setProgram(enumProgram.valueOf(rs.getString("program")));
                
                users.add(newUser);
              
            }
            //this.disconnect();

        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return users;
    }

    private void initDatabase() {
        // SQL statement for creating a new table
       String sql = "CREATE TABLE IF NOT EXISTS User (\n"
               + "   idUsuario INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n"
               + "   name TEXT NOT NULL,\n"
               + "   lastname TEXT NOT NULL,\n"
               + "   phone INT,\n"
               + "   email TEXT NOT NULL UNIQUE,\n"
               + "   password TEXT NOT NULL,\n"
               + "   rol TEXT NOT NULL,\n"
               + "   program TEXT NOT NULL\n"
               + ");";

        try {
            this.connect();
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            //this.disconnect();

        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void connect() {
        // SQLite connection string
  
        String url = "jdbc:sqlite:C:/Users/User/Desktop/Taller02/Taller02_SOLID/BD.db";
      
        try {
            conn = DriverManager.getConnection(url);
             System.out.println("Conectado a la BD en archivo");

        } catch (SQLException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
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

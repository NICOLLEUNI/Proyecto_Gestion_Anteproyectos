
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

   /**
     * @brief Guarda un nuevo usuario en la base de datos.
     *
     * Este método valida los datos del objeto {@link User} recibido y,
     * si son correctos, los inserta en la tabla "User". 
     * Además, recupera y asigna el ID autogenerado por la base de datos al objeto.
     *
     * @param newUser Usuario a registrar en la base de datos.
     *                - Debe contener nombre, apellido, email, contraseña, rol y programa válidos.
     *                - El teléfono puede ser nulo o vacío.
     *
     * @return true si el usuario fue guardado exitosamente en la BD, false en caso de error
     *         (por datos inválidos o fallo en la operación SQL).
     *
     * @throws RuntimeException si ocurre un error grave en la conexión o ejecución de la consulta.
     */
   @Override
    public boolean save(User newUser) {
    try {
        // Validar User
        if (newUser == null || newUser.getName().isBlank()) {
            return false;
        }

        String sql = "INSERT INTO User (name, lastname, phone, email, password, rol, program) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        // OJO: aquí pedimos que devuelva las llaves generadas
        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

        pstmt.setString(1, newUser.getName());
        pstmt.setString(2, newUser.getLastname());
        //Verifica 
        if (newUser.getPhone() == null || newUser.getPhone().isBlank()) {
            pstmt.setNull(3, java.sql.Types.VARCHAR);
        } else {
            pstmt.setString(3, newUser.getPhone());
        }


        pstmt.setString(4, newUser.getEmail());
        pstmt.setString(5, newUser.getPassword());
        pstmt.setString(6, newUser.getRol().name());
        pstmt.setString(7, newUser.getProgram().name());

        int rowsAffected = pstmt.executeUpdate();

        if (rowsAffected > 0) {
            // Recuperar el id generado
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int idGenerado = rs.getInt(1);
                newUser.setIdUsuario(idGenerado);
                System.out.println("Nuevo usuario creado con id: " + idGenerado);
            }
        }

        return true;

    } catch (SQLException ex) {
        Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
    }
    return false;
}
/**
     * @brief Recupera todos los usuarios de la base de datos.
     *
     * Este método ejecuta una consulta SQL sobre la tabla "User" y construye 
     * una lista de objetos {@link User} con los datos obtenidos. 
     * Se encarga de mapear cada columna (idUsuario, name, lastname, phone, 
     * email, password, rol, program) a los atributos de la entidad.
     *
     * @return Lista de objetos {@link User} con todos los usuarios almacenados 
     *         en la base de datos. Si no hay registros, retorna una lista vacía.
     *
     * @note El campo <code>phone</code> puede ser <code>null</code> si en la base 
     *       de datos el valor está vacío o definido como NULL.
     *
     * @throws RuntimeException si ocurre un error de conexión o en la ejecución 
     *         de la consulta SQL.
     */
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
              // Manejar phone como nullable
            String ph = rs.getString("phone");
            newUser.setPhone(ph); // puede ser null si en la BD está vacío

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
               + "   phone TEXT,\n"
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

        String url = "jdbc:sqlite:./BD.db";
      
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

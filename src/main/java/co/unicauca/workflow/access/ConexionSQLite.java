/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.unicauca.workflow.access;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author User
 */
public class ConexionSQLite {
    private static Connection conn;

    public static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            // Agregamos busy_timeout para esperar hasta 5 segundos antes de "locked"
            String url = "jdbc:sqlite:" + System.getProperty("user.dir") + "/BD.db?busy_timeout=5000";
            conn = DriverManager.getConnection(url);
        }
        return conn;
    }

    public static void closeConnection() {
        if (conn != null) {
            try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
            conn = null;
        }
    }
}
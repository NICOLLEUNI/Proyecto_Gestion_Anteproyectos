/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package co.unicauca.workflow.access;

import co.unicauca.workflow.domain.entities.Facultad;
import java.util.List;

/**
 * Interfaz que define las operaciones CRUD para la entidad Facultad
 * 
 * @author USUARIO
 */
public interface IFacultadRepository {
    
    /**
     * Guarda una nueva facultad en la base de datos
     * 
     * @param facultad La facultad a guardar
     * @return true si se guardó correctamente, false en caso contrario
     */
    boolean save(Facultad facultad);
    /**
     * Obtiene todas las facultades registradas en el sistema
     * 
     * @return Lista de facultades
     */
    List<Facultad> list();
    /**
     * Conecta a la base de datos
     */
    void connect();
    /**
     * Desconecta de la base de datos
     */
    void disconnect();
}


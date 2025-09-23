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
     * Busca una facultad por su código único
     * 
     * @param codFacultad El código de la facultad a buscar
     * @return La facultad encontrada o null si no existe
     */
    Facultad findById(int codFacultad);
    
    /**
     * Busca una facultad por su nombre
     * 
     * @param nombre El nombre de la facultad a buscar
     * @return La facultad encontrada o null si no existe
     */
    Facultad findByName(String nombre);
    
    /**
     * Obtiene todas las facultades registradas en el sistema
     * 
     * @return Lista de facultades
     */
    List<Facultad> list();
    
    /**
     * Actualiza una facultad existente en la base de datos
     * 
     * @param facultad La facultad con los datos actualizados
     * @return true si se actualizó correctamente, false en caso contrario
     */
    boolean update(Facultad facultad);
    
    /**
     * Elimina una facultad de la base de datos
     * 
     * @param codFacultad El código de la facultad a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    boolean delete(int codFacultad);

    /**
     * Conecta a la base de datos
     */
    void connect();

    /**
     * Desconecta de la base de datos
     */
    void disconnect();
}


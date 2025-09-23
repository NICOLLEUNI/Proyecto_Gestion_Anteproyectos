/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package co.unicauca.workflow.access;

import co.unicauca.workflow.domain.entities.Programa;
import co.unicauca.workflow.domain.entities.enumProgram;
import java.util.List;

/**
 * Interfaz que define las operaciones CRUD para la entidad Programa
 * 
 * @author USUARIO
 */
public interface IProgramaRepository {
    
    /**
     * Guarda un nuevo programa en la base de datos
     * 
     * @param programa El programa a guardar
     * @return true si se guardó correctamente, false en caso contrario
     */
    boolean save(Programa programa);
    
    /**
     * Busca un programa por su código único
     * 
     * @param codPrograma El código del programa a buscar
     * @return El programa encontrado o null si no existe
     */
    Programa findById(int codPrograma);
    
    /**
     * Busca programas por el tipo de programa (enum)
     * 
     * @param programa El tipo de programa a buscar
     * @return Lista de programas que coinciden con el tipo
     */
    List<Programa> findByPrograma(enumProgram programa);
    
    /**
     * Busca programas por el nombre (descripción del enum)
     * 
     * @param nombrePrograma El nombre del programa a buscar
     * @return Lista de programas que coinciden con el nombre
     */
    List<Programa> findByNombre(String nombrePrograma);
    
    /**
     * Busca programas por departamento
     * 
     * @param codDepartamento El código del departamento
     * @return Lista de programas del departamento
     */
    List<Programa> findByDepartamento(int codDepartamento);
    
    /**
     * Busca programas por facultad
     * 
     * @param codFacultad El código de la facultad
     * @return Lista de programas de la facultad
     */
    List<Programa> findByFacultad(int codFacultad);
    
    /**
     * Obtiene todos los programas registrados en el sistema
     * 
     * @return Lista de programas
     */
    List<Programa> list();
    
    /**
     * Actualiza un programa existente en la base de datos
     * 
     * @param programa El programa con los datos actualizados
     * @return true si se actualizó correctamente, false en caso contrario
     */
    boolean update(Programa programa);
    
    /**
     * Elimina un programa de la base de datos
     * 
     * @param codPrograma El código del programa a eliminar
     * @return true si se eliminó correctamente, false en caso contrario
     */
    boolean delete(int codPrograma);
    
    /**
     * Verifica si existe un programa con el mismo nombre en la misma facultad
     * 
     * @param nombrePrograma Nombre del programa
     * @param codFacultad Código de la facultad
     * @return true si ya existe, false en caso contrario
     */
    boolean existsByNombreAndFacultad(String nombrePrograma, int codFacultad);
}

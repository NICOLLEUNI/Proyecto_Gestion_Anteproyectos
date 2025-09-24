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
     * Obtiene todos los programas registrados en el sistema
     * 
     * @return Lista de programas
     */
    List<Programa> list();
    
    
    
    
}

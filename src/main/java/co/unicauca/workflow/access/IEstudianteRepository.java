/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package co.unicauca.workflow.access;

import java.util.List;
import co.unicauca.workflow.domain.entities.Estudiante;

/**
 *
 * @author CRISTHIAN TORRES
 */
public interface IEstudianteRepository {
    
    boolean save(Estudiante newEstudiante);
    List<Estudiante> list();
}

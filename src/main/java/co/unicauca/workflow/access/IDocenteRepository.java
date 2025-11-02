/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package co.unicauca.workflow.access;
import java.util.List;
import co.unicauca.workflow.domain.entities.Docente;

/**
 *
 * @author CRISTHIAN TORRES
 */
public interface IDocenteRepository {
    
    boolean save(Docente newDocente);
    List<Docente> list();
    
    Docente findById(int id);
    
}

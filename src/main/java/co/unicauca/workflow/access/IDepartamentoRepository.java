/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package co.unicauca.workflow.access;
import co.unicauca.workflow.domain.entities.Departamento;
import java.util.List;

/**
 *
 * @author CRISTHIAN TORRES
 * 
 */

public interface IDepartamentoRepository {
    
    boolean save(Departamento newDepartarmento);
    
    List<Departamento> list();
    

}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package co.unicauca.workflow.access;

import co.unicauca.workflow.domain.entities.FormatoA;
import java.util.List;

/**
 *
 * @author User
 */
public interface IFormatoARepository {
        
    boolean save(FormatoA newFormatoA);

    List<FormatoA > list();
    public FormatoA findById(int id);
    boolean updateEstadoYObservaciones(int idFormato, String estado, String observaciones);
    boolean update (FormatoA formatoA);

}

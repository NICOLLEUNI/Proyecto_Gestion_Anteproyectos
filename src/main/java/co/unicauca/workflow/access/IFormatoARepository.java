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
    public boolean updateEstadoObservacionesYContador(int id, String estado, String observaciones, int contador) ;
    boolean update (FormatoA formatoA);

}

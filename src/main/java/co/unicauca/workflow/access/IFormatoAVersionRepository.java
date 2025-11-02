/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package co.unicauca.workflow.access;

import co.unicauca.workflow.domain.entities.FormatoAVersion;
import java.util.List;

/**
 *
 * @author Usuario
 */
public interface IFormatoAVersionRepository {
    
    boolean save(FormatoAVersion newFormatoAVersion);

    List<FormatoAVersion > list();
    
    public FormatoAVersion findById(int id);
    public boolean deleteByFormatoAId(int formatoAId) ;
     public List<FormatoAVersion> listByFormatoA(int formatoAId);
}

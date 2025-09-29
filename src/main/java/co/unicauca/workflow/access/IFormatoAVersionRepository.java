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
    
    // 🔹 Nuevo método para cargar las versiones de un FormatoA específico
    public List<FormatoAVersion> listByFormatoA(int formatoAId);
    
    public boolean update(FormatoAVersion version);
    
    public boolean deleteByFormatoAId(int formatoAId) ;
    
}

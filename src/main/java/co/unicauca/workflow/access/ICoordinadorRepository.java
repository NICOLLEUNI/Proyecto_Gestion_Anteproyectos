/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.unicauca.workflow.access;

import co.unicauca.workflow.domain.entities.Coordinador;
import co.unicauca.workflow.domain.entities.FormatoA;
import java.util.List;


public interface ICoordinadorRepository {
     boolean save(Coordinador newCoordinador);

    List<Coordinador> list();
   
}

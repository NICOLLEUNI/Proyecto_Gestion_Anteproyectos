/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.unicauca.workflow.access;

import co.unicauca.workflow.domain.entities.Persona;

import java.util.List;

/**
 *
 * @author User
 */
public interface IPersonaRepository {
    
     boolean save(Persona newPersona);

    List<Persona> list();
}

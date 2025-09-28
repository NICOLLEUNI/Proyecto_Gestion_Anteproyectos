/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package co.unicauca.workflow.domain.entities;

/**
 *
 * @author Usuario
 */

public enum enumModalidad {
    
    INVESTIGACION("Investigación"),
    PRACTICA_PROFESIONAL("Práctica profesional"),
    PLAN_COTERMINAL("Plan coterminal");
    
    private final String descripcion;

    enumModalidad(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}

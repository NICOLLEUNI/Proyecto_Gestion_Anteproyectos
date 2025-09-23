/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package co.unicauca.workflow.domain.entities;

/**
 *
 * @author Usuario
 */
public enum enumEstado {
    
    
    ENTREGADO("Entregado"),
    APROBADO("Aprobado"),
    RECHAZADO("Rechazado");
    
    private final String descripcion;

    enumEstado(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
    
}

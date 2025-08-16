/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package co.unicauca.workflow.domain.entities;

/**
 *
 * @author User
 */
public enum enumProgram {
    INGENIERIA_SISTEMAS("Ingeniería de Sistemas"),
    INGENIERIA_ELECTRONICA("Ingeniería Electrónica y Telecomunicaciones"),
    AUTOMATICA_INDUSTRIAL("Automática Industrial"),
    TECNOLOGIA_TELEMATICA("Tecnología en Telemática");
    
     private final String descripcion;

    enumProgram(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
}

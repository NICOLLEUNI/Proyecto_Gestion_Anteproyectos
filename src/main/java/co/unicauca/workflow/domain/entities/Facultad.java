/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.unicauca.workflow.domain.entities;

/**
 *
 * @author User
 */
public class Facultad {
    
    private int codFacultad; //generico
    private String Nombre; //txt

    
    public Facultad(String Nombre) {
        this.Nombre = Nombre;
    }

    public int getCodFacultad() {
        return codFacultad;
    }

    public void setCodFacultad(int codFacultad) {
        this.codFacultad = codFacultad;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.unicauca.workflow.domain.entities;

/**
 *
 * @author User
 */
public class Departamento {
    
    private int codDepartamento; //generico incremental
    private int nombre; //txt
    private Facultad Facultad; //fk 

    public Departamento(int nombre, Facultad Facultad) {
        this.nombre = nombre;
        this.Facultad = Facultad;
    }

    public int getCodDepartamento() {
        return codDepartamento;
    }

    public void setCodDepartamento(int codDepartamento) {
        this.codDepartamento = codDepartamento;
    }

    public int getNombre() {
        return nombre;
    }

    public void setNombre(int nombre) {
        this.nombre = nombre;
    }

    public Facultad getFacultad() {
        return Facultad;
    }

    public void setFacultad(Facultad Facultad) {
        this.Facultad = Facultad;
    }


    
}

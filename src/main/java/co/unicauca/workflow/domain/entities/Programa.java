/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.unicauca.workflow.domain.entities;

/**
 *
 * @author User
 */
public class Programa {
   private int codPrograma; //generico
   private  String nombrePrograma;
   
   //es una llave foranea de la clase Departamento 
   private Departamento departamento;

    public Programa(int codPrograma, String nombrePrograma, Departamento departamento) {
        this.codPrograma = codPrograma;
        this.nombrePrograma = nombrePrograma;
        this.departamento = departamento;
    }

    public int getCodPrograma() {
        return codPrograma;
    }

    public void setCodPrograma(int codPrograma) {
        this.codPrograma = codPrograma;
    }

    public String getNombrePrograma() {
        return nombrePrograma;
    }

    public void setNombrePrograma(String nombrePrograma) {
        this.nombrePrograma = nombrePrograma;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }
   
}

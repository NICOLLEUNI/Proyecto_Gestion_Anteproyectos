/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.unicauca.workflow.domain.entities;

/**
 *
 * @author User
 */
public class Docente extends Persona{

   private Departamento Departamento;

    public Docente(Departamento Departamento, int idUsuario, String name, String lastname, String phone, String email, String password) {
        super(idUsuario, name, lastname, phone, email, password);
        this.Departamento = Departamento;
    }

    public Departamento getDepartamento() {
        return Departamento;
    }

    public void setDepartamento(Departamento Departamento) {
        this.Departamento = Departamento;
    }
   
   
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.unicauca.workflow.domain.entities;

/**
 *
 * @author User
 */
    public class Estudiante extends Persona{
    
    
    private enumProgram program;

    public Estudiante(enumProgram program, int idUsuario, String name, String lastname, String phone, String email, String password) {
        super(idUsuario, name, lastname, phone, email, password);
        this.program = program;
    }

    public enumProgram getProgram() {
        return program;
    }

    public void setProgram(enumProgram program) {
        this.program = program;
    }

    
    
    
}

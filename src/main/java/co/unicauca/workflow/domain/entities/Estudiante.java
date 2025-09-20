/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.unicauca.workflow.domain.entities;

import co.unicauca.workflow.domain.exceptions.ValidationException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
    public class Estudiante extends Persona{
    
    
    private Programa program;

    public Estudiante(Programa program, int idUsuario, String name, String lastname, String phone, String email, String password) throws ValidationException {
        super(idUsuario, name, lastname, phone, email, password);
        this.program = program;
        
        validarCamposEstudiante();
    }
    
    private void validarCamposEstudiante() throws ValidationException{
        List<String> errores = new ArrayList<>();
        
        if (program == null){
            errores.add("El programa es obligatorio.");
        }
        
        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }
        
    }
            
    public Programa getProgram() {
        return program;
    }

    public void setProgram(Programa program) {
        this.program = program;
    }

    
    
    
}

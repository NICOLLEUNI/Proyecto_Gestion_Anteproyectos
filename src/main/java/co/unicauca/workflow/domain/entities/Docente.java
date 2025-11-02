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
public class Docente extends Persona{
    
    
   private Departamento Departamento; // fk id 

    public Docente(int idUsuario, Departamento Departamento, String name, String lastname, String phone, String email, String password) throws ValidationException {
        super(idUsuario, name, lastname, phone, email, password);
        this.Departamento = Departamento;
        
        validarCamposDocente();
        
    }

    public Docente() {
    }
    
    
    
    public void validarCamposDocente() throws ValidationException{
        List<String> errores = new ArrayList<>();
        
        if(Departamento == null){
            errores.add("El departamento es obligatorio");
        }
        
        if(!errores.isEmpty()){
            throw new ValidationException(errores);
        }
         
    }

    public Departamento getDepartamento() {
        return Departamento;
    }

    public void setDepartamento(Departamento Departamento) {
        this.Departamento = Departamento;
    }
    
    @Override
    public String toString() {
        return getEmail(); // usa el getter heredado de Persona
    } 

   
   
}

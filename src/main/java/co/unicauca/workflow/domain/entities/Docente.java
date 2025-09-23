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
    
    
   private int codDocente; //generico
   private Departamento Departamento; // fk id 

    public Docente(Departamento Departamento, int idUsuario, String name, String lastname, String phone, String email, String password) throws ValidationException {
        super(name, lastname, phone, email, password);
        this.Departamento = Departamento;
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

    public int getCodDocente() {
        return codDocente;
    }

    public void setCodDocente(int codDocente) {
        this.codDocente = codDocente;
    }
    
    

    public Departamento getDepartamento() {
        return Departamento;
    }

    public void setDepartamento(Departamento Departamento) {
        this.Departamento = Departamento;
    }
   
   
}

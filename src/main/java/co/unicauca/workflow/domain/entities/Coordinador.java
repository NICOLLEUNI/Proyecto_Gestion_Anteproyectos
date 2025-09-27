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
public class Coordinador extends Persona {
    



    private Departamento Departamento;


    public Coordinador(int idUsuario, Departamento Departamento, String name, String lastname, String phone, String email, String password) throws ValidationException {
        super(idUsuario,name, lastname, phone, email, password);

        this.Departamento = Departamento;
        validarCampos();
    }

    public Coordinador() {
    }
    
    private void validarCampos() throws ValidationException {
           List<String> errores = new ArrayList<>();
     
        if (Departamento == null) {
            errores.add("El coordinador debe estar asociado a un departamento.");
        }
        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }
    }
    
    public Departamento getDepartamento() {
        return Departamento;
    }

    public void setDepartamento(Departamento Departamento) {
        this.Departamento = Departamento;
    }
    
   

    
 


  
    
    
    
}

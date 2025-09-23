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
    



    private int  codigoCoordinador;
    private Departamento Departamento;


    public Coordinador(int codigoCoordinador, Departamento Departamento, String name, String lastname, String phone, String email, String password) throws ValidationException {
        super(name, lastname, phone, email, password);
        this.codigoCoordinador = codigoCoordinador;
        this.Departamento = Departamento;
         validarCampos();
    }
    private void validarCampos() throws ValidationException {
           List<String> errores = new ArrayList<>();
        if (codigoCoordinador <= 0) {
            errores.add("El código del coordinador debe ser un número positivo.");
        }

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
    
   

    
 
    public int getCodigoCoordinador() {
        return codigoCoordinador;
    }

    public void setCodigoCoordinador(int codigoCoordinador) {
        this.codigoCoordinador = codigoCoordinador;
    }

  
    
    
    
}

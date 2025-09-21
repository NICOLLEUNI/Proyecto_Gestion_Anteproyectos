/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.unicauca.workflow.domain.entities;

import co.unicauca.workflow.domain.exceptions.ValidationException;
import java.util.List;

/**
 *
 * @author User
 */
public class Coordinador extends Persona {
    
    private int  codCoordinador; //generico
    private List<FormatoA> listaProyectos;

    public Coordinador(List<FormatoA> listaProyectos, int idUsuario, String name, String lastname, String phone, String email, String password) throws ValidationException {
        super(name, lastname, phone, email, password);
        this.listaProyectos = listaProyectos;
    }

    public int getCodCoordinador() {
        return codCoordinador;
    }

    public void setCodCoordinador(int codCoordinador) {
        this.codCoordinador = codCoordinador;
    }
    
    

    public List<FormatoA> getListaProyectos() {
        return listaProyectos;
    }

    public void setListaProyectos(List<FormatoA> listaProyectos) {
        this.listaProyectos = listaProyectos;
    }
    
    
    
}

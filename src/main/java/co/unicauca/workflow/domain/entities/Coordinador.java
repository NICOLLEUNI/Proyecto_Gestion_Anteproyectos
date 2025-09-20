/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.unicauca.workflow.domain.entities;

import java.util.List;

/**
 *
 * @author User
 */
public class Coordinador extends Persona {
    
    private int  codigoCoordinador;
    private List<FormatoA> listaProyectos;

    public Coordinador(int codigoCoordinador, List<FormatoA> listaProyectos, int idUsuario, String name, String lastname, String phone, String email, String password) {
        super(idUsuario, name, lastname, phone, email, password);
        this.codigoCoordinador = codigoCoordinador;
        this.listaProyectos = listaProyectos;
    }

    public int getCodigoCoordinador() {
        return codigoCoordinador;
    }

    public void setCodigoCoordinador(int codigoCoordinador) {
        this.codigoCoordinador = codigoCoordinador;
    }

    public List<FormatoA> getListaProyectos() {
        return listaProyectos;
    }

    public void setListaProyectos(List<FormatoA> listaProyectos) {
        this.listaProyectos = listaProyectos;
    }
    
    
    
}

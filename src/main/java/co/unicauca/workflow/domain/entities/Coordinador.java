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
    
    private int  codigoCoordinador; //es generico 
    private List<FormatoA> listaProyectos;

    public Coordinador(int codigoCoordinador, List<FormatoA> listaProyectos, int idUsuario, String name, String lastname, String phone, String email, String password) throws ValidationException {
        super(name, lastname, phone, email, password);
        this.codigoCoordinador = codigoCoordinador;
        this.listaProyectos = listaProyectos;
        validarCamposCoordinador();
    }
    public void validarCamposCoordinador() throws ValidationException {
    List<String> errores = new ArrayList<>();

    if (codigoCoordinador <= 0) {
        errores.add("El código del coordinador debe ser mayor que 0.");
    }

    if (listaProyectos == null) {
        errores.add("La lista de proyectos no puede ser nula.");
    } else if (listaProyectos.isEmpty()) {
        errores.add("El coordinador debe tener al menos un proyecto asignado.");
    }

    if (!errores.isEmpty()) {
        throw new ValidationException(errores);
    }
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

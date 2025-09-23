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
public class Departamento {

    private int codDepartamento; 
    private String nombre;       
    private Facultad facultad;   

    public Departamento(String nombre, Facultad facultad) throws ValidationException {
        this.nombre = nombre;
        this.facultad = facultad;

        validarCamposDepartamento();
    }

    public Departamento() {

    }

    /**
     * Valida los campos del Departamento
     * 
     * @throws ValidationException si hay errores de validación
     */
    public void validarCamposDepartamento() throws ValidationException {
        List<String> errores = new ArrayList<>();

        if (nombre == null || nombre.trim().isEmpty()) {
            errores.add("El nombre del departamento es obligatorio.");
        }

        if (facultad == null) {
            errores.add("La facultad es obligatoria.");
        }

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }
    }

    public int getCodDepartamento() {
        return codDepartamento;
    }

    public void setCodDepartamento(int codDepartamento) {
        this.codDepartamento = codDepartamento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Facultad getFacultad() {
        return facultad;
    }

    public void setFacultad(Facultad facultad) {
        this.facultad = facultad;
    }
}

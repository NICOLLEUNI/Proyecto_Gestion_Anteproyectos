/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.unicauca.workflow.domain.entities;

import co.unicauca.workflow.domain.exceptions.ValidationException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una Facultad de la Universidad del Cauca
 * 
 * @author User
 */
public class Facultad {
    private int codFacultad; // genérico, lo asigna la BD
    private String nombre;


    public Facultad(String nombre) throws ValidationException {
        this.nombre = nombre;
        validarCamposFacultad();
    }

    /**
     * Valida los campos de la Facultad
     * 
     * @throws ValidationException si hay errores de validación
     */
    private void validarCamposFacultad() throws ValidationException {
        List<String> errores = new ArrayList<>();

        if (nombre == null || nombre.trim().isEmpty()) {
            errores.add("El nombre de la facultad es obligatorio.");
        } else {
            // Validar que sea la FIET (única facultad válida para este sistema)
            if (!nombre.trim().equalsIgnoreCase("Facultad de Ingeniería Electrónica y Telecomunicaciones") &&
                !nombre.trim().equalsIgnoreCase("FIET")) {
                errores.add("El sistema solo maneja la Facultad de Ingeniería Electrónica y Telecomunicaciones (FIET).");
            }
        }

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

    }

    public int getCodFacultad() {
        return codFacultad;
    }

    public void setCodFacultad(int codFacultad) {
        this.codFacultad = codFacultad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) throws ValidationException {
        this.nombre = nombre;
        validarCamposFacultad();
    }

    @Override
    public String toString() {
        return nombre;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Facultad facultad = (Facultad) obj;
        return nombre.equalsIgnoreCase(facultad.nombre);
    }

    @Override
    public int hashCode() {
        return nombre.toLowerCase().hashCode();
    }
}

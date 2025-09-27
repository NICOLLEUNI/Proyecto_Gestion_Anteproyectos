/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.unicauca.workflow.domain.entities;

import co.unicauca.workflow.domain.exceptions.ValidationException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un Programa académico de la FIET
 * 
 * @author User
 */
public class Programa {
    private int codPrograma; // GENÉRICO - lo asigna la BD (auto-increment)
    private String nombrePrograma; // 👈 CAMBIO: String en lugar de enum
    private Departamento departamento;

    // CONSTRUCTOR PRINCIPAL (con código)
    public Programa(String nombrePrograma, Departamento departamento) throws ValidationException {
        this.nombrePrograma = nombrePrograma;
        this.departamento = departamento;
        validarCamposPrograma();
    }

    public Programa(int codPrograma, String nombrePrograma, Departamento departamento) {
        this.codPrograma = codPrograma;
        this.nombrePrograma = nombrePrograma;
        this.departamento = departamento;
    }

    public Programa() {
    }



    /**
     * Valida los campos del Programa según las reglas de negocio
     * 
     * @throws ValidationException si hay errores de validación
     */
    private void validarCamposPrograma() throws ValidationException {
        List<String> errores = new ArrayList<>();

        // Validación de nombre
        if (nombrePrograma == null || nombrePrograma.trim().isEmpty()) {
            errores.add("El nombre del programa es obligatorio.");
        }

        // Validación de departamento
        if (departamento == null) {
            errores.add("El departamento es obligatorio.");
        }

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }
    }

    // Getters y Setters SIMPLIFICADOS
    public int getCodPrograma() {
        return codPrograma;
    }

    public void setCodPrograma(int codPrograma) {
        this.codPrograma = codPrograma;
    }

    public String getNombrePrograma() {
        return nombrePrograma;
    }

    public void setNombrePrograma(String nombrePrograma) throws ValidationException {
        this.nombrePrograma = nombrePrograma;
        validarCamposPrograma();
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) throws ValidationException {
        this.departamento = departamento;
        validarCamposPrograma();
    }

}


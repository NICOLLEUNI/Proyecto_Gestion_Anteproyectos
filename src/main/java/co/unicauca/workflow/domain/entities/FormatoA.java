/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.unicauca.workflow.domain.entities;

import co.unicauca.workflow.domain.exceptions.ValidationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author User
 */
public class FormatoA {
    
    private int id; //generico
    private String title; 
    private enumModalidad mode; 
    private List<Docente> proyectManager;
    private Coordinador coordinador; 
    private LocalDate date;
    private String generalObjetive;
    private String specificObjetives;
    private String archivoPDF;
    private List<Estudiante> estudiantes;
    private int counter; //Para el numero de intentos
    private String state; //entregado, aceptado o rechazado
    private String observations; //para las observaciones del coordinador

    public FormatoA(int id, String title, enumModalidad mode, List<Docente> proyectManager, LocalDate date, String generalObjetive, String specificObjetives, String archivoPDF, List<Estudiante> estudiantes, int counter, String state) throws ValidationException {
        this.id = id;
        this.title = title;
        this.mode = mode;
        this.proyectManager = proyectManager;
        this.date = date;
        this.generalObjetive = generalObjetive;
        this.specificObjetives = specificObjetives;
        this.archivoPDF = archivoPDF;
        this.estudiantes = estudiantes;
        this.counter = counter;
        this.state = state;
        
        validarCamposFormatoA();
    }

    public FormatoA(int id, String title, enumModalidad mode, List<Docente> proyectManager, Coordinador coordinador, LocalDate date, String generalObjetive, String specificObjetives, String archivoPDF, List<Estudiante> estudiantes, int counter, String state, String observations) throws ValidationException {
        this.id = id;
        this.title = title;
        this.mode = mode;
        this.proyectManager = proyectManager;
        this.coordinador = coordinador;
        this.date = date;
        this.generalObjetive = generalObjetive;
        this.specificObjetives = specificObjetives;
        this.archivoPDF = archivoPDF;
        this.estudiantes = estudiantes;
        this.counter = counter;
        this.state = state;
        this.observations = observations;
        
        validarCamposFormatoA();
    }
    
    
    public FormatoA() {
    }
    
    /**
     * Valida los campos de FormatoA
     * @throws ValidationException si existen errores
     */
    private void validarCamposFormatoA() throws ValidationException {
        List<String> errores = new ArrayList<>();

        if (id <= 0) {
            errores.add("El ID debe ser mayor a 0.");
        }
        if (title == null || title.trim().isEmpty()) {
            errores.add("El título es obligatorio.");
        }
        if (mode == null) {
            errores.add("La modalidad es obligatoria.");
        }
        if (proyectManager == null || proyectManager.isEmpty()) {
            errores.add("Debe asignarse al menos un docente como Project Manager.");
        }
       
        if (date == null) {
            errores.add("La fecha es obligatoria.");
        }
        if (generalObjetive == null || generalObjetive.trim().isEmpty()) {
            errores.add("El objetivo general es obligatorio.");
        }
        if (specificObjetives == null || specificObjetives.trim().isEmpty()) {
            errores.add("Los objetivos específicos son obligatorios.");
        }
        if (archivoPDF == null || archivoPDF.trim().isEmpty()) {
            errores.add("El archivo PDF es obligatorio.");
        }
        if (estudiantes == null || estudiantes.isEmpty()) {
            errores.add("Debe asignarse al menos un estudiante.");
        }
        if (counter < 0) {
            errores.add("El contador de intentos no puede ser negativo.");
        }
        if (state == null || state.trim().isEmpty()) {
            errores.add("El estado es obligatorio (entregado, aceptado o rechazado).");
        }

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public enumModalidad getMode() {
        return mode;
    }

    public void setMode(enumModalidad mode) {
        this.mode = mode;
    }

    public List<Docente> getProyectManager() {
        return proyectManager;
    }

    public void setProyectManager(List<Docente> proyectManager) {
        this.proyectManager = proyectManager;
    }

    public Coordinador getCoordinador() {
        return coordinador;
    }

    public void setCoordinador(Coordinador coordinador) {
        this.coordinador = coordinador;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getGeneralObjetive() {
        return generalObjetive;
    }

    public void setGeneralObjetive(String generalObjetive) {
        this.generalObjetive = generalObjetive;
    }

    public String getSpecificObjetives() {
        return specificObjetives;
    }

    public void setSpecificObjetives(String specificObjetives) {
        this.specificObjetives = specificObjetives;
    }

    public String getArchivoPDF() {
        return archivoPDF;
    }

    public void setArchivoPDF(String archivoPDF) {
        this.archivoPDF = archivoPDF;
    }

    public List<Estudiante> getEstudiantes() {
        return estudiantes;
    }

    public void setEstudiantes(List<Estudiante> estudiantes) {
        this.estudiantes = estudiantes;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    

}

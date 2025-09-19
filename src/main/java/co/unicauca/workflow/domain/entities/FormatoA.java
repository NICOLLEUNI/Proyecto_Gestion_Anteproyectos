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
    private int id;
    private String title;
    private String mode;
    private String proyectManager;  
    private String projectCoManager;
    private LocalDate date;
    private String generalObjetive;
    private String specificObjetives;
    private String archivoPDF;
    private String studentCode;
    private int counter; //Para el numero de intentos
    private String state; //entregado, aceptado o rechazado
    private String observations; //para las observaciones del coordinador
    
    

    public FormatoA(String title, String mode, String proyectManager, String projectCoManager, LocalDate date, String generalObjetive, String specificObjetives,  String archivoPDF, String studentCode, int counter, String state, String observations) {
        this.title = title;
        this.mode = mode;
        this.proyectManager = proyectManager;
        this.projectCoManager = projectCoManager;
        this.date = date;
        this.generalObjetive = generalObjetive;
        this.specificObjetives = specificObjetives;
        this.studentCode = studentCode;
        this.counter = counter;
        this.state = state;
        this.observations = observations;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public String getEstado() {
        return estado;
    }
    

    
    public FormatoA() {
    }

    //
   public void validarCampos() throws ValidationException {
        List<String> errores = new ArrayList<>();

        // 🔹 Validaciones básicas
        if (title == null || title.trim().isEmpty()) {
            errores.add("El título no puede estar vacío.");
        }

        if (mode == null || mode.trim().isEmpty()) {
            errores.add("Debe especificar la modalidad del proyecto.");
        }

        if (date == null) {
            errores.add("Debe especificar la fecha.");
        }

        if (generalObjetive == null || generalObjetive.trim().isEmpty()) {
            errores.add("Debe especificar el objetivo general.");
        }

        if (specificObjetives == null || specificObjetives.trim().isEmpty()) {
            errores.add("Debe especificar al menos un objetivo específico.");
        }

        if (archivoPDF == null || archivoPDF.trim().isEmpty()) {
            errores.add("Debe adjuntar el archivo PDF del Formato A.");
        }

        if (studentCode == null || studentCode.trim().isEmpty()) {
            errores.add("Debe especificar el código del estudiante.");
        }

        // 🔹 Validaciones de negocio (ejemplo)
        if (counter < 0) {
            errores.add("El número de intentos no puede ser negativo.");
        }
        if (counter > 3) {
            errores.add("El proyecto ya no puede ser enviado después del tercer intento.");
        }

        // 🔹 Si hay errores → lanzar excepción
        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }
        
    }
    
    
    
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMode() {
        return mode;
    }

    public String getProyectManager() {
        return proyectManager;
    }

    public String getProjectCoManager() {
        return projectCoManager;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getGeneralObjetive() {
        return generalObjetive;
    }

    public String getSpecificObjetives() {
        return specificObjetives;
    }

    public String getArchivoPDF() {
        return archivoPDF;
    }

    public String getStudentCode() {
        return studentCode;
    }

    public int getCounter() {
        return counter;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setProyectManager(String proyectManager) {
        this.proyectManager = proyectManager;
    }

    public void setProjectCoManager(String projectCoManager) {
        this.projectCoManager = projectCoManager;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setGeneralObjetive(String generalObjetive) {
        this.generalObjetive = generalObjetive;
    }

    public void setSpecificObjetives(String specificObjetives) {
        this.specificObjetives = specificObjetives;
    }

    public void setArchivoPDF(String archivoPDF) {
        this.archivoPDF = archivoPDF;
    }

    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
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

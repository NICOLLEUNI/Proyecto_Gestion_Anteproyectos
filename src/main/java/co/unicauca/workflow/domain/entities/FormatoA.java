/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.unicauca.workflow.domain.entities;

import java.time.LocalDate;
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
    private String counter;

    public FormatoA(String title, String mode, String proyectManager, String projectCoManager, LocalDate date, String generalObjetive, String specificObjetives,  String archivoPDF, String studentCode, String counter) {
        this.title = title;
        this.mode = mode;
        this.proyectManager = proyectManager;
        this.projectCoManager = projectCoManager;
        this.date = date;
        this.generalObjetive = generalObjetive;
        this.specificObjetives = specificObjetives;
        this.archivoPDF = archivoPDF;
        this.studentCode = studentCode;
        this.counter = counter;
    }

    public FormatoA() {
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

    public String getCounter() {
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

    public void setCounter(String counter) {
        this.counter = counter;
    }

 

    
    
}

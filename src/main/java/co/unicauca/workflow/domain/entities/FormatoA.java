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
    private enumModalidad mode; // Modalidad (Investigación, Práctica laboral, etc.)
    private Docente projectManager;     // Director
    private Docente projectCoManager;   // Codirector (opcional)
    private LocalDate date;
    private String generalObjetive;
    private String specificObjetives;
    private String archivoPDF;
    private String cartaLaboral;        // Solo si es práctica laboral
    private List<Estudiante> estudiantes; 
    private List<FormatoAVersion> versiones = new ArrayList<>(); //lista de las versiones del formatoA 
    private int counter;                // Veces rechazado (máx. 3)
    private enumEstado state = enumEstado.ENTREGADO; // entregado (default), rechazado, aprobado
    private String observations;        // Observaciones del coordinador

    public FormatoA(int id, String title, enumModalidad mode, Docente projectManager, Docente projectCoManager, LocalDate date, String generalObjetive, String specificObjetives, String archivoPDF, String cartaLaboral, List<Estudiante> estudiantes, int counter, String observations) {
        this.id = id;
        this.title = title;
        this.mode = mode;
        this.projectManager = projectManager;
        this.projectCoManager = projectCoManager;
        this.date = date;
        this.generalObjetive = generalObjetive;
        this.specificObjetives = specificObjetives;
        this.archivoPDF = archivoPDF;
        this.cartaLaboral = cartaLaboral;
        this.estudiantes = estudiantes;
        this.counter = counter;
        this.observations = observations;
    }
    
    public FormatoA() {
    }
    
    //Método de validación
    public void validar() throws ValidationException {
        List<String> errores = new ArrayList<>();

        if (title == null || title.isBlank()) {
            errores.add("El título no puede ser nulo o vacío.");
        }
        if (mode == null) {
            errores.add("Debe especificar la modalidad.");
        }
        if (projectManager == null) {
            errores.add("Debe asignar un director.");
        }
        if (date == null) {
            errores.add("La fecha no puede ser nula.");
        }
        if (generalObjetive == null || generalObjetive.isBlank()) {
            errores.add("El objetivo general es obligatorio.");
        }
        if (specificObjetives == null || specificObjetives.isBlank()) {
            errores.add("Debe definir los objetivos específicos.");
        }
        if (archivoPDF == null || archivoPDF.isBlank()) {
            errores.add("El archivo PDF es obligatorio.");
        }
        if (estudiantes == null || estudiantes.isEmpty()) {
            errores.add("Debe asignarse al menos un estudiante.");
        }

        //Si hay errores, lanzamos la excepción con la lista
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

    public Docente getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(Docente projectManager) {
        this.projectManager = projectManager;
    }

    public Docente getProjectCoManager() {
        return projectCoManager;
    }

    public void setProjectCoManager(Docente projectCoManager) {
        this.projectCoManager = projectCoManager;
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

    public String getCartaLaboral() {
        return cartaLaboral;
    }

    public void setCartaLaboral(String cartaLaboral) {
        this.cartaLaboral = cartaLaboral;
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

    public enumEstado getState() {
        return state;
    }

    public void setState(enumEstado state) {
        this.state = state;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }
   
    public List<FormatoAVersion> getVersiones() {
    return versiones;
}

    public void setVersiones(List<FormatoAVersion> versiones) {
        this.versiones = versiones;
    }

}

   
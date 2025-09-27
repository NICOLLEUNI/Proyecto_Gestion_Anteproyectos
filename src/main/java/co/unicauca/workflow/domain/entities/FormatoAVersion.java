/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.unicauca.workflow.domain.entities;
import java.time.LocalDate;


/**
 *
 * @author Usuario
 */
public class FormatoAVersion {
    private int idCopia;             // PK autoincrement
    private int numeroVersion;       // 1, 2, 3...
    private LocalDate fecha;         

    // Copia de campos versionables
    private String title;
    private enumModalidad mode;
    private String generalObjetive;
    private String specificObjetives;
    private String archivoPDF;
    private String cartaLaboral;     // mismo nombre que en FormatoA

    // Estado de la versión
    private enumEstado state;        // entregado, aprobado, rechazado
    private String observations;     // comentarios del coordinador

    // Relación con FormatoA principal
    private FormatoA formatoA;

    public FormatoAVersion(int idCopia, int numeroVersion, LocalDate fecha, String title, enumModalidad mode, String generalObjetive, String specificObjetives, String archivoPDF, String cartaLaboral, enumEstado state, String observations, FormatoA formatoA) {
        this.idCopia = idCopia;
        this.numeroVersion = numeroVersion;
        this.fecha = fecha;
        this.title = title;
        this.mode = mode;
        this.generalObjetive = generalObjetive;
        this.specificObjetives = specificObjetives;
        this.archivoPDF = archivoPDF;
        this.cartaLaboral = cartaLaboral;
        this.state = state;
        this.observations = observations;
        this.formatoA = formatoA;
    }

    public int getIdCopia() {
        return idCopia;
    }

    public void setIdCopia(int idCopia) {
        this.idCopia = idCopia;
    }

    public int getNumeroVersion() {
        return numeroVersion;
    }

    public void setNumeroVersion(int numeroVersion) {
        this.numeroVersion = numeroVersion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
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

    public FormatoA getFormatoA() {
        return formatoA;
    }

    public void setFormatoA(FormatoA formatoA) {
        this.formatoA = formatoA;
    }
    
    
}



/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.unicauca.workflow.access;

/**
 *
 * @author User
 */
public class Factory {
    
    private static Factory instance;
    
    private Factory() {
    }
    
    /**
     * Clase singleton
     *
     * @return
     */
    public static Factory getInstance() {
        if (instance == null) {
            instance = new Factory();
        }
        return instance;
    }
    
    /**
     * Método que crea una instancia concreta de la jerarquia IUserRepository
     *
     * @param type cadena que indica qué tipo de clase hija debe instanciar
     * @return una clase hija de la abstracción IUserRepository
     */

    
    public static IPersonaRepository getPersonaRepository(String type) {
        IPersonaRepository result = null;
        switch (type.toLowerCase()) {
            case "default":
                result = new PersonaRepository();
                break;
            // Si en el futuro tienes otro tipo de repositorio, agregas otro case
            // case "mock": result = new PersonaRepositoryMock(); break;
        }
        return result;
    }
    
    // *** AGREGADO: Método para EstudianteRepository ***
    public static IEstudianteRepository getEstudianteRepository(String type) {
        IEstudianteRepository result = null;
        switch (type.toLowerCase()) {
            case "default":
                result = new EstudianteRepository();
                break;
            // case "mock": result = new EstudianteRepositoryMock(); break;
        }
        return result;
    }
        
    public static ICoordinadorRepository getCoordinadorRepository(String type) {
        ICoordinadorRepository result = null;
        switch (type.toLowerCase()) {
            case "default":
                result = new CoordinadorRepository();
                break;
            // Si en el futuro tienes otro tipo de repositorio, agregas otro case
            // case "mock": result = new CoordinadorRepositoryMock(); break;
        }
        return result;
    }
      
    
    public static IFormatoARepository getFormatoARepository(String type) {
        IFormatoARepository result = null;
        switch (type.toLowerCase()) {
            case "default":
                result = new FormatoARepository();
                break;
            // Si en el futuro tienes otro tipo de repositorio, agregas otro case
            // case "mock": result = new FormatoARepositoryMock(); break;
        }
        return result;
    }
    
    public static IDocenteRepository getDocenteRepository(String type) {
        IDocenteRepository result = null;
        switch (type.toLowerCase()) {
            case "default":
                result = new DocenteRepository();
                break;
            // Si en el futuro tienes otro tipo de repositorio, agregas otro case
            // case "mock": result = new DocenteRepositoryMock(); break;
        }
        return result;
    }
    

    // *** AGREGADO: Método para ProgramaRepository ***
    public static IProgramaRepository getProgramaRepository(String type) {
        IProgramaRepository result = null;
        switch (type.toLowerCase()) {
            case "default":
                result = new ProgramaRepository();
                break;
            // case "mock": result = new ProgramaRepositoryMock(); break;
        }
        return result;
    }
    
    // *** AGREGADO: Método para DepartamentoRepository ***
    public static IDepartamentoRepository getDepartamentoRepository(String type) {
        IDepartamentoRepository result = null;
        switch (type.toLowerCase()) {
            case "default":
                result = new DepartamentoRepository();
                break;
            // case "mock": result = new DepartamentoRepositoryMock(); break;
        }
        
        return result;
    }

    public static IFormatoAVersionRepository getFormatoAVersionRepository(String type) {
        IFormatoAVersionRepository result = null;

        switch (type.toLowerCase()) {
            case "default":
                result = new FormatoAVersionRepository();
                break;
            // Si en el futuro tienes otro tipo de repositorio, agregas otro case
            // case "mock": result = new PersonaRepositoryMock(); break;
        }
        
        return result;
    }
    
// Agregar al Factory.java
public static IFacultadRepository getFacultadRepository(String type) {
    IFacultadRepository result = null;
    switch (type.toLowerCase()) {
        case "default":
            result = new FacultadRepository();
            break;
    }
    return result;
}
    
    // Por cada repositorio creado se deben crear dos métodos: el que instancia el repositorio y 
    // el que le asigna el tipo de BDD (los métodos se deben llamar diferentes por cada repositorio)  
}
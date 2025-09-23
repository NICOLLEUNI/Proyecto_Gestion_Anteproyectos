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
     * Método que crea una instancia concreta de la jerarquia IProductRepository
     *
     * @param type cadena que indica qué tipo de clase hija debe instanciar
     * @return una clase hija de la abstracción IProductRepository
     */
    public IUserRepository getRepository(String type) {

        IUserRepository result = null;

        switch (type) {
            case "default":
                result = new UserRepository();
                break;
        }

        return result;
    }
    
    
    
    //Por cada repositorio creado se deben crear dos metodos: el que instancia la repositorio y 
    // el que le asigna el tipo de BDD (los metodos se deben llamar diferentes por cada repositorio)  
}


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
    
    public Facultad(){
    }
    

    /**
     * Valida los campos de la Facultad
     * 
     * @throws ValidationException si hay errores de validación
     */
    private void validarCamposFacultad() throws ValidationException {
        List<String> errores = new ArrayList<>();

        if (nombre == null || nombre.trim().isEmpty()) {
            errores.add("La facultad es obligatoria");
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


}
    

   


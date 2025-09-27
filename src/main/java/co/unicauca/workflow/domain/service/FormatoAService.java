package co.unicauca.workflow.domain.service;

import co.unicauca.workflow.access.IFormatoARepository;
import co.unicauca.workflow.domain.entities.FormatoA;
import co.unicauca.workflow.domain.entities.enumEstado;
import co.unicauca.workflow.domain.exceptions.ValidationException;
import co.unicauca.workflow.infa.Subject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FormatoAService extends Subject{
    
     private IFormatoARepository repo;

    public FormatoAService(IFormatoARepository repo) {
        this.repo = repo;
    }

    //se debe tener en cuenta que cada metodo puede necesitar de otros metodos 
    //crearlos si es necesario

    //Metodo de la dinamica del docente 
    public boolean subirFormatoA(){
        return false;
    } 
    
    
    public void evalarFormatoA(){
    }
    
    
    public void consultarFormatoAEstudiante(){
        //ve una unica respuesta de su formato A
    }
    
    public void consultarFormatoADocente(){
        //el docente ve todas las respuestas de sus formatos A subidos
    }
    
       public List<FormatoA> listFormatoA() {
        return repo.list();
    }
    public FormatoA findById(int id) {
        return repo.findById(id);
    }
public boolean updateEstadoYObservaciones(int idFormato, String estado, String observaciones) {
    // Actualiza en el repositorio
    boolean actualizado = repo.updateEstadoYObservaciones(idFormato, estado, observaciones);
    
    if (actualizado) {
        // Solo notificamos si realmente se actualizó
       this.notifyAllObserves();
    }
    return actualizado;
}
    
    
    
}

    
    

   



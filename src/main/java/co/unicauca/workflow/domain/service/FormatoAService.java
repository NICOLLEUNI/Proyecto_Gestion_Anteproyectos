package co.unicauca.workflow.domain.service;

import co.unicauca.workflow.access.Factory;
import co.unicauca.workflow.access.FormatoAVersionRepository;
import co.unicauca.workflow.access.IFormatoARepository;
import co.unicauca.workflow.access.IFormatoAVersionRepository;
import co.unicauca.workflow.domain.entities.FormatoA;
import co.unicauca.workflow.domain.entities.FormatoAVersion;
import co.unicauca.workflow.domain.entities.enumEstado;
import co.unicauca.workflow.domain.exceptions.ValidationException;
import co.unicauca.workflow.infa.Subject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FormatoAService extends Subject{
    
    private IFormatoARepository repo;
    private IFormatoAVersionRepository versionRepo = Factory.getInstance().getFormatoAVersionRepository("default");

    public FormatoAService(IFormatoARepository repo) {
        this.repo = repo;
    }
    
    
    //se debe tener en cuenta que cada metodo puede necesitar de otros metodos 
    //crearlos si es necesario

    //Metodo de la dinamica del docente 
    public boolean subirFormatoA(FormatoA formatoA){
        try{
            boolean saved = repo.save(formatoA);
            if(!saved) return false;

           

            FormatoAVersion version1 = new FormatoAVersion(
                0,
                1,
                LocalDate.now(),
                formatoA.getTitle(),
                formatoA.getMode(),
                formatoA.getGeneralObjetive(),
                formatoA.getSpecificObjetives(),
                formatoA.getArchivoPDF(),
                formatoA.getCartaLaboral(),
                enumEstado.ENTREGADO,
                null,  // ✅ Las observaciones iniciales deben ser null
                formatoA
            );

            boolean versionSaved = versionRepo.save(version1);
            return versionSaved;

        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
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
    
    public boolean updateEstadoObservacionesYContador(int idFormato, String estado, String observaciones,int counter) {
        // Actualiza en el repositorio
        boolean actualizado = repo. updateEstadoObservacionesYContador(idFormato, estado, observaciones, counter);

        if (actualizado) {
            // Solo notificamos si realmente se actualizó
           this.notifyAllObserves();
        }
        return actualizado;
    }
    public boolean eliminarFormatoAConVersiones(int formatoAId) {
    

    try {
        // 1️⃣ Borrar versiones primero
        versionRepo.deleteByFormatoAId(formatoAId);

        // 2️⃣ Borrar FormatoA + Estudiantes
        boolean formatoEliminado = repo.delete(formatoAId);

        // 3️⃣ Confirmar todo
        if (formatoEliminado) {
         
            return true;
        }
        return false;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
    
}



    
    

   



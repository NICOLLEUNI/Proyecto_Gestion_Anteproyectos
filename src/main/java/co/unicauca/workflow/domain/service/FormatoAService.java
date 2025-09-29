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
import java.util.stream.Collectors;

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

        // 🔹 instanciar aquí el repo
        

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
            "sin observaciones",
            formatoA
        );

        versionRepo.save(version1); // ya no es null
        return true;

    }catch(Exception e){
        e.printStackTrace();
        return false;
    }
}
    
    public void consultarFormatoAEstudiante(){
        //ve una unica respuesta de su formato A
    }

    /**
     * Consultar los formatos asociados a un docente (rol de Persona).
     *
     * @param docente persona logueada con rol DOCENTE
     * @return lista de formatos en los que participa ese docente
     */
    public List<FormatoA> consultarFormatoADocente(Persona docente) {
        List<FormatoA> todos = repository.list();
        return todos.stream()
                .filter(f ->
                        (f.getProjectManager() != null && f.getProjectManager().getIdUsuario() == docente.getIdUsuario()) ||
                        (f.getProjectCoManager() != null && f.getProjectCoManager().getIdUsuario() == docente.getIdUsuario()))
                .collect(Collectors.toList());
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



    
    

   



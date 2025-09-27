package co.unicauca.workflow.domain.service;

import co.unicauca.workflow.access.IFormatoARepository;
import co.unicauca.workflow.domain.entities.FormatoA;
import co.unicauca.workflow.domain.entities.enumEstado;
import co.unicauca.workflow.domain.exceptions.ValidationException;
import co.unicauca.workflow.infa.Subject;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FormatoAService extends Subject{
    
     private IFormatoARepository repo;

    public FormatoAService(IFormatoARepository repo) {
        this.repo = repo;
    }

    //se debe tener en cuenta que cada metodo puede necesitar de otros metodos 
    //crearlos si es necesario

    private final IFormatoARepository repository;

    public FormatoAService(IFormatoARepository repository) {
        this.repository = repository;
    }

    /**
     * Consultar los formatos asociados a un estudiante (rol de Persona).
     *
     * @param estudiante persona logueada con rol ESTUDIANTE
     * @return lista de formatos en los que participa ese estudiante
     */
    public List<FormatoA> consultarFormatoAEstudiante(Persona estudiante) {
        List<FormatoA> todos = repository.list();
        return todos.stream()
                .filter(f -> f.getEstudiantes() != null
                        && f.getEstudiantes().stream()
                              .anyMatch(est -> est.getIdUsuario() == estudiante.getIdUsuario()))
                .collect(Collectors.toList());
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

package co.unicauca.workflow.domain.service;

import co.unicauca.workflow.access.IFormatoARepository;
import co.unicauca.workflow.domain.entities.FormatoA;
import co.unicauca.workflow.domain.entities.Persona;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para manejar la lógica de negocio asociada a FormatoA.
 */
public class FormatoAService {

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
}

package co.unicauca.workflow.domain.service;

import co.unicauca.workflow.access.IFormatoARepository;
import co.unicauca.workflow.domain.entities.FormatoA;
import co.unicauca.workflow.domain.entities.Persona;
import co.unicauca.workflow.domain.entities.enumRol;
import java.util.ArrayList;
import java.util.List;

public class FormatoAService {

    private final IFormatoARepository repo;

    public FormatoAService(IFormatoARepository repo) {
        this.repo = repo;
    }

    /**
     * Un estudiante solo puede ver SU propio Formato A
     * @param estudiante
     * @return 
     */
    /*public List<FormatoA> consultarFormatoAEstudiante(Persona estudiante) {
          List<FormatoA> todos = repo.list();
        List<FormatoA> result = new ArrayList<>();

        for (FormatoA f : todos) {
            if (f.getEstudiantes() != null) {
                boolean pertenece = f.getEstudiantes().stream()
                        .anyMatch(e -> e.getIdUsuario() == estudiante.getIdUsuario());
                if (pertenece) {
                    result.add(f);
                }
            }
        }
        return result;
    }*/

    /**
     * Un docente ve todos los formatos donde es director o codirector
     * @param docente
     * @return 
     */
    /*public List<FormatoA> consultarFormatoADocente(Persona docente) {
         List<FormatoA> todos = repo.list();
        List<FormatoA> result = new ArrayList<>();

        for (FormatoA f : todos) {
            if (f.getProjectManager() != null &&
                    f.getProjectManager().getIdUsuario() == docente.getIdUsuario()) {
                result.add(f);
            } else if (f.getProjectCoManager() != null &&
                    f.getProjectCoManager().getIdUsuario() == docente.getIdUsuario()) {
                result.add(f);
            }
        }
        return result;
    }*/

    /**
     * El coordinador ve TODOS los formatos
     * @param coordinador
     * @return 
     */
    public List<FormatoA> consultarFormatoACoordinador(Persona coordinador) {
        if (coordinador.tieneRol(enumRol.COORDINADOR)) {
            return repo.list();
        }
        return new ArrayList<>();
    }
}

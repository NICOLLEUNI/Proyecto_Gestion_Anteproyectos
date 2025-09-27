/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.unicauca.workflow.domain.service;

import co.unicauca.workflow.access.IDocenteRepository;
import co.unicauca.workflow.domain.entities.Departamento;
import co.unicauca.workflow.domain.entities.Docente;
import co.unicauca.workflow.domain.exceptions.ValidationException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author CRISTHIAN TORRES
 */
public class DocenteService {

    private final IDocenteRepository repository;

    /**
     * Inyección de dependencias en el constructor.
     *
     * @param repository implementación concreta de IDocenteRepository
     */
    public DocenteService(IDocenteRepository repository) {
        this.repository = repository;
    }

    /**
     * Registra un nuevo docente en el sistema, validando sus datos.
     *
     * @param docente objeto docente a registrar
     * @return true si se registra correctamente
     * @throws ValidationException si los datos no son válidos
     */
    public boolean registerDocente(Docente docente) throws ValidationException {
        if (docente == null) {
            throw new ValidationException(List.of("El docente no puede ser nulo."));
        }

        List<String> errores = new ArrayList<>();

        // Validar departamento
        Departamento dep = docente.getDepartamento();
        if (dep == null) {
            errores.add("El departamento es obligatorio.");
        }

        // Validar nombre
        if (docente.getName() == null || docente.getName().trim().isEmpty()) {
            errores.add("El nombre es obligatorio.");
        }

        // Validar email institucional
        if (docente.getEmail() == null || !docente.getEmail().matches("^[A-Za-z0-9._%+-]+@unicauca\\.edu\\.co$")) {
            errores.add("El email debe ser institucional (@unicauca.edu.co).");
        }

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        return repository.save(docente);
    }

    /**
     * Lista todos los docentes registrados.
     *
     * @return lista de docentes
     */
    public List<Docente> listDocentes() {
        return repository.list();
    }
}

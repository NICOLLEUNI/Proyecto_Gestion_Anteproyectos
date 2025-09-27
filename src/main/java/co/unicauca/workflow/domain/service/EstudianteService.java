/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.unicauca.workflow.domain.service;

import co.unicauca.workflow.access.IEstudianteRepository;
import co.unicauca.workflow.domain.entities.Estudiante;
import co.unicauca.workflow.domain.entities.Programa;
import co.unicauca.workflow.domain.exceptions.ValidationException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author CRISTHINA TORRES
 */
public class EstudianteService {

    private final IEstudianteRepository repository;

    /**
     * Inyección de dependencias en el constructor.
     *
     * @param repository implementación concreta de IEstudianteRepository
     */
    public EstudianteService(IEstudianteRepository repository) {
        this.repository = repository;
    }

    /**
     * Registra un nuevo estudiante en el sistema, validando sus datos.
     *
     * @param estudiante objeto estudiante a registrar
     * @return true si se registra correctamente
     * @throws ValidationException si los datos no son válidos
     */
    public boolean registerEstudiante(Estudiante estudiante) throws ValidationException {
        if (estudiante == null) {
            throw new ValidationException(List.of("El estudiante no puede ser nulo."));
        }

        List<String> errores = new ArrayList<>();

        // Validar programa
        Programa programa = estudiante.getProgram();
        if (programa == null) {
            errores.add("El programa es obligatorio.");
        }

        // Validar nombre
        if (estudiante.getName() == null || estudiante.getName().trim().isEmpty()) {
            errores.add("El nombre es obligatorio.");
        }

        // Validar email institucional
        if (estudiante.getEmail() == null || !estudiante.getEmail().matches("^[A-Za-z0-9._%+-]+@unicauca\\.edu\\.co$")) {
            errores.add("El email debe ser institucional (@unicauca.edu.co).");
        }

        if (!errores.isEmpty()) {
            throw new ValidationException(errores);
        }

        return repository.save(estudiante);
    }

    /**
     * Lista todos los estudiantes registrados.
     *
     * @return lista de estudiantes
     */
    public List<Estudiante> listEstudiantes() {
        return repository.list();
    }

   
}

/*
 * Servicio para gestionar Departamentos
 */
package co.unicauca.workflow.domain.service;

import co.unicauca.workflow.access.IDepartamentoRepository;
import co.unicauca.workflow.domain.entities.Departamento;
import co.unicauca.workflow.domain.exceptions.ValidationException;

import java.util.List;

/**
 * Servicio de lógica de negocio para la entidad Departamento.
 * Se encarga de validar reglas antes de delegar en el repositorio.
 * 
 * @author CRISHTIN TORRES
 */
public class DepartamentoService {

    private IDepartamentoRepository repository;

    /**
     * Constructor con inyección de dependencias
     * 
     * @param repository implementación concreta del repositorio de departamentos
     */
    public DepartamentoService(IDepartamentoRepository repository) {
        this.repository = repository;
    }

    /**
     * Registra un nuevo departamento
     * 
     * @param departamento objeto departamento a registrar
     * @return true si fue guardado correctamente
     * @throws ValidationException si los campos no cumplen las reglas de negocio
     */
    public boolean createDepartamento(Departamento departamento) throws ValidationException {
        if (departamento == null) {
            throw new ValidationException(List.of("El departamento no puede ser nulo."));
        }

        // Valida con las reglas de negocio en la entidad
        departamento.validarCamposDepartamento();

        // Validación extra: nombre único (ejemplo)
        List<Departamento> existentes = repository.list();
        for (Departamento d : existentes) {
            if (d.getNombre().equalsIgnoreCase(departamento.getNombre())) {
                throw new ValidationException(List.of("Ya existe un departamento con este nombre."));
            }
        }

        return repository.save(departamento);
    }

   
    public List<Departamento> list() {
        return repository.list();
    }

    
    
}

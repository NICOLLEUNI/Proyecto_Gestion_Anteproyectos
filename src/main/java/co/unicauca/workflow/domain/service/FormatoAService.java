package co.unicauca.workflow.domain.service;

import co.unicauca.workflow.access.IFormatoARepository;
import co.unicauca.workflow.domain.entities.FormatoA;
import co.unicauca.workflow.domain.exceptions.ValidationException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FormatoAService {
    
    private final IFormatoARepository formatoArepository;
    
    private final List<String> lastValidationErrors = new ArrayList<>();

    public FormatoAService(IFormatoARepository formatoArepository) {
        this.formatoArepository = formatoArepository;
    }
    
    public List<String> getLastValidationErrors() {
        return new ArrayList<>(lastValidationErrors);
    }
    
    public boolean subirFormatoA(FormatoA formatoA) {
    lastValidationErrors.clear();

    if (formatoA == null) {
        lastValidationErrors.add("No se recibió el Formato A.");
        return false;
    }

    // Valores por defecto/normalizaciones mínimas
    if (formatoA.getCounter() <= 0) {
        formatoA.setCounter(1); // primer intento por defecto
    }
    if (formatoA.getState() == null || formatoA.getState().isBlank()) {
        formatoA.setState("entregado"); // estado inicial por defecto
    }
    if (formatoA.getDate() == null) {
        formatoA.setDate(LocalDate.now());
    }

    try {
        // 🔹 Ejecuta las validaciones de la entidad
        formatoA.validarCampos();

        // 🔹 Si no lanza excepción, se guarda en el repo
        boolean saved = formatoArepository.save(formatoA);
        if (!saved) {
            lastValidationErrors.add("Ocurrió un error al guardar el Formato A en la base de datos.");
        }
        return saved;

    } catch (ValidationException ex) {
        // 🔹 Capturamos los errores y los guardamos para la GUI
        lastValidationErrors.addAll(ex.getErrores());
        return false;
    }
}

    
    

   

}

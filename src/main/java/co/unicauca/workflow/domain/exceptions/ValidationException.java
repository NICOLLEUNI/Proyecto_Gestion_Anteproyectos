package co.unicauca.workflow.domain.exceptions;

import java.util.List;

public class ValidationException extends Exception {
    private final List<String> errores;

    public ValidationException(List<String> errores) {
        super(String.join("\n", errores)); // Concatenamos los errores en un solo String
        this.errores = errores;
    }

    public List<String> getErrores() {
        return errores;
    }
}

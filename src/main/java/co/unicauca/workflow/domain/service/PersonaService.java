/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.unicauca.workflow.domain.service;

import co.unicauca.workflow.access.IPersonaRepository;
import co.unicauca.workflow.domain.entities.Persona;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author User
 */
public class PersonaService {
    
    
    
    
    
    //crear el metodo de cerrar sesion
    
    //RICARDO - implementar los metodos de inicio y registro. 
    //Implementar el inicio de sesion con la clase persona y sus clases hijas 
    
    //implementar el registro de usuarios: docente, estudiante o coordinador
        private IPersonaRepository repository;

    /**
     * Inyección de dependencias en el constructor. Ya no conviene que el mismo
     * servicio cree un repositorio concreto
     *
     * @param repository una clase hija de IProductRepository
     */
    public PersonaService(IPersonaRepository personaRepository, IEstudianteRepository estudianteRepository,
                         IDocenteRepository docenteRepository, ICoordinadorRepository coordinadorRepository) {
        this.personaRepository = personaRepository;
        this.estudianteRepository = estudianteRepository;
        this.docenteRepository = docenteRepository;
        this.coordinadorRepository = coordinadorRepository;
        this.programaRepository = Factory.getProgramaRepository("default");
        this.departamentoRepository = Factory.getDepartamentoRepository("default");
        this.facultadRepository = Factory.getFacultadRepository("default");
    }

    /**
     * MÉTODO PRINCIPAL: Registra persona con roles múltiples
     */
    public boolean savePerson(String nombre, String apellidos, String celular, String email, 
                             String password, EnumSet<enumRol> roles, String nombrePrograma, 
                             String nombreDepartamento) {
        
        System.out.println("=== INICIO REGISTRO ===");
        System.out.println("Nombre: " + nombre + " " + apellidos);
        System.out.println("Email: " + email);
        System.out.println("Roles: " + roles);
        System.out.println("Programa: " + nombrePrograma);
        System.out.println("Departamento: " + nombreDepartamento);
        
        try {
            // PASO 1: Obtener/crear la facultad FIET única
            Facultad fiet = obtenerFacultadFIET();
            if (fiet == null) {
                System.err.println("ERROR: No se pudo obtener la facultad FIET");
                return false;
            }
            System.out.println("Facultad FIET obtenida con ID: " + fiet.getCodFacultad());

            // PASO 2: Crear Persona con roles
            String hashedPassword = hashPassword(password);
            if (hashedPassword == null) {
                System.err.println("ERROR: No se pudo hashear la contraseña");
                return false;
            }

            Persona persona = new Persona();
            persona.setName(nombre.trim());
            persona.setLastname(apellidos.trim());
            persona.setPhone(celular);
            persona.setEmail(email.trim().toLowerCase(Locale.ROOT));
            persona.setPassword(hashedPassword);
            persona.setRoles(roles);

            if (!personaRepository.save(persona)) {
                System.err.println("ERROR: No se pudo guardar la persona en PersonaRepository");
                return false;
            }
            System.out.println("Persona guardada con ID: " + persona.getIdUsuario());

            // PASO 3: Guardar en repositorios específicos según roles
            boolean exito = true;

            if (roles.contains(enumRol.ESTUDIANTE)) {
                exito &= registrarEstudiante(persona, nombrePrograma, fiet);
            }

            if (roles.contains(enumRol.DOCENTE)) {
                exito &= registrarDocente(persona, nombreDepartamento, fiet);
            }

            if (roles.contains(enumRol.COORDINADOR)) {
                exito &= registrarCoordinador(persona, nombreDepartamento, fiet);
            }

            if (exito) {
                System.out.println("✅ REGISTRO COMPLETADO EXITOSAMENTE");
            } else {
                System.err.println("⚠️ REGISTRO CON ERRORES PARCIALES");
            }

            return exito;

        } catch (Exception e) {
            System.err.println("ERROR INESPERADO en savePerson: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtiene la facultad FIET única del sistema
     */
    protected Facultad obtenerFacultadFIET() {
        try {
            System.out.println("Buscando facultad FIET...");
            
            // Buscar FIET existente
            List<Facultad> facultades = facultadRepository.list();
            for (Facultad f : facultades) {
                if ("FIET".equals(f.getNombre())) {
                    System.out.println("Facultad FIET encontrada con ID: " + f.getCodFacultad());
                    return f;
                }
            }

            // FIET no existe, crearla
            System.out.println("Facultad FIET no existe, creando...");
            Facultad nuevaFIET = new Facultad("FIET");
            
            if (facultadRepository.save(nuevaFIET)) {
                System.out.println("Facultad FIET creada con ID: " + nuevaFIET.getCodFacultad());
                return nuevaFIET;
            } else {
                System.err.println("ERROR: No se pudo guardar la facultad FIET");
                return null;
            }

        } catch (ValidationException e) {
            System.err.println("ERROR de validación al crear FIET: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("ERROR inesperado al obtener FIET: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Registra estudiante con programa
     */
    private boolean registrarEstudiante(Persona persona, String nombrePrograma, Facultad fiet) {
        try {
            System.out.println("Registrando estudiante con programa: " + nombrePrograma);
            
            // Obtener/crear departamento Sistemas para el programa
            Departamento sistemas = obtenerDepartamento("Sistemas", fiet);
            if (sistemas == null) {
                System.err.println("ERROR: No se pudo obtener departamento Sistemas");
                return false;
            }

            // Obtener/crear programa
            Programa programa = obtenerPrograma(nombrePrograma, sistemas);
            if (programa == null) {
                System.err.println("ERROR: No se pudo obtener programa " + nombrePrograma);
                return false;
            }

            // Crear y guardar estudiante
            Estudiante estudiante = new Estudiante(
                persona.getIdUsuario(), programa, persona.getName(),
                persona.getLastname(), persona.getPhone(),
                persona.getEmail(), persona.getPassword()
            );

            boolean guardado = estudianteRepository.save(estudiante);
            if (guardado) {
                System.out.println("✅ Estudiante guardado exitosamente");
            } else {
                System.err.println("❌ Error guardando estudiante");
            }
            return guardado;

        } catch (ValidationException e) {
            System.err.println("ERROR de validación en estudiante: " + e.getMessage());
            return false;
        }
    }

    /**
     * Registra docente con departamento
     */
    private boolean registrarDocente(Persona persona, String nombreDepartamento, Facultad fiet) {
        try {
            System.out.println("Registrando docente en departamento: " + nombreDepartamento);
            
            Departamento departamento = obtenerDepartamento(nombreDepartamento, fiet);
            if (departamento == null) {
                System.err.println("ERROR: No se pudo obtener departamento " + nombreDepartamento);
                return false;
            }

            Docente docente = new Docente(
                persona.getIdUsuario(), departamento, persona.getName(),
                persona.getLastname(), persona.getPhone(),
                persona.getEmail(), persona.getPassword()
            );

            boolean guardado = docenteRepository.save(docente);
            if (guardado) {
                System.out.println("✅ Docente guardado exitosamente");
            } else {
                System.err.println("❌ Error guardando docente");
            }
            return guardado;

        } catch (ValidationException e) {
            System.err.println("ERROR de validación en docente: " + e.getMessage());
            return false;
        }
    }

    /**
     * Registra coordinador con departamento
     */
    private boolean registrarCoordinador(Persona persona, String nombreDepartamento, Facultad fiet) {
        try {
            System.out.println("Registrando coordinador en departamento: " + nombreDepartamento);
            
            Departamento departamento = obtenerDepartamento(nombreDepartamento, fiet);
            if (departamento == null) {
                System.err.println("ERROR: No se pudo obtener departamento " + nombreDepartamento);
                return false;
            }

            Coordinador coordinador = new Coordinador(
                persona.getIdUsuario(), departamento, persona.getName(),
                persona.getLastname(), persona.getPhone(),
                persona.getEmail(), persona.getPassword()
            );

            boolean guardado = coordinadorRepository.save(coordinador);
            if (guardado) {
                System.out.println("✅ Coordinador guardado exitosamente");
            } else {
                System.err.println("❌ Error guardando coordinador");
            }
            return guardado;

        } catch (ValidationException e) {
            System.err.println("ERROR de validación en coordinador: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene o crea departamento
     */
    protected Departamento obtenerDepartamento(String nombreDepartamento, Facultad fiet) {
        try {
            // Buscar existente
            List<Departamento> departamentos = departamentoRepository.list();
            for (Departamento d : departamentos) {
                if (nombreDepartamento.equals(d.getNombre())) {
                    System.out.println("Departamento existente: " + nombreDepartamento);
                    return d;
                }
            }

            // Crear nuevo
            System.out.println("Creando departamento: " + nombreDepartamento);
            Departamento nuevo = new Departamento(nombreDepartamento, fiet);
            
            if (departamentoRepository.save(nuevo)) {
                System.out.println("Departamento creado con ID: " + nuevo.getCodDepartamento());
                return nuevo;
            }

        } catch (ValidationException e) {
            System.err.println("ERROR validando departamento: " + e.getMessage());
        }
        return null;
    }

    /**
     * Obtiene o crea programa
     */
    protected Programa obtenerPrograma(String nombrePrograma, Departamento departamento) {
        try {
            // Buscar existente
            List<Programa> programas = programaRepository.list();
            for (Programa p : programas) {
                if (nombrePrograma.equals(p.getNombrePrograma())) {
                    System.out.println("Programa existente: " + nombrePrograma);
                    return p;
                }
            }

            // Crear nuevo
            System.out.println("Creando programa: " + nombrePrograma);
            Programa nuevo = new Programa(nombrePrograma, departamento);
            
            if (programaRepository.save(nuevo)) {
                System.out.println("Programa creado con ID: " + nuevo.getCodPrograma());
                return nuevo;
            }

        } catch (ValidationException e) {
            System.err.println("ERROR validando programa: " + e.getMessage());
        }
        return null;
    }

    // === MÉTODOS DE UTILIDAD ===

    public String hashPassword(String password) {
        if (password == null) return null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 no encontrado", e);
        }
    }

    
    public boolean validatePassword(String password) {
    if (password == null) return false;
    // min 6, al menos una minúscula, una mayúscula, un dígito y un caracter especial
    String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d])[A-Za-z\\d\\W_]{6,}$";
    return password.matches(regex);
    }

    
    
    /**
     * Valida que el email pertenezca al dominio institucional.
     */
    public boolean validateEmail(String email) {
        String regex = "^[A-Za-z0-9._%+-]+@unicauca\\.edu\\.co$";
        return email != null && email.matches(regex);
    }
    
    public Persona authenticateUser(String email, String password) {
    if (email == null || email.isBlank() || password == null || password.isBlank()) {
        return null;
    }

    // Normalizar email
    String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);

    // Hashear la contraseña ingresada
    String hashedPassword = hashPassword(password);
    if (hashedPassword == null) {
        return null;
    }

    // Obtener todas las personas desde el repositorio
    List<Persona> personas = repository.list();
    for (Persona p : personas) {
        if (p.getEmail() != null
                && p.getEmail().equalsIgnoreCase(normalizedEmail)
                && p.getPassword().equals(hashedPassword)) {
            return p; // 👈 Retorna el objeto completo (puede ser Estudiante, Docente, Coordinador)
        }
    }
    return null;
}

/**
 * Método auxiliar para hashear la contraseña.
 * Puedes usar el mismo algoritmo que en UserService para mantener consistencia.
 */
private String hashPassword(String password) {
    try {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hashedBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    } catch (NoSuchAlgorithmException e) {
        return null;
    }
}

}

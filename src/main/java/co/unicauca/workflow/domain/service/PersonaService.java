package co.unicauca.workflow.domain.service;

import java.util.Locale;
import co.unicauca.workflow.domain.entities.enumRol;
import co.unicauca.workflow.domain.entities.Persona;
import co.unicauca.workflow.domain.entities.Estudiante;
import co.unicauca.workflow.domain.entities.Docente;
import co.unicauca.workflow.domain.entities.Coordinador;
import co.unicauca.workflow.domain.entities.Programa;
import co.unicauca.workflow.domain.entities.Departamento;
import co.unicauca.workflow.domain.entities.Facultad;
import co.unicauca.workflow.domain.exceptions.ValidationException;
import co.unicauca.workflow.access.IPersonaRepository;
import co.unicauca.workflow.access.IEstudianteRepository;
import co.unicauca.workflow.access.IDocenteRepository;
import co.unicauca.workflow.access.ICoordinadorRepository;
import co.unicauca.workflow.access.IProgramaRepository;
import co.unicauca.workflow.access.IDepartamentoRepository;
import co.unicauca.workflow.access.IFacultadRepository;
import co.unicauca.workflow.access.Factory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.EnumSet;
import java.util.List;

public class PersonaService {

    // Dependencias hacia abstracciones
    private final IPersonaRepository personaRepository;
    private final IEstudianteRepository estudianteRepository;
    private final IDocenteRepository docenteRepository;
    private final ICoordinadorRepository coordinadorRepository;
    private final IProgramaRepository programaRepository;
    private final IDepartamentoRepository departamentoRepository;
    private final IFacultadRepository facultadRepository;

    /**
     * Constructor usando Factory pattern
     */
    
    
    
    public PersonaService() {
        this.personaRepository = Factory.getPersonaRepository("default");
        this.estudianteRepository = Factory.getEstudianteRepository("default");
        this.docenteRepository = Factory.getDocenteRepository("default");
        this.coordinadorRepository = Factory.getCoordinadorRepository("default");
        this.programaRepository = Factory.getProgramaRepository("default");
        this.departamentoRepository = Factory.getDepartamentoRepository("default");
        this.facultadRepository = Factory.getFacultadRepository("default");
    
      // DEBUG TEMPORAL - REMOVER DESPUÉS
    System.out.println("=== DEBUG REPOSITORIOS ===");
    System.out.println("PersonaRepository: " + (this.personaRepository != null ? "OK" : "NULL"));
    System.out.println("FacultadRepository: " + (this.facultadRepository != null ? "OK" : "NULL"));
    System.out.println("EstudianteRepository: " + (this.estudianteRepository != null ? "OK" : "NULL"));
    System.out.println("DocenteRepository: " + (this.docenteRepository != null ? "OK" : "NULL"));
    System.out.println("CoordinadorRepository: " + (this.coordinadorRepository != null ? "OK" : "NULL"));
    
    
    
    }

    /**
     * Constructor para testing con inyección manual
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
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z\\d])[A-Za-z\\d\\W_]{6,}$";
        return password.matches(regex);
    }

    public boolean validateEmail(String email) {
        if (email == null) return false;
        String regex = "^[A-Za-z0-9._%+-]+@unicauca\\.edu\\.co$";
        return email.matches(regex);
    }

    public boolean personExists(String email) {
        if (email == null || email.isBlank()) return false;
        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);
        List<Persona> personas = personaRepository.list();
        for (Persona p : personas) {
            if (p.getEmail() != null && p.getEmail().equals(normalizedEmail)) {
                return true;
            }
        }
        return false;
    }

    public Persona authenticatePerson(String email, String password) {
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            return null;
        }
        String normalizedEmail = email.trim().toLowerCase(Locale.ROOT);
        String hashedPassword = hashPassword(password);
        if (hashedPassword == null) return null;

        List<Persona> personas = personaRepository.list();
        for (Persona p : personas) {
            if (p.getEmail() != null && p.getEmail().equals(normalizedEmail) &&
                p.getPassword().equals(hashedPassword)) {
                return p;
            }
        }
        return null;
    }
}
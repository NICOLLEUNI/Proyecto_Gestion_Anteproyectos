package co.unicauca.workflow;


import co.unicauca.workflow.access.DocenteRepository;
import co.unicauca.workflow.access.Factory;
import co.unicauca.workflow.access.ICoordinadorRepository;
import co.unicauca.workflow.access.IPersonaRepository;
import co.unicauca.workflow.domain.entities.Departamento;
import co.unicauca.workflow.domain.entities.Docente;
import co.unicauca.workflow.domain.entities.Estudiante;
import co.unicauca.workflow.domain.entities.Facultad;
import co.unicauca.workflow.domain.entities.FormatoA;
import co.unicauca.workflow.domain.entities.FormatoAVersion;
import co.unicauca.workflow.domain.entities.Persona;
import co.unicauca.workflow.domain.entities.Programa;
import co.unicauca.workflow.domain.entities.User;
import co.unicauca.workflow.domain.entities.enumEstado;
import co.unicauca.workflow.domain.entities.enumModalidad;
import co.unicauca.workflow.domain.entities.enumProgram;
import co.unicauca.workflow.domain.entities.enumRol;
import static co.unicauca.workflow.domain.entities.enumRol.DOCENTE;
import co.unicauca.workflow.domain.service.PersonaService;
import co.unicauca.workflow.presentation.GUILogin;
import co.unicauca.workflow.presentation.GUIMenuPrincipal;
import java.util.EnumSet;
import co.unicauca.workflow.domain.exceptions.ValidationException;
import co.unicauca.workflow.presentation.GUIMenuCoord;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class Taller02PrincipiosSolid {

    public static void main(String[] args) {
        
        IPersonaRepository repository = Factory.getInstance().getPersonaRepository("default");
        PersonaService service = new PersonaService(repository);

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                //new GUILogin().setVisible(true);
             //este es un usuario quemado para probar el cerrar sesion;   
           
            Persona user = new Persona();
           
            user.setName("Carlos");
            user.setIdUsuario(1);
            user.setLastname("torres");
            user.setPassword("secrete");
            user.setPhone("3145339725");
            //user.setProgram(enumProgram.INGENIERIA_SISTEMAS);
            user.setRoles(EnumSet.of(enumRol.ESTUDIANTE,enumRol.COORDINADOR));
            
            
                
   
             


                // Abrir la ventana de login como punto de entrada de la aplicación
                new GUILogin().setVisible(true);
            }
        });
    }
}

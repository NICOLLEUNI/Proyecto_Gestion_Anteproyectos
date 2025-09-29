package co.unicauca.workflow;


import co.unicauca.workflow.access.Factory;
import co.unicauca.workflow.access.IFormatoARepository;
import co.unicauca.workflow.access.IPersonaRepository;
import co.unicauca.workflow.domain.entities.Persona;
import co.unicauca.workflow.domain.entities.enumRol;
import co.unicauca.workflow.domain.service.PersonaService;
import co.unicauca.workflow.presentation.GUILogin;
import java.util.EnumSet;
import co.unicauca.workflow.domain.service.FormatoAService;
import co.unicauca.workflow.presentation.views.GraficoBarras;
import co.unicauca.workflow.presentation.views.GraficoPastel;

/**
 *
 * @author User
 */
public class Taller02PrincipiosSolid {

    public static void main(String[] args) {
        
        

        
        IPersonaRepository repository = Factory.getInstance().getPersonaRepository("default");
        PersonaService service = new PersonaService();
        IFormatoARepository repoFomratoA = Factory.getInstance().getFormatoARepository("default");
        FormatoAService serviceFormato = new FormatoAService(repoFomratoA);
        
        /*
        //primero le asignamos el dipo de BDD a el repositorio
        IEstudianteRepository repoEstudiante = Factory.getInstance().getEstudianteRepository("default");
        //luego, le pasamos el repo creado a la clase donde lo vamos a usar
        EstudianteService serviceEstudiante = new EstudianteService(repoEstudiante);
        */
            GraficoBarras guiObserver1 = new GraficoBarras(serviceFormato);
       serviceFormato.addObserver(guiObserver1);

         GraficoPastel guiObserver2 = new GraficoPastel(serviceFormato);
       serviceFormato.addObserver(guiObserver1);
       
       
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

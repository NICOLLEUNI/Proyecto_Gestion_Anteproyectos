/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.unicauca.workflow;



import co.unicauca.workflow.access.FormatoARepository;
import co.unicauca.workflow.access.DocenteRepository;
import co.unicauca.workflow.access.Factory;
import co.unicauca.workflow.access.ICoordinadorRepository;
import co.unicauca.workflow.access.IFormatoARepository;
import co.unicauca.workflow.access.IPersonaRepository;
import co.unicauca.workflow.access.Factory;
import co.unicauca.workflow.domain.entities.Docente;
import co.unicauca.workflow.domain.entities.Estudiante;
import co.unicauca.workflow.domain.entities.FormatoA;
import co.unicauca.workflow.domain.entities.Persona;
import co.unicauca.workflow.domain.entities.enumRol;

import co.unicauca.workflow.domain.exceptions.ValidationException;
import co.unicauca.workflow.domain.service.FormatoAService;
import co.unicauca.workflow.presentation.GUIMenuCoord;
import co.unicauca.workflow.presentation.GUIMenuPrincipal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Taller02PrincipiosSolid {

    public static void main(String[] args) {
        
        IPersonaRepository repository = Factory.getInstance().getPersonaRepository("default");
        PersonaService service = new PersonaService();
        IFormatoARepository repoFomratoA = Factory.getInstance().getFormatoARepository("default");
        FormatoAService serviceFormato = new FormatoAService(repoFomratoA);

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // Interfaz de login
                //new GUIL<<<<<<< Updated upstream
             //este es un usuario quemado para probar el cerrar sesion;   
           
            Persona user = new Persona();
           
            user.setName("Carlos");
            user.setIdUsuario(1);
            user.setLastname("torres");
            user.setPassword("secrete");
            user.setPhone("3145339725");
            //user.setProgram(enumProgram.INGENIERIA_SISTEMAS);
            user.setRoles(EnumSet.of(enumRol.ESTUDIANTE,enumRol.COORDINADOR));
            
            
                
              new GUIMenuPrincipal(user).setVisible(true);
             



                
                // 🔹 Crear un estudiante de prueba
                Estudiante estudiante = new Estudiante();
                estudiante.setIdUsuario(1);
                estudiante.setName("Carlos");
                estudiante.setLastname("Torres");
                estudiante.setPhone("3145339725");
                estudiante.setEmail("carlos@unicauca.edu.co");
                estudiante.setPassword("secrete");
                estudiante.setRoles(EnumSet.of(enumRol.ESTUDIANTE));
                estudiante.setRoles(EnumSet.of(enumRol.ESTUDIANTE, enumRol.DOCENTE));
                
                new GUIMenuPrincipal(estudiante).setVisible(true);

                // 🔹 Crear un repositorio de prueba e insertar formatos
                IFormatoARepository repo = new FormatoARepository();
                Docente doct = new Docente();
                
                FormatoA formato1 = new FormatoA();
                formato1.setTitle("Proyecto IoT");
                formato1.setProjectManager(doct);
                formato1.setEstudiantes(List.of(estudiante));

                FormatoA formato2 = new FormatoA();
                formato2.setTitle("Proyecto IA");
                formato2.setProjectManager(doct);
                formato2.setEstudiantes(List.of(estudiante)); // también del mismo estudiante

                repo.save(formato1);
                repo.save(formato2);

                // 🔹 Usar el service para filtrar los formatos del estudiante
                FormatoAService service = new FormatoAService(repo);
                List<FormatoA> formatosEst = service.consultarFormatoAEstudiante(estudiante);

               
                
                

                // Si quieres, aquí puedes abrir el menú principal con el usuario
                // new GUIMenuPrincipal(estudiante).setVisible(true);

            }
        });
    }
}

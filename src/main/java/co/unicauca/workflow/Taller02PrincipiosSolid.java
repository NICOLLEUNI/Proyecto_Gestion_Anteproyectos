/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package co.unicauca.workflow;


import co.unicauca.workflow.access.Factory;
import co.unicauca.workflow.access.ICoordinadorRepository;
import co.unicauca.workflow.access.IPersonaRepository;
import co.unicauca.workflow.domain.entities.Estudiante;
import co.unicauca.workflow.domain.entities.Persona;
import co.unicauca.workflow.domain.entities.User;
import co.unicauca.workflow.domain.entities.enumProgram;
import co.unicauca.workflow.domain.entities.enumRol;
import static co.unicauca.workflow.domain.entities.enumRol.DOCENTE;
import co.unicauca.workflow.domain.service.PersonaService;
import co.unicauca.workflow.presentation.GUILogin;
import co.unicauca.workflow.presentation.GUIMenuDocente;
import co.unicauca.workflow.presentation.GUIMenuPrincipal;
import java.util.EnumSet;

/**
 *
 * @author User
 */
public class Taller02PrincipiosSolid {

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
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
            user.setRoles(EnumSet.of(enumRol.ESTUDIANTE,enumRol.DOCENTE));
                
              new GUIMenuPrincipal(user).setVisible(true);
            }
        });
    }
    
    //Se deben instanciar cada uno de los repositorios usando el metodo creado en la factory
    
}
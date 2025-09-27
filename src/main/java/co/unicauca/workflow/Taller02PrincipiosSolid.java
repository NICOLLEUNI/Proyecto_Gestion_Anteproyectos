package co.unicauca.workflow;

import co.unicauca.workflow.domain.entities.Persona;
import co.unicauca.workflow.domain.entities.enumRol;
import co.unicauca.workflow.presentation.GUILogin;
import co.unicauca.workflow.presentation.GUIMenuPrincipal;
import java.util.EnumSet;

/**
 *
 * @author User
 */
public class Taller02PrincipiosSolid {

    public static void main(String[] args) {
        // Ejecutar en el hilo de eventos de Swing
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Abrir la ventana de login como punto de entrada de la aplicación
                new GUILogin().setVisible(true);
            }
        });
    }
}

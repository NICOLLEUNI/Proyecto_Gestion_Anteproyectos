/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package co.unicauca.workflow;


import co.unicauca.workflow.presentation.GUILogin;

/**
 *
 * @author User
 */
public class Taller02PrincipiosSolid {

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUILogin().setVisible(true);
            }
        });
    }
    
    //Se deben instanciar cada uno de los repositorios usando el metodo creado en la factory
    
}
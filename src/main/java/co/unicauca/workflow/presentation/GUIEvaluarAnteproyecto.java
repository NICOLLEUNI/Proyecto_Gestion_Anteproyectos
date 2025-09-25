/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package co.unicauca.workflow.presentation;

//agregar la funcionalidad en los 3 puntos de "volver a la pestaña anterior"
//añadir logica observer que se va a manejar con una capa de estadisticas 

import co.unicauca.workflow.access.FormatoARepository;
import co.unicauca.workflow.access.IFormatoARepository;
import co.unicauca.workflow.domain.entities.Departamento;
import co.unicauca.workflow.domain.entities.Docente;
import co.unicauca.workflow.domain.entities.Estudiante;
import co.unicauca.workflow.domain.entities.Facultad;
import co.unicauca.workflow.domain.entities.FormatoA;
import co.unicauca.workflow.domain.entities.Persona;
import co.unicauca.workflow.domain.entities.Programa;
import co.unicauca.workflow.domain.entities.User;
import co.unicauca.workflow.domain.entities.enumModalidad;
import co.unicauca.workflow.domain.exceptions.ValidationException;
import co.unicauca.workflow.presentation.views.Observaciones;
import co.unicauca.workflow.presentation.views.SubirFormatoA;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMTMaterialLighterIJTheme;
import java.awt.BorderLayout;
import java.time.LocalDate;
import java.util.ArrayList;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author User
 */
public class GUIEvaluarAnteproyecto extends javax.swing.JFrame {

    /**
     * Creates new form GUIEvaluarAnteproyecto
     */
     private static Persona personaLogueado;
     private List<FormatoA> listaFormateada = new ArrayList<>();
    public GUIEvaluarAnteproyecto(Persona logueado) throws ValidationException {
         this.personaLogueado=logueado;
        initComponents();
   initContent();

       cargarDatos();
 
 
  
    }
    
   private void cargarDatos() throws ValidationException {
    // 1. Crear Facultad, Departamento y Programa
    Facultad fac = new Facultad("Ingeniería");
    fac.setCodFacultad(10);

    Departamento dep = new Departamento("Sistemas", fac);
    dep.setCodDepartamento(20);

    Programa prog = new Programa(30, "Software", dep);

    // 2. Crear Estudiantes
    Estudiante e1 = new Estudiante(1, prog, "Ana", "Gómez", "12345", "ana@mail.com", "clave1");
    Estudiante e2 = new Estudiante(2, prog, "Luis", "Pérez", "67890", "luis@mail.com", "clave2");
    List<Estudiante> estudiantes = new ArrayList<>();
    estudiantes.add(e1);
    estudiantes.add(e2);

    // 3. Crear Docentes
    Docente director = new Docente(3, dep, "Carlos", "Torres", "111111", "carlos@unicauca.edu.co", "pass123");
    Docente codirector = new Docente(4, dep, "María", "López", "222222", "maria@unicauca.edu.co", "pass456");

    // 4. Crear FormatosA
    FormatoA formato1 = new FormatoA(
            100,
            "Sistema de Gestión de Tareas",
            enumModalidad.INVESTIGACION,
            director,
            codirector,
            LocalDate.now(),
            "Crear un sistema de gestión de tareas para estudiantes",
            "Implementar login, tareas, reportes",
             "C:/Users/User/Desktop/Taller02/Taller02_SOLID/archivosPDF/Extracto.pdf",
            null,
            estudiantes,
            0,
            "Sin observaciones"
    );

    FormatoA formato2 = new FormatoA(
            101,
            "App de Seguimiento Académico",
            enumModalidad.INVESTIGACION,
            director,
            codirector,
            LocalDate.now(),
            "Monitorear notas y asistencias",
            "Diseñar interfaz móvil",
            "C:\\Users\\User\\Desktop\\Escritorio\\Taller02\\Taller02_SOLID\\archivosPDF\\Extracto.pdf",
            null,
            estudiantes,
            0,
            "Observaciones iniciales"
    );

    // 5. Guardar en atributo global
    listaFormateada.clear();
    listaFormateada.add(formato1);
    listaFormateada.add(formato2);

    // 6. Cargar JTable
    String[] columnas = {"ID", "Título", "Estado"};
    DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

    for (FormatoA f : listaFormateada) {
        Object[] fila = {
            f.getId(),
            f.getTitle(),
            f.getState() != null ? f.getState() : "Entregado"
        };
        modelo.addRow(fila);
    }

    jTable1.setModel(modelo);
}

    
  
    
/*private void cargarDatos() {
    
     IFormatoARepository repo = new FormatoARepository();
    List<FormatoA> lista = repo.list();

    String[] columnas = {"ID",  "Titulo", "Estado"};
    DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

    for (FormatoA f : lista) {
        Object[] fila = {
            f.getId(),
            f.getTitle(),
            f.getState() != null ? f.getState() : "Entregado", // Estado real si existe
        };
        modelo.addRow(fila);
    }

    jTable1.setModel(modelo);
}*/

     private void initStyles(){
     
     }
     private void showJPanel(JPanel pl){
     pl.setSize(533,371);
     pl.setLocation(0, 0);
     
     Contenido.removeAll();
     Contenido.add(pl,BorderLayout.CENTER);
     Contenido.revalidate();
     Contenido.repaint(); 
         
     }
     private void initContent(){

    // Cargamos por defecto el panel de observaciones vacío
    

    // Listener para tabla
    /*
    -----------------------------------------
            PARA TABLA REPOSITORY
    ------------------------------------------                
    jTable1.getSelectionModel().addListSelectionListener(e -> {
        if (!e.getValueIsAdjusting() && jTable1.getSelectedRow() != -1) {
            int fila = jTable1.getSelectedRow();
            int id = (int) jTable1.getValueAt(fila, 0); // ID está en la columna 0

            // Buscar el FormatoA desde repo
            IFormatoARepository repo = new FormatoARepository();
            FormatoA formato = repo.findById(id); // ⚠️ Necesitas implementar este método en FormatoARepository

            if (formato != null) {
                Observaciones panelObs = new Observaciones();
                panelObs.setFormatoA(formato);
                showJPanel(panelObs);
            }
        }
    });
*/
     // Listener para tabla con datos quemados
    jTable1.getSelectionModel().addListSelectionListener(e -> {
        if (!e.getValueIsAdjusting() && jTable1.getSelectedRow() != -1) {
            int fila = jTable1.getSelectedRow();
            int id = (int) jTable1.getValueAt(fila, 0);

            // Buscar FormatoA en lista quemada
            FormatoA formatoSeleccionado = listaFormateada.stream()
                    .filter(f -> f.getId() == id)
                    .findFirst()
                    .orElse(null);

            if (formatoSeleccionado != null) {
                Observaciones panelObs = new Observaciones();
                panelObs.setFormatoA(formatoSeleccionado);
                showJPanel(panelObs);
            }
        }
    });
     }
     
     
     
     
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        Menu = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btVolver = new javax.swing.JButton();
        Icon = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        btEvaluar = new javax.swing.JButton();
        Contenido = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        Menu.setBackground(new java.awt.Color(26, 55, 171));
        Menu.setPreferredSize(new java.awt.Dimension(226, 510));
        Menu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Roboto Medium", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("PROYECTOS");
        Menu.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(73, 14, 206, 28));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Titulo", "Estado"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        Menu.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 54, 353, 450));

        btVolver.setBackground(new java.awt.Color(102, 102, 255));
        btVolver.setFont(new java.awt.Font("Wide Latin", 1, 24)); // NOI18N
        btVolver.setForeground(new java.awt.Color(255, 255, 255));
        btVolver.setText("<");
        btVolver.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btVolver.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btVolverMouseClicked(evt);
            }
        });
        Menu.add(btVolver, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 40, -1));

        Icon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/co/unicauca/workflow/presentation/images/LogoPequeño.png"))); // NOI18N
        Icon.setText("jLabel1");

        jLabel1.setFont(new java.awt.Font("Roboto Medium", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("EVALUAR ANTEPROYECTO");

        btEvaluar.setBackground(new java.awt.Color(65, 55, 171));
        btEvaluar.setFont(new java.awt.Font("Roboto Medium", 1, 24)); // NOI18N
        btEvaluar.setForeground(new java.awt.Color(255, 255, 255));
        btEvaluar.setText("EVALUAR");
        btEvaluar.setBorderPainted(false);
        btEvaluar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btEvaluar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btEvaluarMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout ContenidoLayout = new javax.swing.GroupLayout(Contenido);
        Contenido.setLayout(ContenidoLayout);
        ContenidoLayout.setHorizontalGroup(
            ContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        ContenidoLayout.setVerticalGroup(
            ContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 371, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(Menu, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(115, 115, 115)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(103, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(Icon, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btEvaluar, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Contenido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Menu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(Contenido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Icon)
                    .addComponent(btEvaluar, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btVolverMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btVolverMouseClicked
        GUIMenuCoord ventanaCoord = new GUIMenuCoord(personaLogueado); // Opcional: mostrar un mensaje al usuario
        ventanaCoord.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btVolverMouseClicked

    private void btEvaluarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btEvaluarMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btEvaluarMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        FlatMTMaterialLighterIJTheme.setup();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new GUIEvaluarAnteproyecto(personaLogueado).setVisible(true);
                } catch (ValidationException ex) {
                    Logger.getLogger(GUIEvaluarAnteproyecto.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Contenido;
    private javax.swing.JLabel Icon;
    private javax.swing.JPanel Menu;
    private javax.swing.JButton btEvaluar;
    private javax.swing.JButton btVolver;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}

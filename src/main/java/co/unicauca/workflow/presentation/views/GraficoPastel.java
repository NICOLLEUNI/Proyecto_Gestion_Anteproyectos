/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package co.unicauca.workflow.presentation.views;

import co.unicauca.workflow.domain.entities.FormatoA;
import co.unicauca.workflow.domain.service.FormatoAService;
import co.unicauca.workflow.infa.Observer;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMTMaterialLighterIJTheme;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author User
 */
public class GraficoPastel extends javax.swing.JPanel implements Observer {

    private DefaultPieDataset dataset;
    private JFreeChart chart;
    private ChartPanel chartPanel;

    private FormatoAService formatoAService;

    public GraficoPastel(FormatoAService formatoAService) {
        // Inicializamos el theme
        FlatMTMaterialLighterIJTheme.setup();
        this.formatoAService = formatoAService;

        initComponents();
        initGrafico();
        cargarDatosIniciales(); // opcional para que cargue al inicio
    }

    private void initGrafico() {
        setLayout(new BorderLayout());
        dataset = new DefaultPieDataset();
        chart = ChartFactory.createPieChart(
                "Estados de Proyectos", dataset, true, true, false);

        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(300, 300)); // tamaño 300x300
        add(chartPanel, BorderLayout.CENTER);
    }

    private void cargarDatosIniciales() {
        // Cargamos datos iniciales desde el servicio
        List<FormatoA> lista = formatoAService.listFormatoA();
        actualizarDataset(lista);
    }

    private void actualizarDataset(List<FormatoA> lista) {
    int entregados = 0;
    int aprobados = 0;
    int rechazados = 0;

    for (FormatoA f : lista) {
        // Aseguramos que el estado no sea null
        co.unicauca.workflow.domain.entities.enumEstado estado = f.getState();

        if (estado == null) {
            entregados++;
            continue;
        }

        switch (estado) {
            case APROBADO:
                aprobados++;
                break;
            case RECHAZADO:
                rechazados++;
                break;
            case ENTREGADO:
                entregados++;
                break;
            // Ignoramos RECHAZADO_DEFINITIVAMENTE
            default:
                entregados++;
                break;
        }
    }

    // Actualizamos dataset del gráfico
    dataset.setValue("Entregados", entregados);
    dataset.setValue("Aprobados", aprobados);
    dataset.setValue("Rechazados", rechazados);
}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables

    @Override
    public void update(Object o) {
              if (o instanceof List<?>) {
            List<?> lista = (List<?>) o;
            if (!lista.isEmpty() && lista.get(0) instanceof FormatoA) {
                actualizarDataset((List<FormatoA>) lista);
            }
        } else {
            // Si no pasan lista, la cargamos directamente del service
            List<FormatoA> lista = formatoAService.listFormatoA();
            actualizarDataset(lista);
        }
    }
    }


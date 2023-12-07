/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managecoffeechain;

import java.awt.BorderLayout;
import java.awt.Color;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import services.OrderService;

/**
 *
 * @author thangphan
 */
public class AdminDashboardPage2 extends javax.swing.JFrame {

	/**
	 * Creates new form AdminDashboardPage2
	 */
	public AdminDashboardPage2() {
		initComponents();
		showPieChart();
	}
	
	public void showPieChart(){
	      OrderService orderService = new OrderService();
	      
	      //create dataset
	      DefaultPieDataset barDataset = new DefaultPieDataset( );
	      barDataset.setValue( "Thanh toán bằng tiền mặt" , new Double( orderService.getTotalAmountByPaymentMethod("cash") ));  
	      barDataset.setValue( "Thanh toán bằng momo" , new Double( orderService.getTotalAmountByPaymentMethod("momo") ) );   

	      //create chart
	       JFreeChart piechart = ChartFactory.createPieChart("mobile sales",barDataset, false,true,false);//explain

		PiePlot piePlot =(PiePlot) piechart.getPlot();

	       //changing pie chart blocks colors
	       piePlot.setSectionPaint("Thanh toán bằng tiền mặt", new Color(255,255,102));
		piePlot.setSectionPaint("Thanh toán bằng momo", new Color(102,255,102));
//		piePlot.setSectionPaint("MotoG", new Color(255,102,153));
//		piePlot.setSectionPaint("Nokia Lumia", new Color(0,204,204));


		piePlot.setBackgroundPaint(Color.white);

		panelBarChart.setLayout(new BorderLayout());
		//create chartPanel to display chart(graph)
		ChartPanel barChartPanel = new ChartPanel(piechart);
		panelBarChart.removeAll();
		panelBarChart.add(barChartPanel, BorderLayout.CENTER);
		panelBarChart.validate();
		
		panelBarChart.repaint();
	    }
	

	/**
	 * This method is called from within the constructor to initialize the
	 * form. WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                panelBarChart = new java.awt.Panel();

                setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

                javax.swing.GroupLayout panelBarChartLayout = new javax.swing.GroupLayout(panelBarChart);
                panelBarChart.setLayout(panelBarChartLayout);
                panelBarChartLayout.setHorizontalGroup(
                        panelBarChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 336, Short.MAX_VALUE)
                );
                panelBarChartLayout.setVerticalGroup(
                        panelBarChartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 256, Short.MAX_VALUE)
                );

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(131, 131, 131)
                                .addComponent(panelBarChart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(568, Short.MAX_VALUE))
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(55, 55, 55)
                                .addComponent(panelBarChart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(313, Short.MAX_VALUE))
                );

                pack();
        }// </editor-fold>//GEN-END:initComponents

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
		/* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(AdminDashboardPage2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(AdminDashboardPage2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(AdminDashboardPage2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(AdminDashboardPage2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new AdminDashboardPage2().setVisible(true);
			}
		});
	}

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private java.awt.Panel panelBarChart;
        // End of variables declaration//GEN-END:variables
}

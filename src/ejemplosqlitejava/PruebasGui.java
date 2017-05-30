/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejemplosqlitejava;

import java.awt.event.ActionEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;

/**
 *
 * @author otorradomiguez
 */
public class PruebasGui {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Gui g = new Gui();
        SQLiteHandler hand = new SQLiteHandler();

        g.botonCargar.addActionListener((ActionEvent e) -> {
            JFileChooser fc = new JFileChooser("src/..");
            int showOpenDialog = fc.showOpenDialog(Gui.marco);
            if (showOpenDialog == JFileChooser.APPROVE_OPTION) {
                String ruta = fc.getName(fc.getSelectedFile());
                hand.conectar(ruta);
                hand.cargarDB();
            }
        });

        g.botonComando.addActionListener((ActionEvent e) -> {
            String comando = JOptionPane.showInputDialog("Intruzca el comando sql");
            hand.executeUpdate(comando);
            hand.cargarDB();
        });

        g.botonModificar.addActionListener((ActionEvent e) -> {
            Gui.panelDatos.removeAll();
            Gui.panelDatos.repaint();
            Gui.panelDatos.revalidate();
            //System.out.println(Gui.panelDerecha.getSelectedComponent());
            JScrollPane sp1 = (JScrollPane) Gui.panelDerecha.getSelectedComponent();
            //System.out.println(sp1.getComponent(0));
            JViewport vp1 = (JViewport) sp1.getComponent(0);
            JTable t1 = (JTable) vp1.getComponent(0);
            //System.out.println(t1.getSelectedRow());
            for (int i = 0; i < t1.getModel().getColumnCount(); i++) {
                //System.out.println(t1.getColumnName(i) + " , " + t1.getValueAt(t1.getSelectedRow(), i));
                Gui.panelDatos.add(new JLabel("" + t1.getColumnName(i)));
                Gui.panelDatos.add(new JTextField("" + t1.getValueAt(t1.getSelectedRow(), i)));

            }
            JButton botonCommit = new JButton("Aplicar Cambios");
            Gui.panelDatos.add(botonCommit);
            Gui.panelDatos.repaint();
            Gui.panelDatos.revalidate();

            botonCommit.addActionListener((ActionEvent ev) -> {
                //System.out.println((Gui.panelDatos.getComponentCount()-1)/2);
                System.out.println(t1.getName());

                String setValues = " ";
                for (int l = 0; l < t1.getColumnCount(); l++) {
                    if ((t1.getValueAt(t1.getSelectedRow(), l) == null || t1.getValueAt(t1.getSelectedRow(), l).equals("")) && l < t1.getColumnCount() - 1) {
                        setValues = setValues + t1.getColumnName(l) + " is null and ";
                    } else if (t1.getValueAt(t1.getSelectedRow(), l) == null || t1.getValueAt(t1.getSelectedRow(), l).equals("")) {
                        setValues = setValues + t1.getColumnName(l) + " is null ";
                    }else if (l < t1.getColumnCount() - 1 && t1.getValueAt(t1.getSelectedRow(), l) != null) {
                        setValues = setValues + t1.getColumnName(l) + "='" + t1.getValueAt(t1.getSelectedRow(), l) + "' and ";
                    }else if (t1.getValueAt(t1.getSelectedRow(), l) != null) {
                        setValues = setValues + t1.getColumnName(l) + "='" + t1.getValueAt(t1.getSelectedRow(), l) + "'";
                    } 
                }

                /*
                Aqui inserto los nuevos valores en la tabla
                 */
                int k = 1;
                for (int j = 0; j < (Gui.panelDatos.getComponentCount() - 1) / 2; j++) {
                    JTextField tf1 = (JTextField) Gui.panelDatos.getComponent(k);
                    t1.setValueAt(tf1.getText(), t1.getSelectedRow(), j);
                    k += 2;
                }

                String setValuesUpdate = " set ";
                String insertValuesUpdate = " ";
                for (int l = 0; l < t1.getColumnCount(); l++) {
                    if (l < t1.getColumnCount() - 1 && (t1.getValueAt(t1.getSelectedRow(), l).equals("null") || t1.getValueAt(t1.getSelectedRow(), l).equals("''"))) {
                        setValuesUpdate = setValuesUpdate + t1.getColumnName(l) + "=null ,";
                        insertValuesUpdate = insertValuesUpdate + "null ,";
                    } else if (t1.getValueAt(t1.getSelectedRow(), l).equals("null") || t1.getValueAt(t1.getSelectedRow(), l).equals("''")) {
                        setValuesUpdate = setValuesUpdate + t1.getColumnName(l) + "=null ";
                        insertValuesUpdate = insertValuesUpdate + "null ";
                        //El !=null creo que dará true siempre pq
                    } else if (l < t1.getColumnCount() - 1 && t1.getValueAt(t1.getSelectedRow(), l) != null) {
                        setValuesUpdate = setValuesUpdate + t1.getColumnName(l) + "='" + t1.getValueAt(t1.getSelectedRow(), l) + "', ";
                        insertValuesUpdate = insertValuesUpdate + "'" + t1.getValueAt(t1.getSelectedRow(), l) + "',";
                    } else if (t1.getValueAt(t1.getSelectedRow(), l) != null) {
                        setValuesUpdate = setValuesUpdate + t1.getColumnName(l) + "='" + t1.getValueAt(t1.getSelectedRow(), l) + "'";
                        insertValuesUpdate = insertValuesUpdate + "'" + t1.getValueAt(t1.getSelectedRow(), l) + "'";
                    }

                }

                Gui.panelDatos.removeAll();
                Gui.panelDatos.repaint();
                Gui.panelDatos.revalidate();

                ResultSet rsNumFilas = hand.executeConsulta("select count(*) from " + t1.getName());
                int filasDB = 0;
                try {
                    while (rsNumFilas.next()) {
                        filasDB = rsNumFilas.getInt(1);
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(PruebasGui.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (t1.getSelectedRow() <= filasDB - 1) {
                    String consulta = "update " + t1.getName() + setValuesUpdate + " where" + setValues;
                    System.out.println(consulta);
                    hand.executeUpdate(consulta);
                } else {
                    String consultaInsert = "insert into " + t1.getName() + " values(" + insertValuesUpdate + ")";
                    System.out.println(consultaInsert);
                    hand.executeUpdate(consultaInsert);
                    //De lo que sigue ya se encarga la llamada hand.cargarDB
                    /*DefaultTableModel nuevaFilaModel = (DefaultTableModel) t1.getModel();
                    Object[] filaVacia = new Object[t1.getColumnCount()];
                    nuevaFilaModel.addRow(filaVacia);
                    t1.setModel(nuevaFilaModel);
                     */
                }
                hand.cargarDB();
                Gui.marco.repaint();
                Gui.marco.revalidate();

            });
        });

    }

}

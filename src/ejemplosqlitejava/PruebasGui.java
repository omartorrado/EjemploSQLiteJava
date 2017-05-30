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

            JScrollPane sp1 = (JScrollPane) Gui.panelDerecha.getSelectedComponent();
            JViewport vp1 = (JViewport) sp1.getComponent(0);
            JTable t1 = (JTable) vp1.getComponent(0);

            if (t1.getSelectedRow() > (-1)) {
                for (int i = 0; i < t1.getModel().getColumnCount(); i++) {
                    //System.out.println(t1.getColumnName(i) + " , " + t1.getValueAt(t1.getSelectedRow(), i));

                    Gui.panelDatos.add(new JLabel("" + t1.getColumnName(i)));
                    Gui.panelDatos.add(new JTextField("" + t1.getValueAt(t1.getSelectedRow(), i)));
                }

                JButton botonCommit = new JButton("Aplicar Cambios");
                Gui.panelDatos.add(botonCommit);

                botonCommit.addActionListener((ActionEvent e2) -> {
                    Boolean checkAllNull = true;
                    String conditionValues = setConditionValues();

                    /*
                Aqui inserto los nuevos valores en la JTable
                     */
                    int k = 1;
                    for (int j = 0; j < (Gui.panelDatos.getComponentCount() - 1) / 2; j++) {
                        JTextField tf1 = (JTextField) Gui.panelDatos.getComponent(k);
                        t1.setValueAt(tf1.getText(), t1.getSelectedRow(), j);
                        k += 2;
                    }

                    //setValuesUpdate crea la string para los set
                    String setValuesUpdate = " set ";
                    //insertValuesUpdate crea la string para los insert
                    String insertValuesUpdate = " ";
                    for (int l = 0; l < t1.getColumnCount(); l++) {
                        if (l < t1.getColumnCount() - 1 && (t1.getValueAt(t1.getSelectedRow(), l).equals("null") || t1.getValueAt(t1.getSelectedRow(), l).equals(""))) {
                            setValuesUpdate = setValuesUpdate + t1.getColumnName(l) + "=null ,";
                            insertValuesUpdate = insertValuesUpdate + "null ,";
                        } else if (t1.getValueAt(t1.getSelectedRow(), l).equals("null") || t1.getValueAt(t1.getSelectedRow(), l).equals("")) {
                            setValuesUpdate = setValuesUpdate + t1.getColumnName(l) + "=null ";
                            insertValuesUpdate = insertValuesUpdate + "null ";
                            //El !=null creo que dará true siempre pq
                        } else if (l < t1.getColumnCount() - 1) {
                            setValuesUpdate = setValuesUpdate + t1.getColumnName(l) + "='" + t1.getValueAt(t1.getSelectedRow(), l) + "', ";
                            insertValuesUpdate = insertValuesUpdate + "'" + t1.getValueAt(t1.getSelectedRow(), l) + "',";
                            checkAllNull = false;
                        } else {
                            setValuesUpdate = setValuesUpdate + t1.getColumnName(l) + "='" + t1.getValueAt(t1.getSelectedRow(), l) + "'";
                            insertValuesUpdate = insertValuesUpdate + "'" + t1.getValueAt(t1.getSelectedRow(), l) + "'";
                            checkAllNull = false;
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
                        String consulta = "update " + t1.getName() + setValuesUpdate + " where" + conditionValues;
                        hand.executeUpdate(consulta);
                    } else if (checkAllNull == false) {
                        String consultaInsert = "insert into " + t1.getName() + " values(" + insertValuesUpdate + ")";
                        hand.executeUpdate(consultaInsert);
                    } else {
                        JOptionPane.showMessageDialog(null, "Como todos los valores son nulos no se incluiran en la BD");
                    }
                    hand.cargarDB();
                    Gui.marco.repaint();
                    Gui.marco.revalidate();
                });

                JButton botonBorrar = new JButton("Borrar");
                Gui.panelDatos.add(botonBorrar);

                botonBorrar.addActionListener((ActionEvent e3) -> {
                    String borrar = "delete from " + t1.getName() + " where " + setConditionValues();
                    hand.executeUpdate(borrar);
                    Gui.panelDatos.removeAll();
                    hand.cargarDB();
                    Gui.marco.repaint();
                    Gui.marco.revalidate();
                });

                Gui.panelDatos.repaint();
                Gui.panelDatos.revalidate();
            }
        });
    }

    public static String setConditionValues() {
        JScrollPane sp1 = (JScrollPane) Gui.panelDerecha.getSelectedComponent();
        JViewport vp1 = (JViewport) sp1.getComponent(0);
        JTable t1 = (JTable) vp1.getComponent(0);
        //setValues crea la string con las condiciones para los where
        String conditionValues = " ";
        for (int l = 0; l < t1.getColumnCount(); l++) {
            if (t1.getValueAt(t1.getSelectedRow(), l) == null && l < t1.getColumnCount() - 1) {
                conditionValues = conditionValues + t1.getColumnName(l) + " is null and ";
            } else if (t1.getValueAt(t1.getSelectedRow(), l) == null) {
                conditionValues = conditionValues + t1.getColumnName(l) + " is null ";
            } else if (l < t1.getColumnCount() - 1) {
                conditionValues = conditionValues + t1.getColumnName(l) + "='" + t1.getValueAt(t1.getSelectedRow(), l) + "' and ";
            } else {
                conditionValues = conditionValues + t1.getColumnName(l) + "='" + t1.getValueAt(t1.getSelectedRow(), l) + "'";
            }
        }
        return conditionValues;
    }
}

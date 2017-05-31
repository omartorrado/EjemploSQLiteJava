/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejemplosqlitejava;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author otorradomiguez
 */
public class SQLiteHandler {
    
    private Connection cn=null;
    private Statement st;
    
    public void conectar(){
        try {
            Class.forName("org.sqlite.JDBC");
            cn=DriverManager.getConnection("jdbc:sqlite:default.db");
            st = cn.createStatement();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(SQLiteHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void conectar(String database){
        try {
            Class.forName("org.sqlite.JDBC");
            cn=DriverManager.getConnection("jdbc:sqlite:"+database);
            st = cn.createStatement();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(SQLiteHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void cargarDB(){
        Gui.panelDerecha.removeAll();
        try {
            ResultSet listaTabla=st.executeQuery("select name from sqlite_master where type='table'");
            int contadorTablas=0;
            while(listaTabla.next()){
                String nombreTabla=listaTabla.getString(1);
                //System.out.println("Nueva tabla");
                //System.out.println(nombreTabla);
                
                JTable tabla=new JTable();
                DefaultTableModel modeloTabla=new DefaultTableModel(); 
                
                //Creamos otro statement para esta busqueda y guardamos en el los
                //datos de la tabla
                Statement st2=cn.createStatement();
                ResultSet rs=st2.executeQuery("pragma table_info("+nombreTabla+")");
                
                int contadorCampos=0;
                System.out.println("Lista campos "+nombreTabla+":");
                while(rs.next()){
                    /*
                    for(int r=1;r<=rs.getMetaData().getColumnCount();r++){
                    System.out.println(rs.getMetaData().getColumnName(r));
                    }
                       */
                    //La string 2 del pragma es el nombre y la 3 el typo de dato
                    String columnName=rs.getString(2);
                    //Column type no lo uso de momento
                    String columnType=rs.getString(3);
                    modeloTabla.addColumn(columnName);
                    contadorCampos++;
                    System.out.println(columnName+", type:"+columnType);
                    }
                //Creo otro statement para la busqueda de los valores de cada fila de la tabla
                Statement st3=cn.createStatement();
                ResultSet rs2=st3.executeQuery("select * from "+nombreTabla);
                //guardo los datos de cada fila en un array para incluirlos en el TableModel
                Object[] fila=new Object[contadorCampos];
                while(rs2.next()){
                    int posicionFila=0;
                    for(int g=0;g<contadorCampos;g++){
                    fila[posicionFila]=rs2.getString(posicionFila+1);
                    posicionFila++;
                    }
                    modeloTabla.addRow(fila);
                }
                Object[] filaVacia=new Object[contadorCampos];
                modeloTabla.addRow(filaVacia);
                //Finalmente asignamos el modelo a la tabla
                tabla.setDefaultEditor(Object.class, null);
                tabla.setModel(modeloTabla);
                tabla.setShowGrid(true);
                tabla.setName(nombreTabla);
                
                Gui.panelDerecha.add(new JScrollPane(tabla));
                Gui.panelDerecha.setTitleAt(contadorTablas, nombreTabla);
                Gui.panelDerecha.setVisible(true);
                
                contadorTablas++;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(SQLiteHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ResultSet executeConsulta(String consulta){
        try {
            return st.executeQuery(consulta);
        } catch (SQLException ex) {
            Logger.getLogger(SQLiteHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public void executeUpdate(String consulta){
        try {
            st.executeUpdate(consulta);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ha ocurrido un error al realizar la modificacion");
        }
    }
    
}

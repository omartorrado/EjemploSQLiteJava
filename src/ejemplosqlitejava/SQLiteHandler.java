/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejemplosqlitejava;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        try {
            ResultSet listaTabla=st.executeQuery("select name from sqlite_master where type='table'");
            while(listaTabla.next()){
                String nombreTabla=listaTabla.getString(1);
                System.out.println("Nueva tabla");
                System.out.println(nombreTabla);
                
                JScrollPane panelTabla=new JScrollPane();
                JTable tabla=new JTable();
                DefaultTableModel modeloTabla=(DefaultTableModel) tabla.getModel();
                
                //Creamos otro statement para esta busqueda y guardamos en el los
                //datos de la tabla
                Statement st2=cn.createStatement();
                ResultSet rs=st2.executeQuery("pragma table_info("+nombreTabla+")");
                ResultSetMetaData rsmd=rs.getMetaData();
                while(rs.next()){
                    //La string 2 del pragma es el nombre y la 3 el typo de dato
                    String columnName=rs.getString(2);
                    String columnType=rs.getString(3);
                    modeloTabla.addColumn(columnName);
                    }
                //tabla.setModel(modeloTabla);  esto va luego
                Statement st3=cn.createStatement();
                ResultSet rs2=st3.executeQuery("select * from "+nombreTabla);
                while(rs2.next()){
                    
                }
                System.out.println("Fin Tabla");
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
            Logger.getLogger(SQLiteHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

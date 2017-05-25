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
                System.out.println(listaTabla.getString(1));
                JScrollPane panelTabla=new JScrollPane();
                JTable tabla=new JTable();
                //Hay que ver como crear las tablas con los campos sacados de la db
                tabla.getModel().
                Gui.panelDerecha.
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

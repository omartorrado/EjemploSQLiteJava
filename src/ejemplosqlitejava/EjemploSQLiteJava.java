/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejemplosqlitejava;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author guest-z5aepn
 */
public class EjemploSQLiteJava {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Connection cn=null;
        try {
            //Cargamos el driver JDBC para sqlite (Esta en la libreria)
            Class.forName("org.sqlite.JDBC");
            //Establecemos una conexion (Como no existe la DB creara una)
            cn= DriverManager.getConnection("jdbc:sqlite:ejemploDB.db");
            //Instanciamos un Statement (Para realizar consultas, etc)
            Statement st = cn.createStatement();
            //A partir de aqui podemos interactuar con la BD usando el statement
            st.executeUpdate("DROP table if exists ejemplo");
            st.executeUpdate("CREATE table ejemplo(id integer, nombre string, valor float)");
            st.executeUpdate("INSERT into ejemplo values(1,'Omar',8.25)");
            st.executeUpdate("INSERT into ejemplo values(2,'Pedro',7.86)");
            //Realizamos una consulta
            ResultSet consulta1=st.executeQuery("SELECT * from ejemplo");
            //Imprimimos los resultados con un while
            while(consulta1.next()){
                System.out.println("ID: "+consulta1.getInt("id")+", nombre: "+
                        consulta1.getString("nombre")+", valor: "+consulta1.getFloat("valor"));
            }
        //Aki vienen los try-catch para las posibles excepciones
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EjemploSQLiteJava.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(EjemploSQLiteJava.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            /*Por ultimo si la conexion no es nula la cierra (y con ello tb los 
            Statements y Resultsets que creasemos con ella
            */
            try {
                if(cn!=null){
                cn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(EjemploSQLiteJava.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}

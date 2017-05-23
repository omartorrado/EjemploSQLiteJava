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
            st.executeUpdate("DROP table ejemplo");//Hacemos el drop pq si la tabla ya existe da error
            st.executeUpdate("CREATE table ejemplo(id integer, nombre varchar, valor float)");//Creamos una tabla
            /*
            A la hora de indicar los DataTypes podemos usar diferentes notaciones, por ejemplo
            nos serviría tanto string, como varchar o varchar(20), Char(20)
            Mas informacion al respecto en: https://www.sqlite.org/datatype3.html y https://www.w3schools.com/sql/sql_datatypes_general.asp
            */
            st.executeUpdate("INSERT into ejemplo values(1,'Omar',8.25)");//añadimos una fila a dicha tabla
            st.executeUpdate("INSERT into ejemplo values(null,'Pedro',null)");//aqui uno de los campos va vacio,si es un numero le asigna valor por defecto 0 (o 0.0)
            st.executeUpdate("insert into ejemplo values(3,null,1.9834)");//aqui creamos una con el campo nombre como null, si pusiesemos el nombre sin las comillas daria error de SQL
            //st.executeUpdate("INSERT into ejemplo values(1,Omar,8.25)"); -> Esto da error por lo dicho arriba
            st.executeUpdate("INSERT into ejemplo values(1,'Omar',8.25)");//No pone ningun impedimento a la hora de repetir resultados ¿quiza pq no definimos clave primaria?->exacto
            /*
            Creando la tabla con una primary key como en la siguiente linea si k haria que diese un error de SQL la linea anterior
            st.executeUpdate("CREATE table ejemplo(id integer, nombre string, valor float, primary key (id))");
            */            
            //Realizamos una consulta (Viene siendo un cursor el ResultSet)
            ResultSet consulta1=st.executeQuery("SELECT * from ejemplo");
            //Imprimimos los resultados con un while
            while(consulta1.next()){
                //Para mostrar los resultados podemos acceder a ellos con getInt, etc, segun el tipo de dato
                System.out.println("ID: "+consulta1.getInt("id")+", nombre: "+
                        consulta1.getString("nombre")+", valor: "+consulta1.getFloat("valor"));
                }
            /*
            Genero una tabla diferente
            */
            st.executeUpdate("create table jugadores(id integer, nombre string)");
            st.executeUpdate("create table barajas(id integer, baraja string");
            st.executeUpdate("create table cartas(idc integer, vida integer, ataque integer, coste integer");
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejemplosqlitejava;

/**
 *
 * @author otorradomiguez
 */
public class PruebasGui {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Gui g=new Gui();
        SQLiteHandler hand=new SQLiteHandler();
        hand.conectar("ejemploDB.db");
        hand.cargarDB();
        
        
    }
    
}

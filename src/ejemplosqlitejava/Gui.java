/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejemplosqlitejava;

import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author otorradomiguez
 */
public class Gui {
    
    public static JFrame marco = new JFrame();
    public static JTabbedPane panelDerecha = new JTabbedPane();
    public static JPanel panelIzquierda=new JPanel();
    public static JPanel panelDatos = new JPanel();
    public static JPanel panelBotones = new JPanel();
    
    JButton botonConsulta=new JButton("Realizar Consulta");
    JButton botonComando=new JButton("Ejecutar Comando SQL");
    JButton botonModificar= new JButton("Modificar fila");

    public Gui() {
        
        marco.setLayout(new FlowLayout());        
        //marco.setLayout(null);
        
        
        panelIzquierda.setLayout(new BoxLayout(panelIzquierda,BoxLayout.Y_AXIS));
        panelIzquierda.add(new JLabel("Panel Izquierda"));
        
        panelDatos.setLayout(new BoxLayout(panelDatos,BoxLayout.Y_AXIS));
        panelDatos.add(new JLabel("Panel Datos"));
        
        panelBotones.add(botonConsulta);
        panelBotones.add(botonComando);
        panelBotones.add(botonModificar);
        
        panelIzquierda.add(panelDatos);
        panelIzquierda.add(panelBotones);
                
        marco.add(panelIzquierda);

        panelDerecha.setSize(500,600);
        marco.add(panelDerecha);
        
        marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        marco.setSize(1000,600);
        marco.setLocationRelativeTo(null);
        marco.setVisible(true);
    }
    

}

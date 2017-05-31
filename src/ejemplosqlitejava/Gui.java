/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejemplosqlitejava;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author otorradomiguez
 */
public class Gui {

    public static JFrame marco = new JFrame();
    public static JTabbedPane panelDerecha = new JTabbedPane();
    public static JPanel panelIzquierda = new JPanel();
    public static JPanel panelDatos = new JPanel();
    public static JPanel panelBotones = new JPanel();

    //JButton botonConsulta=new JButton("Realizar Consulta");
    public JButton botonComando = new JButton("Ejecutar Comando SQL");
    public JButton botonModificar = new JButton("Modificar fila");
    public JButton botonCargar = new JButton("Cargar base de datos");
    public JButton botonSalir = new JButton("Salir");

    public Gui() {

        marco.setLayout(new FlowLayout());
        marco.setTitle("SQLite Simple Manager");

        panelIzquierda.setLayout(new BoxLayout(panelIzquierda, BoxLayout.Y_AXIS));

        panelDatos.setLayout(new BoxLayout(panelDatos, BoxLayout.Y_AXIS));

        botonSalir.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });

        panelBotones.add(botonCargar);
        //panelBotones.add(botonConsulta);
        panelBotones.add(botonComando);
        panelBotones.add(botonModificar);
        panelBotones.add(botonSalir);

        panelIzquierda.add(panelDatos);
        panelIzquierda.add(panelBotones);

        marco.add(panelIzquierda);

        panelDerecha.setSize(500, 600);
        panelDerecha.setVisible(false);
        marco.add(panelDerecha);

        marco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        marco.setSize(1000, 600);
        marco.setLocationRelativeTo(null);
        marco.setVisible(true);
    }

}

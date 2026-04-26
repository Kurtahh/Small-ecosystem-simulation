package ui;

import javax.swing.*;
import java.awt.*;
// import java.awt.event.*;
// import java.io.*;

public class SimulationUI {
    static final int buttonW = 200;
    static final int buttonH = 100;

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 720);
        frame.setVisible(true);
        UIManager.put("Button.font", new Font("Serif", Font.PLAIN, 24));
    }

    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SimulationUI().createAndShowGUI();
            }
        });
    }
}

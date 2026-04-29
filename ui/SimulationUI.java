package ui;

import javax.swing.*;
import java.awt.*;
import simulation.animals.Rabbit;
import simulation.animals.Wolf;
import simulation.Ecosystem;
// import java.awt.event.*;
// import java.io.*;

public class SimulationUI {
    static final int buttonW = 200;
    static final int buttonH = 100;

    private class WorldPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            for(Rabbit r : Ecosystem.getInstance().getRabbits()){
                g2d.setColor(Color.GREEN);
                g2d.fillOval((int) r.getX(), (int) r.getY(), 10, 10);
            }
            for(Wolf w : Ecosystem.getInstance().getWolves()){
                g2d.setColor(Color.RED);
                int[] xPoints = { (int) w.getX(), (int) w.getX() - 5, (int) w.getX() + 5};
                int[] yPoints = { (int) w.getY(), (int) w.getY() + 10, (int) w.getY() + 10};
                g2d.fillPolygon(xPoints, yPoints, 3);
            }
        }
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 720);
        UIManager.put("Button.font", new Font("Serif", Font.PLAIN, 24));

        GridBagLayout gbl = new GridBagLayout();
        frame.setLayout(gbl);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        WorldPanel world = new WorldPanel();
        world.setPreferredSize(new Dimension(700, 480));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 20, 0, 20);
        frame.add(world, gbc);

        JPanel buttons = new JPanel(new GridLayout(2, 2, 10, 10));
        gbc.gridy = 1;

        JButton addR = new JButton("Add rabbit");
        addR.addActionListener(e -> {
            Ecosystem.getInstance().addRabbit();
        });
        addR.setPreferredSize(new Dimension(buttonW, buttonH));
        addR.setFocusPainted(false);
        buttons.add(addR);

        JButton addW = new JButton("Add wolf");
        addW.addActionListener(e -> {
            Ecosystem.getInstance().addWolf();
        });
        addW.setPreferredSize(new Dimension(buttonW, buttonH));
        addW.setFocusPainted(false);
        buttons.add(addW);

        JButton save = new JButton("Save");
        save.addActionListener(e -> {
            //Ecosystem.getInstance().save();
        });
        save.setPreferredSize(new Dimension(buttonW, buttonH));
        save.setFocusPainted(false);
        buttons.add(save);

        JButton load = new JButton("Load");
        load.addActionListener(e -> {
            //Ecosystem.getInstance().load();
        });
        load.setPreferredSize(new Dimension(buttonW, buttonH));
        load.setFocusPainted(false);
        buttons.add(load);

        gbc.insets = new Insets(10, 20, 10, 20);
        frame.add(buttons, gbc);

        frame.setVisible(true);
        Ecosystem.getInstance().setDimensions(world.getWidth(), world.getHeight());

        Thread simulationThread = new Thread(() -> {
            while(true){
                Ecosystem.getInstance().update();
                world.repaint();
                try {
                    Thread.sleep(100);
                }
                catch(InterruptedException e) {
                    break;
                }
            }
        });
        simulationThread.start();
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

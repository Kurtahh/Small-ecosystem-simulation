package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import simulation.animals.Rabbit;
import simulation.animals.Wolf;
import simulation.Ecosystem;

public class SimulationUI {
    static final int buttonW = 200;
    static final int buttonH = 100;
    private Image buttonSprite = new ImageIcon("assets/BUTTON.png").getImage();

    private class WorldPanel extends JPanel {
        private Image ecoBgSprite = new ImageIcon("assets/ECOSYSTEM.png").getImage();
        private Image rabbitSprite = new ImageIcon("assets/RABBIT.png").getImage();
        @Override
        protected void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            //ecosystem background
            g2d.drawImage(ecoBgSprite, 0, 0, getWidth(), getHeight(), this);
            g2d.setColor(new Color(0, 0, 0, 80)); // last value is alpha, 0-255
            g2d.fillRect(0, 0, getWidth(), getHeight());
            //outline
            int stroke = 4;
            g2d.setStroke(new BasicStroke(stroke));
            g2d.setColor(Color.BLACK);
            g2d.drawRect(stroke/2, stroke/2, getWidth()-stroke, getHeight()-stroke);
            g2d.setStroke(new BasicStroke(1));
            //animals
            for(Rabbit r : Ecosystem.getInstance().getRabbits()){
                AffineTransform saved = g2d.getTransform();

                double rAngle = Math.atan2(r.getFacing().y, r.getFacing().x) - Math.PI/2; 
                double rx = r.getPosition().x;
                double ry = r.getPosition().y;

                g2d.translate(rx, ry);
                if(r.getMovementState() != Rabbit.MovementState.WANDERING) g2d.rotate(rAngle);
                g2d.drawImage(rabbitSprite, -16, -16, 32, 32, this);
                g2d.setTransform(saved); 
                //g2d.fillOval((int) r.getPosition().x, (int) r.getPosition().y, 10, 10);
            }
            for(Wolf w : Ecosystem.getInstance().getWolves()){
                AffineTransform saved = g2d.getTransform();

                g2d.setColor(Color.RED);
                double wangle = Math.atan2(w.getFacing().y, w.getFacing().x) + Math.PI/2;
                double wx = w.getPosition().x;
                double wy = w.getPosition().y;

                g2d.translate(wx, wy);
                if(w.getMovementState() != Wolf.MovementState.WANDERING) g2d.rotate(wangle);

                int[] xPoints = { 0, -w.getSize(), w.getSize() };
                int[] yPoints = { 0, w.getSize()*2, w.getSize()*2 };
                double scale = 1.2;
                int[] outlineX = {0, (int)(-w.getSize()*scale), (int)(w.getSize()*scale)};
                int[] outlineY = {(int)(-w.getSize()*0.2 -2), (int)(w.getSize()*2*scale), (int)(w.getSize()*2*scale)};
                // outline
                g2d.setColor(Color.BLACK);
                g2d.drawPolygon(outlineX, outlineY, 3);
                g2d.setStroke(new BasicStroke(1)); // reset

                // fill
                g2d.setColor(Color.RED);
                g2d.fillPolygon(xPoints, yPoints, 3);

                g2d.setTransform(saved);
            }
        }
    }

    private void createAndShowGUI() throws FontFormatException, IOException{
        Font minecraftFont = Font.createFont(Font.TRUETYPE_FONT, new File("assets/minecraft.ttf")).deriveFont(30f);
        GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(minecraftFont);
        JFrame frame = new JFrame("Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 720);
        UIManager.put("Button.font", new Font("Serif", Font.PLAIN, 24));

        GridBagLayout gbl = new GridBagLayout();
        frame.setLayout(gbl);

        JPanel contentPane = new JPanel(gbl) {
            private Image bg = new ImageIcon("assets/BACKGROUND.png").getImage();
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
            }
        };
        frame.setContentPane(contentPane);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        WorldPanel world = new WorldPanel();
        world.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Ecosystem.getInstance().setDimensions(world.getWidth(), world.getHeight());
            }
        });
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(30, 40, 10, 40);
        frame.add(world, gbc);

        JPanel buttons = new JPanel(new GridLayout(2, 2, 10, 10));

        JButton addR = new JButton("Add rabbit") {
            @Override
            protected void paintComponent(Graphics g) {
                g.drawImage(buttonSprite, 0, 0, getWidth(), getHeight(), this);
                if(getModel().isRollover()){
                    g.setColor(new Color(255, 255, 255, 30));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
                if(getModel().isPressed()){
                    g.setColor(new Color(255, 255, 255, 60));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
                g.setColor(new Color(0, 0, 0, 60));
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        addR.setRolloverEnabled(true);
        addR.setFont(minecraftFont);
        addR.setHorizontalTextPosition(JButton.CENTER);
        addR.setVerticalTextPosition(JButton.CENTER);
        addR.setContentAreaFilled(false);
        addR.addActionListener(e -> {
            Ecosystem.getInstance().addRabbit();
        });
        addR.setPreferredSize(new Dimension(buttonW, buttonH));
        addR.setFocusPainted(false);
        addR.setForeground(Color.BLACK);
        addR.setOpaque(false);
        addR.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        buttons.add(addR);

        JButton addW = new JButton("Add wolf") {
            @Override
            protected void paintComponent(Graphics g) {
                g.drawImage(buttonSprite, 0, 0, getWidth(), getHeight(), this);
                if(getModel().isRollover()){
                    g.setColor(new Color(255, 255, 255, 30));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
                if(getModel().isPressed()){
                    g.setColor(new Color(255, 255, 255, 60));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
                g.setColor(new Color(0, 0, 0, 60));
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        addW.setRolloverEnabled(true);
        addW.setFont(minecraftFont);
        addW.setHorizontalTextPosition(JButton.CENTER);
        addW.setVerticalTextPosition(JButton.CENTER);
        addW.setContentAreaFilled(false);
        addW.addActionListener(e -> {
            Ecosystem.getInstance().addWolf();
        });
        addW.setPreferredSize(new Dimension(buttonW, buttonH));
        addW.setFocusPainted(false);
        addW.setForeground(Color.BLACK);
        addW.setOpaque(false);
        addW.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        buttons.add(addW);

        JButton save = new JButton("Save") {
            @Override
            protected void paintComponent(Graphics g) {
                g.drawImage(buttonSprite, 0, 0, getWidth(), getHeight(), this);
                if(getModel().isRollover()){
                    g.setColor(new Color(255, 255, 255, 30));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
                if(getModel().isPressed()){
                    g.setColor(new Color(255, 255, 255, 60));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
                g.setColor(new Color(0, 0, 0, 60));
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        save.setRolloverEnabled(true);
        save.setFont(minecraftFont);
        save.setHorizontalTextPosition(JButton.CENTER);
        save.setVerticalTextPosition(JButton.CENTER);
        save.setContentAreaFilled(false);
        save.addActionListener(e -> {
            //Ecosystem.getInstance().save();
        });
        save.setPreferredSize(new Dimension(buttonW, buttonH));
        save.setFocusPainted(false);
        save.setForeground(Color.BLACK);
        save.setOpaque(false);
        save.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        buttons.add(save);

        JButton load = new JButton("Load") {
            @Override
            protected void paintComponent(Graphics g) {
                g.drawImage(buttonSprite, 0, 0, getWidth(), getHeight(), this);
                if(getModel().isRollover()){
                    g.setColor(new Color(255, 255, 255, 30));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
                if(getModel().isPressed()){
                    g.setColor(new Color(255, 255, 255, 60));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
                g.setColor(new Color(0, 0, 0, 60));
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        load.setRolloverEnabled(true);
        load.setFont(minecraftFont);
        load.setHorizontalTextPosition(JButton.CENTER);
        load.setVerticalTextPosition(JButton.CENTER);
        load.setContentAreaFilled(false);
        load.addActionListener(e -> {
            //Ecosystem.getInstance().load();
        });
        load.setPreferredSize(new Dimension(buttonW, buttonH));
        load.setFocusPainted(false);
        load.setForeground(Color.BLACK);
        load.setOpaque(false);
        load.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        buttons.add(load);

        buttons.setPreferredSize(new Dimension(700, buttonH * 2 + 10));
        gbc.gridy = 1;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 20, 10, 20);
        buttons.setOpaque(true);
        buttons.setBackground(new Color(0, 0, 0, 100));
        frame.add(buttons, gbc);

        frame.setVisible(true);

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
                try {
                    new SimulationUI().createAndShowGUI();
                }
                catch(FontFormatException | IOException e){

                }
            }
        });
    }
}
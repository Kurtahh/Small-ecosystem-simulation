package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.IOException;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
        private Image leftWolfSprite = new ImageIcon("assets/LWOLF.png").getImage();
        private Image rightWolfSprite = new ImageIcon("assets/RWOLF.png").getImage();
        private BufferedImage rabbitOutlinedSprite = createOutlined(rabbitSprite, 8, 8);
        private BufferedImage lWolfOutlinedSprite = createOutlined(leftWolfSprite, 16, 16);
        private BufferedImage rWolfOutlinedSprite = createOutlined(rightWolfSprite, 16, 16);
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
                //outline
                g2d.drawImage(rabbitOutlinedSprite, -17, -17, 34, 34, this);
                
                // g2d.drawImage(rabbitSprite, -16, -16, 32, 32, this);
                g2d.setTransform(saved);
            }
            for(Wolf w : Ecosystem.getInstance().getWolves()){
                AffineTransform saved = g2d.getTransform();

                g2d.setColor(Color.RED);
                double wangle;
                if(w.getFacing().x < 0) wangle = Math.atan2(w.getFacing().y, w.getFacing().x) + Math.PI;
                else wangle = Math.atan2(w.getFacing().y, w.getFacing().x);

                double wx = w.getPosition().x;
                double wy = w.getPosition().y;

                g2d.translate(wx, wy);
                if(w.getMovementState() != Wolf.MovementState.WANDERING) g2d.rotate(wangle);

                if(w.getFacing().x > 0 && w.getMovementState() != Wolf.MovementState.WANDERING) 
                    g2d.drawImage(rWolfOutlinedSprite, -48, -48, 96, 96, this);
                else g2d.drawImage(lWolfOutlinedSprite, -48, -48, 96, 96, this);
                
                g2d.setTransform(saved);
            }
        }
        private BufferedImage createOutlined(Image sprite, int w, int h)
        {
            BufferedImage outlined = new BufferedImage(w+2, h+2, BufferedImage.TYPE_INT_ARGB);
            Graphics2D og = outlined.createGraphics();
            og.drawImage(sprite, 1, 0, w, h, null);
            og.drawImage(sprite, 1, 2, w, h, null);
            og.drawImage(sprite, 0, 1, w, h, null);
            og.drawImage(sprite, 2, 1, w, h, null);
            og.drawImage(sprite, 1, 1, w, h, null); // diagonals
            og.drawImage(sprite, 0, 0, w, h, null);
            og.drawImage(sprite, 2, 0, w, h, null);
            og.drawImage(sprite, 0, 2, w, h, null);
            og.drawImage(sprite, 2, 2, w, h, null);
            og.setComposite(AlphaComposite.SrcAtop);
            og.setColor(Color.BLACK);
            og.fillRect(0, 0, w+2, h+2);
            og.setComposite(AlphaComposite.SrcOver);
            og.drawImage(sprite, 1, 1, w, h, null);
            og.dispose();
            return outlined;
        }
    }

    private JButton createButton(String label, Runnable action, Font font) {
        JButton btn = new JButton(label) {
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
        btn.setRolloverEnabled(true);
        btn.setFont(font);
        btn.setHorizontalTextPosition(JButton.CENTER);
        btn.setVerticalTextPosition(JButton.CENTER);
        btn.setContentAreaFilled(false);
        btn.addActionListener(e -> action.run());
        btn.setPreferredSize(new Dimension(buttonW, buttonH));
        btn.setFocusPainted(false);
        btn.setForeground(Color.BLACK);
        btn.setOpaque(false);
        btn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        return btn;
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

        JButton addR = createButton("Add rabbit", () -> Ecosystem.getInstance().addRabbit(), minecraftFont);
        Timer rHoldTimer = new Timer(200, e -> Ecosystem.getInstance().addRabbit());
        rHoldTimer.setRepeats(true);
        addR.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) { rHoldTimer.start(); }
            @Override
            public void mouseReleased(MouseEvent e) { rHoldTimer.stop(); }
        });
        buttons.add(addR);

        JButton addW = createButton("Add wolf", () -> Ecosystem.getInstance().addWolf(), minecraftFont);
        Timer wHoldTimer = new Timer(200, e -> Ecosystem.getInstance().addWolf());
        wHoldTimer.setRepeats(true);
        addW.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) { wHoldTimer.start(); }
            @Override
            public void mouseReleased(MouseEvent e) { wHoldTimer.stop(); }
        });
        buttons.add(addW);

        JButton save = createButton("Save", () -> Ecosystem.getInstance().save(), minecraftFont);
        buttons.add(save);

        JButton load = createButton("Load", () -> Ecosystem.getInstance().load(), minecraftFont);
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
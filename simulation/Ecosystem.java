package simulation;
import java.util.List;

import simulation.animals.*;

import java.util.ArrayList;

public class Ecosystem {
    private static final Ecosystem instance = new Ecosystem();
    private List<Rabbit> rabbits = new ArrayList<>();
    private List<Rabbit> rabbitBuffer = new ArrayList<>();
    private List<Wolf> wolves = new ArrayList<>();
    private int width, height;
    
    private Ecosystem() { };

    public static Ecosystem getInstance()
    {
        return instance;
    }

    public int getWidth()
    {
        return width;
    }
    public int getHeight()
    {
        return height;
    }

    public List<Rabbit> getRabbits()
    {
        return rabbits;
    }
    public List<Wolf> getWolves()
    {
        return wolves;
    }

    public void setDimensions(int newWidth, int newHeight)
    {
        width = newWidth;
        height = newHeight;
    }

    public void bufferRabbit(Rabbit r)
    {
        rabbitBuffer.add(r);
    }
    public synchronized void addRabbit()
    {
        int x = Animal.rand.nextInt(width);
        int y = Animal.rand.nextInt(height);
        Rabbit r = new Rabbit(x, y);
        rabbits.add(r);
    }

    public synchronized void addWolf()
    {   
        int x = Animal.rand.nextInt(width);
        int y = Animal.rand.nextInt(height);
        Wolf w = new Wolf(x, y);
        wolves.add(w);
    }

    public synchronized void update()
    {
        for(Rabbit r : rabbits){
            r.update(Ecosystem.getInstance());
        }
        rabbits.removeIf(r -> !r.getAlive());
        rabbits.addAll(rabbitBuffer);
        rabbitBuffer.clear();
        
        for(Wolf w : wolves){
            w.update(Ecosystem.getInstance());
        }
        wolves.removeIf(w -> !w.getAlive());
    }
}

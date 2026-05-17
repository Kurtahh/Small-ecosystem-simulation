package simulation;
import java.util.List;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import simulation.animals.*;

import java.util.ArrayList;

public class Ecosystem implements Serializable {
    private static final Ecosystem instance = new Ecosystem();
    private List<Rabbit> rabbits = new ArrayList<>();
    private List<Rabbit> rabbitBuffer = new ArrayList<>();
    private List<Wolf> wolves = new ArrayList<>();
    private int width, height;
    
    private Ecosystem() { };

    private void loadFrom(Ecosystem loaded)
    {
        this.rabbits = loaded.rabbits;
        this.wolves = loaded.wolves;
        this.rabbitBuffer = loaded.rabbitBuffer;
        this.width = loaded.width;
        this.height = loaded.height;
    }

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

    public void save()
    {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("save.dat"))) {
            oos.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("save.dat"))) {
            Ecosystem loaded = (Ecosystem) ois.readObject();
            loadFrom(loaded);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

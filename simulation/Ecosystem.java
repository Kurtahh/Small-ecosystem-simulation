package simulation;
import java.util.List;

import simulation.animals.*;

import java.util.ArrayList;

public class Ecosystem {
    private static final Ecosystem instance = new Ecosystem();
    private List<Animal> animals = new ArrayList<>();
    private int width, height;
    
    private Ecosystem() { };

    public static Ecosystem getInstance()
    {
        return instance;
    }

    public List<Animal> getAnimals()
    {
        return animals;
    }

    public void setDimensions(int newWidth, int newHeight)
    {
        width = newWidth;
        height = newHeight;
    }

    public void addRabbit()
    {
        int x = Animal.rand.nextInt(width);
        int y = Animal.rand.nextInt(height);
        Rabbit r = new Rabbit(x, y);
        animals.add(r);
    }

    public void addWolf()
    {   
        int x = Animal.rand.nextInt(width);
        int y = Animal.rand.nextInt(height);
        Wolf w = new Wolf(x, y);
        animals.add(w);
    }

    public void update()
    {

    }
}

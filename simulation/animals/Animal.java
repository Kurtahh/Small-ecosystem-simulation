package simulation.animals;

import simulation.Ecosystem;
import java.util.Random;
import java.io.Serializable;

public abstract class Animal implements Cloneable, Serializable {
    private int x;
    private int y;
    private boolean isAlive = true;
    private static int created = 0;
    private int speed;
    public static Random rand = new Random();

    protected Animal(int x, int y)
    {
        this.x = x;
        this.y = y;
        isAlive = true;
        created++;
    }

    public static int getCreated()
    {
        return created;
    }

    public int getX()
    {
        return x;
    }
    public void setX(int newX)
    {
        x = newX;
    }

    public int getY()
    {
        return y;
    }
    public void setY(int newY)
    {
        y = newY;
    }

    public boolean getAlive()
    {
        return isAlive;
    }
    public void setAlive(boolean isAlive)
    {
        this.isAlive = isAlive;
    }

    public int getSpeed()
    {
        return speed;
    }
    public void setSpeed(int newSpeed)
    {
        speed = newSpeed;
    }

    public void move()
    {
        int dirX = rand.nextInt(3) - 1;
        int dirY = rand.nextInt(3) - 1;
        int newX = getX() + dirX*speed;
        int newY = getY() + dirY*speed;
        setX(newX);
        setY(newY);
    }

    abstract public void update(Ecosystem e);

    @Override
    public Animal clone() throws CloneNotSupportedException
    {
        return (Animal) super.clone();
    }
}

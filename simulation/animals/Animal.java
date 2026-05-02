package simulation.animals;

import simulation.Ecosystem;
import java.util.Random;
import java.io.Serializable;

public abstract class Animal implements Cloneable, Serializable {
    private double x;
    private double y;
    private boolean isAlive = true;
    private static int created = 0;
    private double speed;
    private int eyesight;
    public static Random rand = new Random();
    protected static final double PROXIMITY_THRESHOLD = 10.0;

    protected Animal(double x, double y)
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

    public double getX()
    {
        return x;
    }
    public void setX(double newX)
    {
        x = newX;
    }

    public double getY()
    {
        return y;
    }
    public void setY(double newY)
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

    public double getSpeed()
    {
        return speed;
    }
    public void setSpeed(double newSpeed)
    {
        speed = newSpeed;
    }
    public int getEyesight()
    {
        return eyesight;
    }
    public void setEyesight(int newEyesight)
    {
        eyesight = newEyesight;
    }

    abstract protected void move();

    public void moveRandomly()
    {
        int dirX = rand.nextInt(3) - 1;
        int dirY = rand.nextInt(3) - 1;
        double newX = getX() + dirX*speed;
        double newY = getY() + dirY*speed;
        setX(newX);
        setY(newY);
    }

    abstract protected void update(Ecosystem e);

    @Override
    public Animal clone() throws CloneNotSupportedException
    {
        return (Animal) super.clone();
    }
}

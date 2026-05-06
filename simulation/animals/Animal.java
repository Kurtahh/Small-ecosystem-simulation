package simulation.animals;

import simulation.Ecosystem;
import simulation.util.Vec2;
import java.util.Random;
import java.io.Serializable;

public abstract class Animal implements Cloneable, Serializable {
    private Vec2 position;
    private boolean isAlive = true;
    private static int created = 0;
    private double speed;
    private int eyesight;
    public static Random rand = new Random();
    protected static final double PROXIMITY_THRESHOLD = 10.0;

    protected Animal(double x, double y)
    {
        position = new Vec2(x, y);
        isAlive = true;
        created++;
    }

    public static int getCreated()
    {
        return created;
    }

    public Vec2 getPosition()
    {
        return position;
    }
    public void setPosition(Vec2 newPosition)
    {
        position = newPosition;
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

    public void moveToward(Vec2 target)
    {
        Vec2 towards = new Vec2(position, target);
        towards.normalize();
        double newX = position.x + towards.x*speed;
        double newY = position.y + towards.y*speed;
        setPosition(new Vec2(newX, newY)); 
    }
    public void moveRandomly()
    {
        int dirX = rand.nextInt(3) - 1;
        int dirY = rand.nextInt(3) - 1;
        double newX = getPosition().x + dirX*speed;
        double newY = getPosition().y + dirY*speed;
        setPosition(new Vec2(newX, newY));
    }

    abstract protected void update(Ecosystem e);

    @Override
    public Animal clone() throws CloneNotSupportedException
    {
        return (Animal) super.clone();
    }
}

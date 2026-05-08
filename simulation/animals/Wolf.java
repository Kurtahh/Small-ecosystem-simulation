package simulation.animals;

import simulation.Ecosystem;
import simulation.util.Vec2;

public class Wolf extends Animal{
    private static final int MAX_SIZE = 50;
    private enum MovementState { WANDERING, HUNTING };
    private MovementState state = MovementState.WANDERING;
    private int hunger;
    private int size = 5;
    private int eatCooldown = 3;
    private int starvationRate = 5;
    private Vec2 nearestRabbitPos;

    public Wolf(double x, double y){
        super(x, y);
        setSpeed(2);
        setEyesight(300);
        hunger = 1000;
    }

    public int getSize(){
        return size;
    }

    private void eat(Rabbit food){
        if(eatCooldown > 0){
            hunger += food.getMealFactor();
            hunger = Math.min(hunger, 1000);
            food.setAlive(false);
            if(size != MAX_SIZE) size++;
            setSpeed(getSpeed() - 0.01);
            starvationRate += 0.1;
        }
    }

    private void depleteHunger(){
        hunger -= starvationRate;
        if(hunger <= 0){
            setAlive(false);
        }
    }

    private void checkMovementState(Ecosystem e)
    {
        double minDistRabbit = Double.MAX_VALUE;
        for(Rabbit r : e.getRabbits()){
            Vec2 toRabbit = new Vec2(getPosition(), r.getPosition());
            if(toRabbit.length < getEyesight() && minDistRabbit > toRabbit.length){
                minDistRabbit = toRabbit.length;
                nearestRabbitPos = r.getPosition();
            }
        }

        if(minDistRabbit != Double.MAX_VALUE){
            state = MovementState.HUNTING;
        }
        else {
            state = MovementState.WANDERING;
        }
    }

    @Override
    protected void move()
    {
        switch(state){
            case HUNTING -> moveToward(nearestRabbitPos);
            case WANDERING -> moveRandomly();
        }
    }

    @Override
    public void update(Ecosystem e)
    {
        if(this.getAlive()){
            if(eatCooldown < 0) eatCooldown--;
            checkMovementState(e);
            move();
            double clampedX = Math.max(0, Math.min(getPosition().x, e.getWidth()));
            double clampedY = Math.max(0, Math.min(getPosition().y, e.getHeight()));
            setPosition(new Vec2(clampedX, clampedY));

            for(Rabbit r : e.getRabbits()){
                if(nearestRabbitPos == r.getPosition()){
                    Vec2 toPrey = new Vec2(getPosition(), nearestRabbitPos);
                    if(toPrey.length < PROXIMITY_THRESHOLD){
                        eat(r);
                    }
                }
            }
            
            depleteHunger();
        }
    }
}

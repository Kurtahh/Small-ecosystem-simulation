package simulation.animals;

import simulation.Ecosystem;
import simulation.util.Vec2;

public class Wolf extends Animal{
    private static final double PROXIMITY_THRESHOLD = 45.0;
    public enum MovementState { WANDERING, HUNTING, CHASING };
    private MovementState state = MovementState.WANDERING;
    private int hunger;
    private int chaseCooldown = 20;
    private int starvationRate = 5;
    private double normalSpeed = 2;
    private double chaseSpeed = 6;
    private int chaseTimer = 40;
    private Vec2 prevPos;
    private Vec2 nearestRabbitPos;

    public Wolf(double x, double y){
        super(x, y);
        setSpeed(normalSpeed);
        setEyesight(600);
        hunger = 1000;
    }

    public MovementState getMovementState(){
        return state;
    }

    private void eat(Rabbit food){
        hunger += food.getMealFactor();
        hunger = Math.min(hunger, 1000);
        food.setAlive(false);
        setSpeed(getSpeed() - 0.01);
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

        if(minDistRabbit != Double.MAX_VALUE && chaseCooldown == 0){
            state = MovementState.CHASING;
        }
        else if (minDistRabbit != Double.MAX_VALUE) {
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
            case CHASING -> {
                setSpeed(chaseSpeed);
                moveToward(nearestRabbitPos);
                setSpeed(normalSpeed);  
            }
            case WANDERING -> moveRandomly();
        }
    }

    @Override
    public void update(Ecosystem e)
    {
        if(this.getAlive()){
            if(chaseCooldown > 0) chaseCooldown--;
            if(state == MovementState.CHASING){
                if(chaseTimer > 0) chaseTimer--;
                else {
                    chaseTimer = 20;
                    chaseCooldown = 20;
                }
            }

            checkMovementState(e);
            prevPos = getPosition();
            move();
            Vec2 facing = new Vec2(prevPos, getPosition());
            setFacing(facing);
            double clampedX = Math.max(PROXIMITY_THRESHOLD, Math.min(getPosition().x, e.getWidth()-PROXIMITY_THRESHOLD));
            double clampedY = Math.max(PROXIMITY_THRESHOLD, Math.min(getPosition().y, e.getHeight()-PROXIMITY_THRESHOLD));
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

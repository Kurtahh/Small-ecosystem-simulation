package simulation.animals;

import simulation.Ecosystem;
import simulation.util.Vec2;

public class Rabbit extends Animal{
    private static final double PROXIMITY_THRESHOLD = 20.0;
    public enum MovementState { WANDERING, FLEEING, SEEKING_MATE }
    private MovementState state = MovementState.WANDERING;
    private int duplicationRate;
    private int duplicationCooldown = 0;
    private int mealFactor;
    private Vec2 nearestWolfPos;
    private Vec2 nearestRabbitPos;

    public Rabbit(double x, double y){
        super(x, y);
        setSpeed(3);
        setEyesight(300);
        duplicationRate = rand.nextInt(10);
        mealFactor = rand.nextInt(201) + 300; // mealFactor = [300;500]
    }

    public MovementState getMovementState(){
        return state;
    }
    public int getMealFactor(){
        return mealFactor;
    }

    private Rabbit duplicate(Rabbit partner) throws CloneNotSupportedException{
        int dupThreshold = rand.nextInt(5) + 1; 
        if(this.duplicationRate > dupThreshold && partner.duplicationRate > dupThreshold){
            Rabbit copy = (Rabbit) partner.clone();
            double newX = getPosition().x + rand.nextInt(10) - 5;
            double newY = getPosition().y + rand.nextInt(10) - 5;
            copy.setPosition(new Vec2(newX, newY));
            copy.mealFactor = rand.nextInt(41) + 10;
            copy.duplicationRate = rand.nextInt(10);
            return copy;
        }
        return null;
    }

    private void checkMovementState(Ecosystem e)
    {
        double minWolfDist = Double.MAX_VALUE;
        for(Wolf wolf : e.getWolves()){
            Vec2 toWolf = new Vec2(getPosition(), wolf.getPosition());
            if(toWolf.length < getEyesight() && toWolf.length < minWolfDist){
                minWolfDist = toWolf.length;
                nearestWolfPos = wolf.getPosition();
            }
        }
        if(minWolfDist != Double.MAX_VALUE){
            state = MovementState.FLEEING;
        }
        else if(duplicationCooldown == 0) {
            double minRabbitDist = Double.MAX_VALUE;
            for(Rabbit r : e.getRabbits()){
                if(r != this){
                    Vec2 toRabbit = new Vec2(getPosition(), r.getPosition());
                    if(toRabbit.length < getEyesight() && toRabbit.length < minRabbitDist){
                        minRabbitDist = toRabbit.length;
                        nearestRabbitPos = r.getPosition();
                    }
                }
            }

            if(minRabbitDist != Double.MAX_VALUE){
                state = MovementState.SEEKING_MATE;    
            }
            else {
                state = MovementState.WANDERING;
            }
        }
        else {
            state = MovementState.WANDERING;
        }
    }

    @Override
    protected void move()
    {
        switch(state){
            case FLEEING -> {
                Vec2 awayFromWolf = new Vec2(nearestWolfPos, getPosition());
                moveInDirection(awayFromWolf);
            }
            case SEEKING_MATE -> moveToward(nearestRabbitPos); 
            case WANDERING -> moveRandomly();
        }
    }

    @Override
    public void update(Ecosystem e)
    {
        if(duplicationCooldown > 0) duplicationCooldown--;

        if(this.getAlive()){
            checkMovementState(e);
            Vec2 prevPos = getPosition();
            move();
            Vec2 facing = new Vec2(prevPos, getPosition());
            setFacing(facing);
            double clampedX = Math.max(PROXIMITY_THRESHOLD, Math.min(getPosition().x, e.getWidth()-PROXIMITY_THRESHOLD));
            double clampedY = Math.max(PROXIMITY_THRESHOLD, Math.min(getPosition().y, e.getHeight()-PROXIMITY_THRESHOLD));
            setPosition(new Vec2(clampedX, clampedY));

            for(Rabbit r : e.getRabbits()){
                if(r.getPosition() == nearestRabbitPos){
                    Vec2 toMate = new Vec2(getPosition(), nearestRabbitPos);
                    if(toMate.length < PROXIMITY_THRESHOLD && duplicationCooldown == 0){
                        try {
                            Rabbit baby = duplicate(r);
                            if(baby != null){
                                e.bufferRabbit(baby);
                                duplicationCooldown = 100;
                            }
                            else duplicationCooldown = 50;
                        }
                        catch(CloneNotSupportedException ex) {
                            //do nothing
                        }
                    }
                }
            }
        }
    }
}

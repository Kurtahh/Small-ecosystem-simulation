package simulation.animals;

import simulation.Ecosystem;

public class Wolf extends Animal{
    private int hunger;
    private int size = 5;
    private int eatCooldown = 3;
    private int starvationRate = 5;

    public Wolf(int x, int y){
        super(x, y);
        setSpeed(2);
        setEyesight(10000);
        hunger = 1000;
    }

    public int getSize(){
        return size;
    }

    private void eat(Rabbit food){
        hunger += food.getMealFactor();
        hunger = Math.min(hunger, 1000);
        food.setAlive(false);
        size++;
        setSpeed(getSpeed() - 0.01);
        starvationRate += 0.1;
    }

    private void depleteHunger(){
        hunger -= starvationRate;
        if(hunger <= 0){
            setAlive(false);
        }
    }

    @Override
    protected void move()
    {
        if(eatCooldown > 0){
            moveRandomly();
            return;
        }

        double minTargetDistance = getEyesight();
        double targetDX = 1;
        double targetDY = 1;
        boolean foundTarget = false;
        for(Rabbit target : Ecosystem.getInstance().getRabbits()){
            double dx = target.getX() - getX();
            double dy = target.getY() - getY();
            double targetDistance = Math.sqrt(dx*dx + dy*dy);
            if(targetDistance < minTargetDistance){
                minTargetDistance = targetDistance;
                targetDX = dx;
                targetDY = dy;
                foundTarget = true;
            }
        }

        if(foundTarget){
            setX(getX() + (targetDX/minTargetDistance) * getSpeed());
            setY(getY() + (targetDY/minTargetDistance) * getSpeed());
        }
        else {
            moveRandomly();
        }
    }

    @Override
    public void update(Ecosystem e)
    {
        if(eatCooldown > 0) eatCooldown--;

        if(this.getAlive()){
            move();
            setX(Math.max(0, Math.min(getX(), e.getWidth())));
            setY(Math.max(0, Math.min(getY(), e.getHeight())));

            for(Rabbit r : e.getRabbits()){
                double dx = r.getX() - this.getX();
                double dy = r.getY() - this.getY();
                double distance = Math.sqrt(dx*dx + dy*dy);
                if(distance < size && r.getAlive() && eatCooldown == 0){
                    this.eat(r);
                }
            }
            
            depleteHunger();
        }
    }
}

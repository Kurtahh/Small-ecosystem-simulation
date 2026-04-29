package simulation.animals;

import simulation.Ecosystem;

public class Wolf extends Animal{
    private int hunger;
    private static final int HUNGER_RATE = 5;

    public Wolf(int x, int y){
        super(x, y);
        setSpeed(2);
        hunger = 1000;
    }

    public void eat(Rabbit food){
        hunger += food.getMealFactor();
        hunger = Math.min(hunger, 100);
        food.setAlive(false);
    }

    public void depleteHunger(){
        hunger -= HUNGER_RATE;
        if(hunger <= 0){
            setAlive(false);
        }
    }

    @Override
    public void update(Ecosystem e)
    {
        if(this.getAlive()){
            move();
            setX(Math.max(0, Math.min(getX(), e.getWidth())));
            setY(Math.max(0, Math.min(getY(), e.getHeight())));

            for(Rabbit r : e.getRabbits()){
                double dx = r.getX() - this.getX();
                double dy = r.getY() - this.getY();
                double distance = Math.sqrt(dx*dx + dy*dy);
                if(distance < PROXIMITY_THRESHOLD && r.getAlive()){
                    this.eat(r);
                }
            }
            
            depleteHunger();
        }
    }
}

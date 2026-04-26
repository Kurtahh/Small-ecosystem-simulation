package simulation.animals;

import simulation.Ecosystem;

public class Wolf extends Animal{
    private int hunger;
    private static final int HUNGER_RATE = 5;

    public Wolf(int x, int y){
        super(x, y);
        setSpeed(2);
        hunger = 100;
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
        
    }
}

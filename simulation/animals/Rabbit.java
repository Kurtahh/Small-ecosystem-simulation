package simulation.animals;

import simulation.Ecosystem;

public class Rabbit extends Animal{
    private int duplicationRate;
    private int mealFactor;

    public Rabbit(int x, int y){
        super(x, y);
        setSpeed(1);
        duplicationRate = rand.nextInt(10);
        mealFactor = rand.nextInt(41) + 10; // mealFactor = [10;50]
    }

    public int getMealFactor(){
        return mealFactor;
    }

    public Rabbit duplicate(Rabbit partner) throws CloneNotSupportedException{
        int dupThreshold = rand.nextInt(5) + 1; 
        if(this.duplicationRate > dupThreshold && partner.duplicationRate > dupThreshold){
            Rabbit copy = (Rabbit) partner.clone();
            copy.mealFactor = rand.nextInt(41) + 10;
            copy.duplicationRate = rand.nextInt(10);
            return copy;
        }
        return null;
    }

    @Override
    public void update(Ecosystem e)
    {
        
    }
}

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

    private Rabbit duplicate(Rabbit partner) throws CloneNotSupportedException{
        int dupThreshold = rand.nextInt(5) + 1; 
        if(this.duplicationRate > dupThreshold && partner.duplicationRate > dupThreshold){
            Rabbit copy = (Rabbit) partner.clone();
            copy.setX(copy.getX() + rand.nextInt(10) - 5);
            copy.setY(copy.getY() + rand.nextInt(10) - 5);
            copy.mealFactor = rand.nextInt(41) + 10;
            copy.duplicationRate = rand.nextInt(10);
            return copy;
        }
        return null;
    }

    @Override
    public void update(Ecosystem e)
    {
        if(this.getAlive()){
            move();
            setX(Math.max(0, Math.min(getX(), e.getWidth())));
            setY(Math.max(0, Math.min(getY(), e.getHeight())));

            for(Rabbit r : e.getRabbits()){
                if(r != this){
                    double dx = r.getX() - this.getX();
                    double dy = r.getY() - this.getY();
                    double distance = Math.sqrt(dx*dx + dy*dy);
                    if(distance < PROXIMITY_THRESHOLD){
                        try {
                            Rabbit baby = duplicate(r);
                            if(baby != null){
                                e.bufferRabbit(baby);
                            }
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

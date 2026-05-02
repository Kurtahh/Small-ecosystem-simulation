package simulation.animals;

import simulation.Ecosystem;

public class Rabbit extends Animal{
    private int duplicationRate;
    private int duplicationCooldown = 0;
    private int mealFactor;

    public Rabbit(int x, int y){
        super(x, y);
        setSpeed(3);
        setEyesight(100);
        duplicationRate = rand.nextInt(10);
        mealFactor = rand.nextInt(201) + 300; // mealFactor = [300;500]
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
    protected void move()
    {
        if(duplicationCooldown == 0){
            double minTargetDistance = getEyesight();
            double targetDX = 1;
            double targetDY = 1;
            boolean foundTarget = false;
            for(Rabbit target : Ecosystem.getInstance().getRabbits()){
                if(target != this){
                    double dx = target.getX() - getX();
                    double dy = target.getY() - getY();
                    double targetDistance = Math.sqrt(dx*dx + dy*dy);
                    if(targetDistance > 0 && targetDistance < minTargetDistance){
                        minTargetDistance = targetDistance;
                        targetDX = dx;
                        targetDY = dy;
                        foundTarget = true;
                    }
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
        else {
            moveRandomly();
        }
    }

    @Override
    public void update(Ecosystem e)
    {
        if(duplicationCooldown > 0) duplicationCooldown--;

        if(this.getAlive()){
            move();
            setX(Math.max(0, Math.min(getX(), e.getWidth())));
            setY(Math.max(0, Math.min(getY(), e.getHeight())));

            for(Rabbit r : e.getRabbits()){
                if(r != this){
                    double dx = r.getX() - this.getX();
                    double dy = r.getY() - this.getY();
                    double distance = Math.sqrt(dx*dx + dy*dy);
                    if(distance < PROXIMITY_THRESHOLD && duplicationCooldown == 0){
                        try {
                            Rabbit baby = duplicate(r);
                            if(baby != null){
                                e.bufferRabbit(baby);
                                duplicationCooldown = 100;
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

package simulation.util;

public class Vec2 {
    public double x;
    public double y;
    public double length;

    public Vec2(double x, double y){
        this.x = x;
        this.y = y;
        length = Math.sqrt(x*x + y*y);
    }
    public Vec2(Vec2 from, Vec2 to){
        this(to.x - from.x, to.y - from.y);
    }

    public void normalize()
    {
        x = x/length;
        y = y/length;
    }
}

package platform.game.actor;// Created by Loris Witschard on 23.11.16.

import platform.util.Box;
import platform.util.Vector;

public class Limits extends Actor
{
    private Box box;
    
    public Limits(Box box)
    {
        if(box == null)
            throw new NullPointerException();
        
        this.box = box;
        
        priority = 1000;
    }
    
    @Override
    public Box getBox()
    {
        return box;
    }
    
    @Override
    public void interact(Actor other)
    {
        super.interact(other);
        if(getBox().isColliding(other.getBox()))
            other.hurt(this, Damage.VOID, Double.POSITIVE_INFINITY, Vector.ZERO);
    }
}

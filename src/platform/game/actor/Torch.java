package platform.game.actor;// Created by Loris Witschard on 25.11.16.

import platform.game.Damage;
import platform.game.signal.Signal;
import platform.util.Box;
import platform.util.Input;
import platform.util.Vector;

public class Torch extends Actor implements Signal
{
    private Vector position;
    private boolean lit = false;
    private double SIZE = 1;
    private double variation = 0;
    
    public Torch(Vector position, boolean lit)
    {
        if(position == null)
            throw new NullPointerException();
        
        this.position = position;
        this.lit = lit;
        
        priority = 30;
    }
    
    @Override
    public Box getBox()
    {
        return new Box(position, SIZE, SIZE);
    }
    
    @Override
    public boolean hurt(Actor instigator, Damage type, double amount, Vector location)
    {
        switch(type)
        {
            case AIR:
                if(!lit)
                    return false;
                lit = false;
                return true;
            
            case FIRE:
                if(lit)
                    return false;
                lit = true;
                return true;
            
            default:
                return super.hurt(instigator, type, amount, location);
        }
    }
    
    @Override
    public void update(Input input)
    {
        super.update(input);
        
        variation -= input.getDeltaTime();
        if(variation < 0.0)
            variation = 0.6;
        
        if(!lit)
            sprite = getSprite("torch");
        else
            sprite = getSprite("torch.lit." + (variation < 0.3 ? "2" : "1"));
    }
    
    @Override
    public boolean isActive()
    {
        return lit;
    }
}

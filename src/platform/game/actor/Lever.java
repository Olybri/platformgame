package platform.game.actor;// Created by Loris Witschard on 25.11.16.

import platform.game.Damage;
import platform.game.signal.Signal;
import platform.util.Box;
import platform.util.Input;
import platform.util.Vector;

public class Lever extends Actor implements Signal
{
    private boolean active = false;
    private Vector position;
    private final double SIZE = 1;
    private double countdown = 0;
    private double countdownMax = 5;
    
    public Lever(Vector position)
    {
        if(position == null)
            throw new NullPointerException();
    
        this.position = position;
        
        priority = 50;
    }
    
    @Override
    public Box getBox()
    {
        return new Box(position, SIZE, SIZE);
    }
    
    @Override
    public void update(Input input)
    {
        super.update(input);
        
        countdown -= input.getDeltaTime();
        if(countdown <= 0)
            active = false;
        
        sprite = getSprite(active ? "lever.left" : "lever.right");
    }
    
    @Override
    public boolean hurt(Actor instigator, Damage type, double amount, Vector location)
    {
        switch(type)
        {
            case ACTIVATION:
                active = !active;
                countdown = countdownMax;
                return true;
            
            default:
                return false;
        }
    }
    
    @Override
    public boolean isActive()
    {
        return active;
    }
}

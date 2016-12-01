package platform.game.actor;// Created by Loris Witschard on 11/28/2016.

import platform.game.Damage;
import platform.game.Signal;
import platform.game.level.Level;
import platform.util.Box;
import platform.util.Input;
import platform.util.Vector;

public class Exit extends Actor
{
    private Vector position;
    private Level next;
    private Signal signal;
    
    private boolean open = false;
    private final double SIZE = 1;
    
    public Exit(Vector position, Level next, Signal signal)
    {
        if(position == null || next == null || signal == null)
            throw new NullPointerException();
        
        this.position = position;
        this.next = next;
        this.signal = signal;
        
        priority = 20;
    }
    
    @Override
    public void update(Input input)
    {
        super.update(input);
    
        if(signal.isActive() && !open)
        {
            getWorld().register(new Smoke(position));
            open = true;
        }
        else if(!signal.isActive() && open)
        {
            getWorld().register(new Smoke(position));
            open = false;
        }
        
        sprite = getSprite(signal.isActive() ? "door.open" : "door.closed");
        priority = open ? 50 : 20;
    }
    
    @Override
    public Box getBox()
    {
        return new Box(position, SIZE, SIZE);
    }
    
    @Override
    public void interact(Actor other)
    {
        super.interact(other);
        if(signal.isActive() && other.getBox().isColliding(position))
            if(other.hurt(this, Damage.ACTIVATION, 1, position))
            {
                getWorld().setNextLevel(next);
                getWorld().nextLevel();
            }
    }
}

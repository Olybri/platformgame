package platform.game.actor;// Created by Loris Witschard on 11/28/2016.

import platform.game.Damage;
import platform.game.signal.Signal;
import platform.game.level.Level;
import platform.util.Box;
import platform.util.Input;
import platform.util.Vector;

/**
 * Exit door that opens whenever the given signal is active. It turns back to closed whenever the given signal is
 * inactive again.
 */
public class Exit extends Actor
{
    private Vector position;
    private Level next;
    private Signal signal;
    
    private boolean open;
    private final double SIZE = 1.25;
    
    /**
     * @param position position of the exit
     * @param next     instance of the next level to be loaded
     * @param signal   signal that opens or closes the exit
     */
    public Exit(Vector position, Level next, Signal signal)
    {
        if(position == null || next == null || signal == null)
            throw new NullPointerException();
        
        this.position = position;
        this.next = next;
        this.signal = signal;
        
        open = signal.isActive();
        
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

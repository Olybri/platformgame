package platform.game.actor;// Created by Loris Witschard on 25.11.16.

import platform.game.ItemColor;
import platform.game.Signal;
import platform.util.Box;
import platform.util.Input;
import platform.util.Vector;

public class Door extends Block
{
    private Signal signal;
    private Vector position;
    private ItemColor color;
    private boolean open = false;
    
    public Door(Vector position, ItemColor color, Signal signal)
    {
        super(new Box(position, 1, 1), "lock." + color.toString());
        
        if(position == null)
            throw new NullPointerException();
        
        this.position = position;
        this.signal = signal;
        this.color = color;
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
    
        sprite = open ? null : getSprite("lock." + color.toString());
    }
    
    @Override
    public boolean isSolid()
    {
        return !signal.isActive();
    }
}

package platform.game.actor;// Created by Loris Witschard on 25.11.16.

import platform.game.ItemColor;
import platform.game.signal.Signal;
import platform.util.Box;
import platform.util.Input;
import platform.util.Output;
import platform.util.Vector;

/**
 * Solid block that disappears whenever the given signal is active. It turns back to solid whenever the given signal is
 * inactive again.
 */
public class Door extends Block
{
    private Signal signal;
    private Vector position;
    private ItemColor color;
    private boolean open;
    
    /**
     * @param position position of the door
     * @param color    color of the sprite
     * @param signal   signal that opens or closes the door
     */
    public Door(Vector position, ItemColor color, Signal signal)
    {
        super(new Box(position, 1, 1), "lock." + color.toString());
        
        if(position == null)
            throw new NullPointerException();
        
        this.position = position;
        this.signal = signal;
        this.color = color;
        
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
        
        sprite = getSprite("lock." + color.toString());
    }
    
    @Override
    public boolean isSolid()
    {
        return !signal.isActive();
    }
    
    @Override
    public void draw(Input input, Output output)
    {
        output.drawSprite(sprite, getBox(), 0, open ? 0.2 : 1);
    }
}

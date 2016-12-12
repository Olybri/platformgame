package platform.game.actor;// Created by Loris Witschard on 25.11.16.

import platform.game.Damage;
import platform.game.ItemColor;
import platform.game.signal.Signal;
import platform.util.Box;
import platform.util.Input;
import platform.util.Output;
import platform.util.Vector;

/**
 * Collectible item that activates a signal once taken.
 */
public class Key extends Actor implements Signal
{
    private Vector position;
    private boolean taken = false;
    private double SIZE = 0.75;
    private ItemColor color;
    
    /**
     * @param position position of the item
     * @param color    color of the sprite
     */
    public Key(Vector position, ItemColor color)
    {
        if(position == null)
            throw new NullPointerException();
        
        this.position = position;
        this.color = color;
        
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
        sprite = getSprite("key." + color.toString());
    }
    
    @Override
    public void draw(Input input, Output output)
    {
        Vector offset = new Vector(0, 0.15 * Math.sin(input.getTime() * 2 + position.getAngle()));
        output.drawSprite(sprite, getBox().add(offset));
    }
    
    @Override
    public void interact(Actor other)
    {
        super.interact(other);
        if(other.getBox().isColliding(position))
            if(other.hurt(this, Damage.ACTIVATION, 1, position))
            {
                taken = true;
                getWorld().register(new Smoke(position));
                getWorld().unregister(this);
            }
    }
    
    @Override
    public boolean isActive()
    {
        return taken;
    }
}

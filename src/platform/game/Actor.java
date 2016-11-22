package platform.game;

import platform.util.*;

/**
 * Base class of all simulated actors, attached to a world.
 */
public abstract class Actor implements Comparable<Actor>
{
    protected int priority = -1;
    protected Sprite sprite = null;
    
    private World world = null;
    
    @Override
    public int compareTo(Actor other)
    {
        if(priority == other.getPriority())
            return 0;
        
        return priority > other.getPriority() ? -1 : 1;
    }
    
    public void register(World world)
    {
        this.world = world;
    }
    
    public void unregister()
    {
        world = null;
    }
    
    protected World getWorld()
    {
        return world;
    }
    
    public final int getPriority()
    {
        return priority;
    }
    
    public void interact(Actor other)
    {
    }
    
    public boolean isSolid()
    {
        return false;
    }
    
    public Box getBox()
    {
        return null;
    }
    
    public Vector getPosition()
    {
        Box box = getBox();
        if(box == null)
            return null;
        return box.getCenter();
    }
    
    public void preUpdate()
    {
    }
    
    public void postUpdate()
    {
    }
    
    public void update(Input input)
    {
    }
    
    public void draw(Input input, Output output)
    {
        output.drawSprite(sprite, getBox());
    }
}
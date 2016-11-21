package platform.game;

import platform.util.Box;
import platform.util.Input;
import platform.util.Vector;
import platform.util.Output;
import platform.util.Sprite;

/**
 * Base class of all simulated actors, attached to a world.
 */
public abstract class Actor implements Comparable<Actor>
{
    private int priority;
    
    public final int getPriority()
    {
        return priority;
    }
    
    @Override
    public int compareTo(Actor other)
    {
        if(priority == other.getPriority())
            return 0;
        
        return priority > other.getPriority() ? -1 : 1;
    }
    
    public void update(Input input)
    {
    }
    
    public void draw(Input input, Output output)
    {
    }
}
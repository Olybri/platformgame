package platform.game.actor;

import platform.game.Damage;
import platform.game.World;
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
    
    /**
     * Register the actor in a world
     *
     * @param world world in which the actor is added
     */
    public void register(World world)
    {
        this.world = world;
    }
    
    /**
     * Unregister the actor from its world
     */
    public void unregister()
    {
        world = null;
    }
    
    /**
     * @return world in which the actor is living
     */
    protected World getWorld()
    {
        return world;
    }
    
    /**
     * @return priority of the actor
     */
    public int getPriority()
    {
        return priority;
    }
    
    /**
     * Interact with another actor
     *
     * @param other actor to interact with
     */
    public void interact(Actor other)
    {
    }
    
    /**
     * @return true if the actor is solid, false otherwise
     */
    public boolean isSolid()
    {
        return false;
    }
    
    /**
     * @return hitbox of the actor
     */
    public Box getBox()
    {
        return null;
    }
    
    /**
     * @return location of the actor
     */
    public Vector getPosition()
    {
        Box box = getBox();
        if(box == null)
            return null;
        return box.getCenter();
    }
    
    /**
     * Executes statements before the main update.
     */
    public void preUpdate()
    {
    }
    
    /**
     * Executes statements after the main update.
     */
    public void postUpdate()
    {
    }
    
    /**
     * Update the actor, depending on the elapsed time.
     *
     * @param input input system of the game
     */
    public void update(Input input)
    {
    }
    
    /**
     * Interpret damage sent from another actor.
     *
     * @param instigator actor which sent damage
     * @param type       damage type
     * @param amount     damage quantity
     * @param location   damage source position
     * @return true if the actor is hurt, false otherwise
     */
    public boolean hurt(Actor instigator, Damage type, double amount, Vector location)
    {
        return false;
    }
    
    /**
     * @param name name of the sprite
     * @return sprite associated to the given name
     */
    public Sprite getSprite(String name)
    {
        if(world == null)
            return null;
        
        return world.getLoader().getSprite(name);
    }
    
    /**
     * Draw the sprite on the given render target.
     *
     * @param input  input system of the game
     * @param output render system of the game
     */
    public void draw(Input input, Output output)
    {
        if(sprite != null)
            output.drawSprite(sprite, getBox());
    }
}
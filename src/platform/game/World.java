package platform.game;

import platform.game.actor.Actor;
import platform.game.level.Level;
import platform.util.Box;
import platform.util.Loader;
import platform.util.Vector;

/**
 * Represents an environment populated by actors.
 */
public interface World
{
    /**
     * @return associated loader, not null
     */
    Loader getLoader();
    
    /**
     * @return current world gravity
     */
    Vector getGravity();
    
    /**
     * Clear the current level and run the next level.
     */
    void nextLevel();
    
    /**
     * Store the next level.
     *
     * @param level instance of the next level
     */
    void setNextLevel(Level level);
    
    /**
     * Cause damage to all actors in a specific area.
     *
     * @param area       targeted area
     * @param instigator actor causing damage
     * @param type       damage type
     * @param amount     damage quantity
     * @param location   damage source position
     * @return number of hurt actors
     */
    int hurt(Box area, Actor instigator, Damage type, double amount, Vector location);
    
    /**
     * Set viewport location and size, with smooth panning.
     *
     * @param center viewport center, not null
     * @param radius viewport radius, positive
     */
    void setView(Vector center, double radius);
    
    /**
     * Reset viewport location and size, without panning.
     *
     * @param center viewport center, not null
     * @param radius viewport radius, positive
     */
    void resetView(Vector center, double radius);
    
    /**
     * @return current view center
     */
    Vector getViewCenter();
    
    /**
     * Add new actor to the world
     *
     * @param actor actor to be registered
     */
    void register(Actor actor);
    
    /**
     * Remove existing actor from the world
     *
     * @param actor actor to be unregistered
     */
    void unregister(Actor actor);
}

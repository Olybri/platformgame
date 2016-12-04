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
    
    Vector getGravity();
    
    // permet d'indiquer que la transition à un autre niveau
    // doit se faire :
    void nextLevel();
    
    // permet de passer au niveau level :
    void setNextLevel(Level level);
    
    int hurt(Box area, Actor instigator, Damage type, double amount, Vector location);
    
    /**
     * Set viewport location and size.
     *
     * @param center viewport center , not null
     * @param radius viewport radius , positive
     */
    void setView(Vector center, double radius);
    
    void resetView(Vector center, double radius);
    
    Vector getViewCenter();
    
    void register(Actor actor);
    
    void unregister(Actor actor);
}

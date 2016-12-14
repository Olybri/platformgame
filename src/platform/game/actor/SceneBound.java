package platform.game.actor;

import platform.util.Vector;

/**
 * Created by Loris on 12/13/2016.
 */
public class SceneBound extends Scenery
{
    public SceneBound()
    {
        super(new Vector(0, 0), "cst/scene.bound", 1935000, 0.9999);
        
        priority = 3000;
    }
}

package platform.game.level;

import platform.game.*;
import platform.game.actor.*;
import platform.util.Box;
import platform.util.Vector;

public class BasicLevel extends Level
{
    @Override
    public void register(World world)
    {
        super.register(world);
        
        // Register a new instance, to restart level automatically
        world.setNextLevel(new BasicLevel());
        
        // Create blocks
        world.register(new Block(new Box(new Vector(0, 0), 4, 2), "stone.broken.2"));
        world.register(new Block(new Box(new Vector(4, 0), 4, 2), "stone.broken.2"));
        world.register(new Block(new Box(new Vector(-1.5, 1.5), 1, 1), "stone.broken.1"));
        world.register(new Jumper(new Vector(0.5, 1.5)));
        world.register(new Heart(new Vector(-1.5, 2.5)));
        world.register(new Spike(new Vector(4.5, 1.5)));
        world.register(new Torch(new Vector(3.5, 2.5), false));
        world.register(new Limits(new Box(new Vector(0, -30), 1000, 30)));
        
        Player player = new Player(new Vector(2, 2));
        world.register(player);
        world.register(new Overlay(player));
    }
}

package platform.game.level;// Created by Loris Witschard on 25.11.16.

import platform.game.And;
import platform.game.ItemColor;
import platform.game.World;
import platform.game.actor.*;
import platform.util.Box;
import platform.util.Vector;

public class BasicInteract extends Level
{
    @Override
    public void register(World world)
    {
        super.register(world);
        
        // Register a new instance, to restart level automatically
        world.setNextLevel(new BasicInteract());
        
        // Create blocks
        world.register(new Block(new Box(new Vector(0, 0), 4, 2), "stone.broken.2"));
        world.register(new Block(new Box(new Vector(-4, 0), 4, 2), "stone.broken.2"));
        world.register(new Block(new Box(new Vector(-6, 4), 4, 2), "stone.broken.2"));
        world.register(new Block(new Box(new Vector(4, 1), 2, 4), "stone.broken.8"));
        world.register(new Block(new Box(new Vector(-2, 2), 2, 2), "stone.broken.1"));
        world.register(new Block(new Box(new Vector(6, 0), 2, 2), "stone.broken.1"));
        world.register(new Block(new Box(new Vector(10, 2), 2, 2), "stone.broken.1"));
//        world.register(new Jumper(new Vector(0, 1.5)));
        world.register(new Heart(new Vector(-2, 3.5)));
        world.register(new Spike(new Vector(4, 3.25)));
        Torch torch = new Torch(new Vector(2.5, 2.5), false);
        world.register(torch);
    
        Key key = new Key(new Vector(10, 3.5), ItemColor.RED);
        world.register(key);
        Lever lever = new Lever(new Vector(6, 1.5));
        world.register(lever);
        world.register(new Door(new Vector(-4.5, 1.5), ItemColor.RED, key));
        world.register(new Door(new Vector(-4.5, 2.5), ItemColor.GREEN, lever));
        world.register(new Mover(new Vector(0, 1.75), new Vector(0, 4), 2, 1.5, 2, "box.double", torch));
        
        world.register(new Exit(new Vector(-6, 5.5), new BasicLevel(), new And(lever, torch)));
        
        world.register(new Limits(new Box(new Vector(0, -30), 1000, 30)));
        world.register(new Scenery("bg.grasslands", new Vector(0, 1), 0.1, 0.5));
        world.register(new Scenery("duck", new Vector( 0, 0), 0.02, -0.5));
        world.register(new Scenery("duck", new Vector( -3, 0.5), 0.02, -0.1));
        world.register(new Scenery("duck", new Vector( 4, 1), 0.02, -0.3));
        world.register(new Scenery("duck", new Vector(-2, 2), 0.02, -0.7));
        world.register(new Scenery("duck", new Vector( 3, 5), 0.02, 0.2));
        world.register(new Scenery("duck", new Vector( 0, 6), 0.02, 0.3));
        world.register(new Scenery("duck", new Vector( -1, 5.5), 0.02, 0.4));
        
        Player player = new Player(new Vector(1.5, 2));
        world.register(player);
        world.register(new Overlay(player));
    }
}

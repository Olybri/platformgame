package platform.game.level;// Created by Loris Witschard on 25.11.16.

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
        world.register(new Block(new Box(new Vector(0, 0), 4, 2), getSprite("stone.broken.2")));
        world.register(new Block(new Box(new Vector(4, 0), 2, 4), getSprite("stone.broken.8")));
        world.register(new Block(new Box(new Vector(-2, 2), 2, 2), getSprite("stone.broken.1")));
        world.register(new Block(new Box(new Vector(6, 0), 2, 2), getSprite("stone.broken.1")));
        world.register(new Jumper(new Vector(0, 1.5)));
        world.register(new Heart(new Vector(-2, 3.5)));
        world.register(new Spike(new Vector(4, 2.5)));
        world.register(new Torch(new Vector(2.5, 2.5), false));
        
        world.register(new Limits(new Box(new Vector(0, -30), 1000, 30)));
        world.register(new Background(getSprite("bg.grasslands")));
        
        Player player = new Player(new Vector(1.5, 2), getSprite("blocker.happy"));
        world.register(player);
        world.register(new Overlay(player));
    }
}

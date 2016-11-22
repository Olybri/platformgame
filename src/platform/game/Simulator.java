package platform.game;

import java.util.ArrayList;

import platform.util.*;

/**
 * Basic implementation of world, managing a complete collection of actors.
 */
public class Simulator implements World
{
    private Loader loader;
    
    private Vector currentCenter;
    private double currentRadius;
    private Vector expectedCenter;
    private double expectedRadius;
    
    private SortedCollection<Actor> actors = new SortedCollection<>();
    private ArrayList<Actor> registered = new ArrayList<>();
    private ArrayList<Actor> unregistered = new ArrayList<>();
    
    private final Vector GRAVITY = new Vector(0, -9.81);
    
    /**
     * Create a new simulator .
     *
     * @param loader associated loader , not null
     */
    public Simulator(Loader loader, String[] args)
    {
        if(loader == null)
            throw new NullPointerException();
        
        this.loader = loader;
        currentCenter = Vector.ZERO;
        expectedCenter = Vector.ZERO;
        currentRadius = 10.0;
        expectedRadius = 10.0;
        
        register(new Block(new Box(new Vector(-4,-1), new Vector(4, 0)), loader.getSprite("box.empty")));
        register(new Block(new Box(new Vector(-2, 0), new Vector(-1, 10)), loader.getSprite("box.empty")));
        register(new Player(new Vector(2, 3), new Vector(0, -1), loader.getSprite("blocker.happy")));
    }
    
    /**
     * Simulate a single step of the simulation.
     *
     * @param input  input object to use, not null
     * @param output output object to use, not null
     */
    public void update(Input input, Output output)
    {
        double factor = 0.001;
        currentCenter = currentCenter.mul(1.0 - factor).add(expectedCenter.mul(factor));
        currentRadius = currentRadius * (1.0 - factor) + expectedRadius * factor;
        
        View view = new View(input, output);
        view.setTarget(currentCenter, currentRadius);
    
        for(Actor actor : actors.descending())
            actor.preUpdate();
        
        for(Actor actor : actors)
            for(Actor other : actors)
                if(actor.getPriority() > other.getPriority())
                    actor.interact(other);
        
        for(Actor actor : actors.descending())
            actor.update(view);
        
        for(Actor actor : actors.descending())
            actor.draw(view, view);
    
        for(Actor actor : actors.descending())
            actor.postUpdate();
        
        // Add registered actors
        for(Actor actor : registered)
            if(!actors.contains(actor))
            {
                actor.register(this);
                actors.add(actor);
            }
        
        registered.clear();
        
        // Remove unregistered actors
        for(Actor actor : unregistered)
        {
            actor.unregister();
            actors.remove(actor);
        }
        
        unregistered.clear();
    }
    
    @Override
    public void setView(Vector center, double radius)
    {
        if(center == null)
            throw new NullPointerException();
        if(radius <= 0.0)
            throw new IllegalArgumentException("radius must be positive");
        expectedCenter = center;
        expectedRadius = radius;
    }
    
    @Override
    public Loader getLoader()
    {
        return loader;
    }
    
    @Override
    public Vector getGravity()
    {
        return GRAVITY;
    }
    
    @Override
    public void register(Actor actor)
    {
        registered.add(actor);
    }
    
    @Override
    public void unregister(Actor actor)
    {
        unregistered.add(actor);
    }
}

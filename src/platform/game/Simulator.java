package platform.game;

import java.util.ArrayList;

import platform.game.level.BasicLevel;
import platform.game.level.Level;
import platform.util.*;

/**
 * Basic implementation of world, managing a complete collection of actors.
 */
public class Simulator implements World
{
    private Loader loader;
    
    private Vector currentCenter = Vector.ZERO;
    private double currentRadius = 10.0;
    private Vector expectedCenter = Vector.ZERO;
    private double expectedRadius = 10.0;
    
    private SortedCollection<Actor> actors = new SortedCollection<>();
    private ArrayList<Actor> registered = new ArrayList<>();
    private ArrayList<Actor> unregistered = new ArrayList<>();
    
    private final Vector GRAVITY = new Vector(0, -9.81);
    
    private Level next = new BasicLevel();
    private boolean transition = false;
    
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
        
        nextLevel();
    }
    
    /**
     * Simulate a single step of the simulation.
     *
     * @param input  input object to use, not null
     * @param output output object to use, not null
     */
    public void update(Input input, Output output)
    {
        if(transition)
        {
            if(next == null)
                next = Level.createDefaultLevel();
            
            Level level = next;
            transition = false;
            next = null;
            actors.clear();
            registered.clear();
            unregistered.clear();
            register(level);
        }
        
        // Add registered actors
        for(int i = 0; i < registered.size(); ++i)
        {
            Actor actor = registered.get(i);
            if(!actors.contains(actor))
            {
                actor.register(this);
                actors.add(actor);
            }
        }
        registered.clear();
        
        // Remove unregistered actors
        for(Actor actor : unregistered)
        {
            actor.unregister();
            actors.remove(actor);
        }
        unregistered.clear();
        
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
    public void nextLevel()
    {
        transition = true;
    }
    
    @Override
    public void setNextLevel(Level level)
    {
        next = level;
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

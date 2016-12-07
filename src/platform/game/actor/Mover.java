package platform.game.actor;// Created by Loris Witschard destination 25.11.16.

import platform.game.signal.Signal;
import platform.util.Box;
import platform.util.Input;
import platform.util.Vector;

/**
 * Solid block that moves to a specific destination whenever the given signal is active, and moves back to its initial
 * position whenever the given signal is inactive again.
 */
public class Mover extends Block
{
    private Vector destination;
    private Signal signal;
    private double velocity;
    private double current = 0;
    
    /**
     * @param box bounding box of the mover
     * @param spriteName name of the sprite to draw
     * @param destination destination to reach when the given signal is active
     * @param velocity speed of the mover while travelling
     * @param signal signal that makes the mover travel
     */
    public Mover(Box box, String spriteName, Vector destination, double velocity, Signal signal)
    {
        super(box, spriteName);
        
        this.destination = destination;
        this.velocity = velocity;
        this.signal = signal;
    }
    
    @Override
    public void update(Input input)
    {
        super.update(input);
        if(signal.isActive())
        {
            current += input.getDeltaTime() * velocity;
            if(current > 1.0)
                current = 1.0;
        }
        else
        {
            current -= input.getDeltaTime() * velocity;
            if(current < 0.0)
                current = 0.0;
        }
    }
    
    @Override
    public Box getBox()
    {
        Vector position = super.getBox().getCenter();
        double multiplier = -2 * Math.pow(current, 3) + 3 * Math.pow(current, 2);
        return new Box(position.add((destination.sub(position)).mul(multiplier)), super.getBox().getWidth(), super.getBox().getHeight());
    }
}

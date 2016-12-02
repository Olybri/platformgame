package platform.game.actor;// Created by Loris Witschard on 25.11.16.

import platform.game.signal.Signal;
import platform.util.Box;
import platform.util.Input;
import platform.util.Vector;

public class Mover extends Block
{
    private Vector on;
    private Vector off;
    private double width;
    private double height;
    private Signal signal;
    private double velocity;
    private double current = 0;
    
    public Mover(Vector off, Vector on, double width, double height, double velocity, String spriteName, Signal signal)
    {
        super(new Box(off, width, height), spriteName);
        
        this.on = on;
        this.off = off;
        this.signal = signal;
        this.width = width;
        this.height = height;
        this.velocity = velocity;
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
        double multiplier = -2 * Math.pow(current, 3) + 3 * Math.pow(current, 2);
        return new Box(off.add((on.sub(off)).mul(multiplier)), width, height);
    }
}

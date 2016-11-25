package platform.game.actor;// Created by Loris Witschard on 24.11.16.

import platform.game.Damage;
import platform.util.Box;
import platform.util.Input;
import platform.util.Vector;

public class Spike extends Actor
{
    private Vector position;
    private final double SIZE = 1;
    
    private double cooldown = 0;
    private final double cooldownMax = 0.5;
    
    public Spike(Vector position)
    {
        if(position == null)
            throw new NullPointerException();
    
        this.position = position;
    
        priority = 50;
    }
    
    @Override
    public Box getBox()
    {
        return new Box(position, SIZE, SIZE);
    }
    
    @Override
    public void interact(Actor other)
    {
        super.interact(other);
    
        if(cooldown <= 0 && getBox().isColliding(other.getBox()))
            if(other.hurt(this, Damage.PHYSICAL, 0.4, position.add(new Vector(0, SIZE/2))))
                cooldown = cooldownMax;
            else
                other.interact(this);
    }
    
    @Override
    public boolean isSolid()
    {
        return true;
    }
    
    @Override
    public void update(Input input)
    {
        super.update(input);
        cooldown -= input.getDeltaTime();
        sprite = getSprite("spikes");
    }
}

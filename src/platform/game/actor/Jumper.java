package platform.game.actor;// Created by Loris Witschard on 23.11.16.

import platform.util.*;

public class Jumper extends Actor
{
    private Vector position;
    private final double SIZE = 1;
    
    private double cooldown = 0;
    private final double cooldownMax =  0.5;
    
    public Jumper(Vector position)
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
    public void update(Input input)
    {
        super.update(input);
        cooldown -= input.getDeltaTime();
        
        if(cooldown > 0)
            sprite = getSprite("jumper.normal");
        else
            sprite = getSprite("jumper.extended");
    }
    
    @Override
    public void interact(Actor other)
    {
        super.interact(other);
        if(cooldown <= 0 && getBox().isColliding(other.getBox())
            && other.getPosition().getY() > position.getY() + SIZE/2)
        {
            Vector below = new Vector(position.getX(), position.getY() - 1);
            if(other.hurt(this, Damage.AIR, 10.0, below))
                cooldown = cooldownMax;
    
        }
        else if(cooldown < cooldownMax - 0.1)
            other.interact(this);
    }
    
    @Override
    public boolean isSolid()
    {
        return true;
    }
}
